package me.sreeraj.pokemontoitem.api

import net.minecraft.server.level.ServerPlayer
import java.util.UUID

/**
 * API pública para el sistema de LyxCoins
 * Permite a otros mods interactuar con las monedas de los jugadores
 */
interface LyxCoinsAPI {
    
    /**
     * Obtiene la cantidad de LyxCoins de un jugador
     * @param player El jugador del cual obtener las monedas
     * @return La cantidad de LyxCoins del jugador
     */
    fun getCoins(player: ServerPlayer): Long
    
    /**
     * Obtiene la cantidad de LyxCoins de un jugador por UUID
     * @param playerUUID El UUID del jugador
     * @return La cantidad de LyxCoins del jugador
     */
    fun getCoins(playerUUID: UUID): Long
    
    /**
     * Añade LyxCoins a un jugador
     * @param player El jugador al cual añadir monedas
     * @param amount La cantidad de monedas a añadir
     * @return true si la operación fue exitosa
     */
    fun addCoins(player: ServerPlayer, amount: Long): Boolean
    
    /**
     * Añade LyxCoins a un jugador por UUID
     * @param playerUUID El UUID del jugador
     * @param amount La cantidad de monedas a añadir
     * @return true si la operación fue exitosa
     */
    fun addCoins(playerUUID: UUID, amount: Long): Boolean
    
    /**
     * Remueve LyxCoins de un jugador
     * @param player El jugador del cual remover monedas
     * @param amount La cantidad de monedas a remover
     * @return true si la operación fue exitosa (el jugador tenía suficientes monedas)
     */
    fun removeCoins(player: ServerPlayer, amount: Long): Boolean
    
    /**
     * Remueve LyxCoins de un jugador por UUID
     * @param playerUUID El UUID del jugador
     * @param amount La cantidad de monedas a remover
     * @return true si la operación fue exitosa (el jugador tenía suficientes monedas)
     */
    fun removeCoins(playerUUID: UUID, amount: Long): Boolean
    
    /**
     * Establece la cantidad exacta de LyxCoins de un jugador
     * @param player El jugador al cual establecer las monedas
     * @param amount La cantidad exacta de monedas
     * @return true si la operación fue exitosa
     */
    fun setCoins(player: ServerPlayer, amount: Long): Boolean
    
    /**
     * Establece la cantidad exacta de LyxCoins de un jugador por UUID
     * @param playerUUID El UUID del jugador
     * @param amount La cantidad exacta de monedas
     * @return true si la operación fue exitosa
     */
    fun setCoins(playerUUID: UUID, amount: Long): Boolean
    
    /**
     * Verifica si un jugador tiene suficientes LyxCoins
     * @param player El jugador a verificar
     * @param amount La cantidad de monedas a verificar
     * @return true si el jugador tiene al menos la cantidad especificada
     */
    fun hasCoins(player: ServerPlayer, amount: Long): Boolean
    
    /**
     * Verifica si un jugador tiene suficientes LyxCoins por UUID
     * @param playerUUID El UUID del jugador
     * @param amount La cantidad de monedas a verificar
     * @return true si el jugador tiene al menos la cantidad especificada
     */
    fun hasCoins(playerUUID: UUID, amount: Long): Boolean
    
    /**
     * Verifica si el sistema de LyxCoins está disponible
     * @return true si el sistema está disponible y funcionando
     */
    fun isAvailable(): Boolean
    
    /**
     * Obtiene la versión de la API
     * @return La versión de la API como string
     */
    fun getAPIVersion(): String
} 