package me.sreeraj.pokemontoitem.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import me.sreeraj.pokemontoitem.PokemonToItem
import net.minecraft.server.level.ServerPlayer
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerDataManager {
    private val playerData = ConcurrentHashMap<UUID, PlayerData>()
    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    
    private val dataDirectory = File(System.getProperty("user.dir"), "config/pokemontoitem/playerdata")
    
    init {
        dataDirectory.mkdirs()
    }
    
    fun getPlayerData(player: ServerPlayer): PlayerData {
        return getPlayerData(player.uuid)
    }
    
    fun getPlayerData(playerId: UUID): PlayerData {
        return playerData.computeIfAbsent(playerId) { loadPlayerData(it) }
    }
    
    private fun loadPlayerData(playerId: UUID): PlayerData {
        val file = File(dataDirectory, "$playerId.json")
        
        return if (file.exists()) {
            try {
                val reader = FileReader(file)
                val data = gson.fromJson(reader, PlayerData::class.java)
                reader.close()
                data
            } catch (e: Exception) {
                PokemonToItem.getLogger().warn("Error al cargar datos del jugador $playerId: ${e.message}")
                PlayerData()
            }
        } else {
            PlayerData()
        }
    }
    
    fun savePlayerData(playerId: UUID) {
        val data = playerData[playerId] ?: return
        val file = File(dataDirectory, "$playerId.json")
        
        try {
            val writer = FileWriter(file)
            gson.toJson(data, writer)
            writer.close()
        } catch (e: Exception) {
            PokemonToItem.getLogger().error("Error al guardar datos del jugador $playerId: ${e.message}")
        }
    }
    
    fun saveAllPlayerData() {
        playerData.keys.forEach { savePlayerData(it) }
    }
    
    // Métodos con ServerPlayer
    fun addCoins(player: ServerPlayer, amount: Long) {
        val data = getPlayerData(player)
        data.coins += amount
        savePlayerData(player.uuid)
        PokemonToItem.getLogger().info("${player.name.string} recibió $amount monedas LyxMon. Total: ${data.coins}")
    }
    
    fun removeCoins(player: ServerPlayer, amount: Long): Boolean {
        val data = getPlayerData(player)
        if (data.coins >= amount) {
            data.coins -= amount
            savePlayerData(player.uuid)
            return true
        }
        return false
    }
    
    fun setCoins(player: ServerPlayer, amount: Long) {
        val data = getPlayerData(player)
        data.coins = amount
        savePlayerData(player.uuid)
        PokemonToItem.getLogger().info("${player.name.string} ahora tiene $amount monedas LyxMon")
    }
    
    fun getCoins(player: ServerPlayer): Long {
        return getPlayerData(player).coins
    }
    
    // Métodos con UUID
    fun addCoins(playerId: UUID, amount: Long) {
        val data = getPlayerData(playerId)
        data.coins += amount
        savePlayerData(playerId)
        PokemonToItem.getLogger().info("Jugador $playerId recibió $amount monedas LyxMon. Total: ${data.coins}")
    }
    
    fun removeCoins(playerId: UUID, amount: Long): Boolean {
        val data = getPlayerData(playerId)
        if (data.coins >= amount) {
            data.coins -= amount
            savePlayerData(playerId)
            return true
        }
        return false
    }
    
    fun setCoins(playerId: UUID, amount: Long) {
        val data = getPlayerData(playerId)
        data.coins = amount
        savePlayerData(playerId)
        PokemonToItem.getLogger().info("Jugador $playerId ahora tiene $amount monedas LyxMon")
    }
    
    fun getCoins(playerId: UUID): Long {
        return getPlayerData(playerId).coins
    }
}

data class PlayerData(
    var coins: Long = 0
) 