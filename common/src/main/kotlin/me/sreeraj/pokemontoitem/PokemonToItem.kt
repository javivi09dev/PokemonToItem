package me.sreeraj.pokemontoitem


import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojang.brigadier.CommandDispatcher
import me.sreeraj.pokemontoitem.commands.ItemToPoke
import me.sreeraj.pokemontoitem.commands.PokeToItem
import me.sreeraj.pokemontoitem.config.PokemonToItemConfig
import me.sreeraj.pokemontoitem.permissions.PokemonToItemPermissions
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
    }

    fun onShutdown() {
        System.out.println("PokemonToItem - Shutting Down")
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
                System.err.println("[PokemonToItem] Failed to load the config! Using default config as fallback")
                e.printStackTrace()
                config = PokemonToItemConfig()
            }
        } else {
            config = PokemonToItemConfig()
        }
        saveConfig()
    }

    private fun mergeConfigs(defaultConfig: JsonObject, fileConfig: JsonObject): JsonElement {
        // For every entry in the default config, check if it exists in the file config
        getLogger().info("Checking for config merge.")
        var merged = false
        for (key in defaultConfig.keySet()) {
            if (!fileConfig.has(key)) {
                // If the file config does not have the key, add it from the default config
                fileConfig.add(key, defaultConfig.get(key))
                getLogger().info("[PokemonToItem] $key not found in file config, adding from default.")
                merged = true
            } else {
                // If it's a nested object, recursively merge it
                if (defaultConfig.get(key).isJsonObject && fileConfig.get(key).isJsonObject) {
                    mergeConfigs(defaultConfig.getAsJsonObject(key), fileConfig.getAsJsonObject(key))
                }
            }
        }
        if (merged) {
            getLogger().info("[PokemonToItem] Successfully merged config.")
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
            System.err.println("[PokemonToItem] Failed to save config")
            e.printStackTrace()
        }
    }

    fun registerCommands(dispatcher: CommandDispatcher<CommandSourceStack>) {
        getLogger().info("PokemonToItem registerCommands")
        PokeToItem().register(dispatcher)
        ItemToPoke().register(dispatcher)
    }

}
