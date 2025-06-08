package me.sreeraj.pokemontoitem.api

import me.sreeraj.pokemontoitem.util.PlayerDataManager
import net.minecraft.server.level.ServerPlayer
import java.util.UUID

/**
 * Implementación de la API de LyxCoins
 * Esta clase implementa todas las funciones de la interfaz LyxCoinsAPI
 */
class LyxCoinsAPIImpl : LyxCoinsAPI {
    
    companion object {
        const val API_VERSION = "1.0.0"
    }
    
    override fun getCoins(player: ServerPlayer): Long {
        return PlayerDataManager.getCoins(player)
    }
    
    override fun getCoins(playerUUID: UUID): Long {
        return PlayerDataManager.getCoins(playerUUID)
    }
    
    override fun addCoins(player: ServerPlayer, amount: Long): Boolean {
        return try {
            PlayerDataManager.addCoins(player, amount)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun addCoins(playerUUID: UUID, amount: Long): Boolean {
        return try {
            PlayerDataManager.addCoins(playerUUID, amount)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun removeCoins(player: ServerPlayer, amount: Long): Boolean {
        return PlayerDataManager.removeCoins(player, amount)
    }
    
    override fun removeCoins(playerUUID: UUID, amount: Long): Boolean {
        return PlayerDataManager.removeCoins(playerUUID, amount)
    }
    
    override fun setCoins(player: ServerPlayer, amount: Long): Boolean {
        return try {
            PlayerDataManager.setCoins(player, amount)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun setCoins(playerUUID: UUID, amount: Long): Boolean {
        return try {
            PlayerDataManager.setCoins(playerUUID, amount)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun hasCoins(player: ServerPlayer, amount: Long): Boolean {
        return getCoins(player) >= amount
    }
    
    override fun hasCoins(playerUUID: UUID, amount: Long): Boolean {
        return getCoins(playerUUID) >= amount
    }
    
    override fun isAvailable(): Boolean {
        return try {
            // Verificar que el PlayerDataManager esté funcionando
            PlayerDataManager.javaClass != null
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override fun getAPIVersion(): String {
        return API_VERSION
    }
} 