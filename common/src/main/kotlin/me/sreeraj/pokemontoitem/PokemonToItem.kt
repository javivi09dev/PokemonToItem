package me.sreeraj.pokemontoitem


import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.brigadier.CommandDispatcher
import me.sreeraj.pokemontoitem.commands.ItemToPoke
import me.sreeraj.pokemontoitem.commands.LyxMonCommand
import me.sreeraj.pokemontoitem.commands.PokeToItem
import me.sreeraj.pokemontoitem.commands.ScoreboardCommand
import me.sreeraj.pokemontoitem.config.PokemonToItemConfig
import me.sreeraj.pokemontoitem.events.PokemonBattleEventHandler
import me.sreeraj.pokemontoitem.events.PokemonCaptureEventHandler
import me.sreeraj.pokemontoitem.permissions.PokemonToItemPermissions
import me.sreeraj.pokemontoitem.util.PlayerDataManager
import net.minecraft.commands.CommandSourceStack
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.io.FileReader
import java.io.FileWriter


object PokemonToItem {
    lateinit var permissions: PokemonToItemPermissions
    const val MODID = "pokemontoitem"
    lateinit var config: PokemonToItemConfig
    var LOGGER: Logger = LogManager.getLogger("[PokemonToItem]")

    fun initialize() {
        getLogger().info("PokemonToItem - Initialized")
        loadConfig() // must load before permissions so perms use default permission level.

        this.permissions = PokemonToItemPermissions()
        
        // Registrar eventos
        PokemonCaptureEventHandler.register()
        PokemonBattleEventHandler.register()
        getLogger().info("Eventos de captura y batalla de Pokémon registrados")
    }

    fun onShutdown() {
        System.out.println("PokemonToItem - Shutting Down")
        PlayerDataManager.saveAllPlayerData()
        getLogger().info("Datos de jugadores guardados.")
    }

    fun getLogger(): Logger {
        return this.LOGGER
    }

    fun loadConfig() {
        val configFileLoc = System.getProperty("user.dir") + File.separator + "config" + File.separator + "pokemontoitem" + File.separator + "config.json"
        System.out.println("Loading config file found at: $configFileLoc")
        val configFile: File = File(configFileLoc)
        configFile.parentFile.mkdirs()

        // Check config existence and load if it exists, otherwise create default.
        if (configFile.exists()) {
            try {
                val fileReader = FileReader(configFile)


                // Create a default config instance
                val defaultConfig = PokemonToItemConfig()
                val defaultConfigJson: String = PokemonToItemConfig.GSON.toJson(defaultConfig)


                val fileConfigElement: JsonElement = JsonParser.parseReader(fileReader)


                // Convert default config JSON string to JsonElement
                val defaultConfigElement: JsonElement = JsonParser.parseString(defaultConfigJson)


                // Merge default config with the file config
                val mergedConfigElement: JsonElement = mergeConfigs(defaultConfigElement.getAsJsonObject(), fileConfigElement.getAsJsonObject())


                // Deserialize the merged JsonElement back to PokemonToItemConfig
                val finalConfig: PokemonToItemConfig = PokemonToItemConfig.GSON.fromJson(mergedConfigElement, PokemonToItemConfig::class.java)

                this.config = finalConfig

                fileReader.close()
            } catch (e: Exception) {
                System.err.println("[LyxMon] Fallo al cargar la configuración! Usando la configuración por defecto como fallback")
                e.printStackTrace()
                config = PokemonToItemConfig()
            }
        } else {
            config = PokemonToItemConfig()
        }
        saveConfig()
    }

    private fun mergeConfigs(defaultConfig: JsonObject, fileConfig: JsonObject): JsonElement {
        getLogger().info("Buscando merge de configuración.")
        var merged = false
        for (key in defaultConfig.keySet()) {
            if (!fileConfig.has(key)) {
                fileConfig.add(key, defaultConfig.get(key))
                getLogger().info("[LyxMon] $key no encontrado en la configuración, agregando desde la configuración por defecto.")
                merged = true
            } else {
                // If it's a nested object, recursively merge it
                if (defaultConfig.get(key).isJsonObject && fileConfig.get(key).isJsonObject) {
                    mergeConfigs(defaultConfig.getAsJsonObject(key), fileConfig.getAsJsonObject(key))
                }
            }
        }
        if (merged) {
            getLogger().info("[LyxMon] Configuración fusionada correctamente.")
        }
        return fileConfig
    }

    private fun saveConfig() {
        try {
            val configFileLoc = System.getProperty("user.dir") + File.separator + "config" + File.separator + "pokemontoitem" + File.separator + "config.json"
            System.out.println("Saving config to: $configFileLoc")
            val configFile: File = File(configFileLoc)
            val fileWriter = FileWriter(configFile)
            PokemonToItemConfig.GSON.toJson(config, fileWriter)
            fileWriter.flush()
            fileWriter.close()
        } catch (e: java.lang.Exception) {
            System.err.println("[LyxMon] Fallo al guardar la configuración")
            e.printStackTrace()
        }
    }

    fun reloadConfig() {
        loadConfig()
        getLogger().info("Configuración recargada exitosamente")
    }

    fun registerCommands(dispatcher: CommandDispatcher<CommandSourceStack>) {
        getLogger().info("LyxMon registerCommands")
        PokeToItem().register(dispatcher)
        ItemToPoke().register(dispatcher)
        LyxMonCommand().register(dispatcher)
        ScoreboardCommand.register(dispatcher)
    }

}
