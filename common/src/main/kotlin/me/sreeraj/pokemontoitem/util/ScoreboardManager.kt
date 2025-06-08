package me.sreeraj.pokemontoitem.util

import com.cobblemon.mod.common.Cobblemon.storage
import me.sreeraj.pokemontoitem.PokemonToItem
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket
import net.minecraft.network.protocol.game.ClientboundSetScorePacket
import net.minecraft.network.protocol.game.ClientboundResetScorePacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.scores.*
import net.minecraft.world.scores.criteria.ObjectiveCriteria
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt

object ScoreboardManager {
    
    private val playerScoreboards = ConcurrentHashMap<UUID, String>()
    private val playerLines = ConcurrentHashMap<UUID, MutableList<String>>()
    
    /**
     * Crea y muestra un scoreboard para un jugador
     */
    fun createScoreboard(player: ServerPlayer) {
        if (!PokemonToItem.config.scoreboard.ENABLED) {
            return
        }
        
        removeScoreboard(player)
        
        val objectiveName = "lyxcraft_sb"
        
        // Crear objetivo
        val objective = Objective(
            null, // scoreboard
            objectiveName,
            ObjectiveCriteria.DUMMY,
            ColorUtils.colorize(PokemonToItem.config.scoreboard.TITLE),
            ObjectiveCriteria.RenderType.INTEGER,
            true, // displayAutoUpdate  
            null // numberFormat
        )
        
        // Guardar referencias
        playerScoreboards[player.uuid] = objectiveName
        playerLines[player.uuid] = mutableListOf()
        
        // Enviar packets al cliente
        player.connection.send(
            ClientboundSetObjectivePacket(objective, ClientboundSetObjectivePacket.METHOD_ADD)
        )
        
        player.connection.send(
            ClientboundSetDisplayObjectivePacket(DisplaySlot.SIDEBAR, objective)
        )
        
        // Actualizar contenido inicial
        updateScoreboard(player)
    }
    
    /**
     * Actualiza el contenido del scoreboard de un jugador
     */
    fun updateScoreboard(player: ServerPlayer) {
        if (!PokemonToItem.config.scoreboard.ENABLED) {
            return
        }
        
        val objectiveName = playerScoreboards[player.uuid] ?: return
        val oldLines = playerLines[player.uuid] ?: mutableListOf()
        
        // Limpiar scores anteriores
        oldLines.forEach { line ->
            player.connection.send(
                ClientboundResetScorePacket(line, objectiveName)
            )
        }
        
        // Procesar líneas con placeholders
        val processedLines = processPlaceholders(player, PokemonToItem.config.scoreboard.LINES)
        val newLines = mutableListOf<String>()
        
        // Agregar líneas al scoreboard (en orden inverso porque Minecraft las muestra al revés)
        processedLines.reversed().forEachIndexed { index, line ->
            if (line.isNotBlank()) {
                val uniqueEntry = getUniqueEntry(line, index)
                newLines.add(uniqueEntry)
                
                // Enviar score al cliente
                player.connection.send(
                    ClientboundSetScorePacket(
                        uniqueEntry,
                        objectiveName,
                        index,
                        Optional.of(ColorUtils.colorize(line)),
                        Optional.empty() // numberFormat
                    )
                )
            }
        }
        
        // Actualizar líneas guardadas
        playerLines[player.uuid] = newLines
    }
    

    
    /**
     * Procesa placeholders en las líneas del scoreboard
     */
    private fun processPlaceholders(player: ServerPlayer, lines: List<String>): List<String> {
        return lines.map { line ->
            var processedLine = line
            
            // Placeholders básicos
            processedLine = processedLine.replace("{player_name}", player.name.string)
            processedLine = processedLine.replace("{player_coins}", PlayerDataManager.getCoins(player).toString())
            processedLine = processedLine.replace("{server_name}", PokemonToItem.config.scoreboard.PLACEHOLDERS.SERVER_NAME)
            
            // Hora actual
            val currentTime = LocalDateTime.now()
            val timeFormatter = DateTimeFormatter.ofPattern(PokemonToItem.config.scoreboard.PLACEHOLDERS.TIME_FORMAT)
            processedLine = processedLine.replace("{time}", currentTime.format(timeFormatter))
            
            // Posición del jugador
            val pos = player.position()
            val locationFormat = PokemonToItem.config.scoreboard.PLACEHOLDERS.LOCATION_FORMAT
                .replace("{x}", pos.x.roundToInt().toString())
                .replace("{y}", pos.y.roundToInt().toString())
                .replace("{z}", pos.z.roundToInt().toString())
            processedLine = processedLine.replace("{player_location}", locationFormat)
            
            // Cantidad de Pokémon
            val partyStorage = storage.getParty(player)
            val pokemonCount = partyStorage.count { it != null }
            processedLine = processedLine.replace("{pokemon_count}", pokemonCount.toString())
            
            processedLine
        }
    }
    
    /**
     * Genera un entry único para evitar conflictos en el scoreboard
     */
    private fun getUniqueEntry(text: String, index: Int): String {
        // Minecraft requiere entries únicos, así que agregamos caracteres invisibles
        val invisibleChars = "\u00A7r".repeat(index)
        return text + invisibleChars
    }
    
    /**
     * Verifica si un jugador tiene un scoreboard activo
     */
    fun hasScoreboard(player: ServerPlayer): Boolean {
        return playerScoreboards.containsKey(player.uuid)
    }
    
    /**
     * Remueve el scoreboard de un jugador
     */
    fun removeScoreboard(player: ServerPlayer) {
        val objectiveName = playerScoreboards.remove(player.uuid)
        if (objectiveName != null) {
            val objective = Objective(
                null,
                objectiveName,
                ObjectiveCriteria.DUMMY,
                ColorUtils.colorize(PokemonToItem.config.scoreboard.TITLE),
                ObjectiveCriteria.RenderType.INTEGER,
                true,
                null
            )
            player.connection.send(
                ClientboundSetObjectivePacket(objective, ClientboundSetObjectivePacket.METHOD_REMOVE)
            )
        }
        playerLines.remove(player.uuid)
    }
    
    /**
     * Actualiza los scoreboards de todos los jugadores en línea
     */
    fun updateAllScoreboards(server: net.minecraft.server.MinecraftServer) {
        if (!PokemonToItem.config.scoreboard.ENABLED) {
            return
        }
        
        server.playerList.players.forEach { player ->
            if (playerScoreboards.containsKey(player.uuid)) {
                updateScoreboard(player)
            }
        }
    }
    
    /**
     * Limpia todos los scoreboards
     */
    fun clearAll() {
        playerScoreboards.clear()
        playerLines.clear()
    }
} 