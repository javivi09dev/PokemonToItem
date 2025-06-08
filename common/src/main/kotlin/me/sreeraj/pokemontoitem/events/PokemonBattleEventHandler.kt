package me.sreeraj.pokemontoitem.events

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent
import com.cobblemon.mod.common.battles.BattleFormat
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor
import me.sreeraj.pokemontoitem.PokemonToItem
import me.sreeraj.pokemontoitem.util.PlayerDataManager
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object PokemonBattleEventHandler {
    
    fun register() {
        CobblemonEvents.BATTLE_VICTORY.subscribe { event ->
            handleBattleVictory(event)
        }
    }
    
    private fun handleBattleVictory(event: BattleVictoryEvent) {
        if (!PokemonToItem.config.coins.ENABLE_BATTLE_REWARDS) {
            return
        }
        
                try {
            // Filtrar solo los ganadores que son jugadores
            val playerWinners = event.winners.filter { it.type == ActorType.PLAYER }
                .filterIsInstance<PlayerBattleActor>()
            
            if (playerWinners.isEmpty()) {
                return
            }

            val battleFormat = event.battle.format
            val coinsToGive = calculateBattleReward(battleFormat)
            val battleType = getBattleTypeDescription(battleFormat)
            
            if (coinsToGive <= 0) {
                return
            }

            // Procesar cada jugador ganador
            playerWinners.forEach { playerActor ->
                val serverPlayer = playerActor.entity as? ServerPlayer
                
                if (serverPlayer != null) {
                    PlayerDataManager.addCoins(serverPlayer, coinsToGive.toLong())
                    
                    // Crear mensaje de recompensa
                    val message = Component.literal("⚔️ ¡Has ganado la batalla ")
                        .withStyle(ChatFormatting.GOLD)
                        .append(
                            Component.literal(battleType)
                                .withStyle(ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD)
                        )
                        .append(
                            Component.literal(" y recibido ")
                                .withStyle(ChatFormatting.GOLD)
                        )
                        .append(
                            Component.literal("$coinsToGive LyxCoins")
                                .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                        )
                        .append(
                            Component.literal("!")
                                .withStyle(ChatFormatting.GOLD)
                        )
                    
                    serverPlayer.sendSystemMessage(message)
                    
                    PokemonToItem.getLogger().info(
                        "${serverPlayer.name.string} ganó una batalla $battleType y recibió $coinsToGive monedas"
                    )
                }
            }
        } catch (e: Exception) {
            PokemonToItem.getLogger().error("Error al manejar victoria de batalla: ${e.message}")
        }
    }
    
    private fun calculateBattleReward(format: BattleFormat): Int {
        return when {
            // Batalla PvP (Jugador vs Jugador)
            format.battleType.name.contains("pvp", ignoreCase = true) -> {
                PokemonToItem.config.coins.COINS_PER_PVP_VICTORY
            }
            // Batalla contra entrenador NPC
            format.battleType.name.contains("pvn", ignoreCase = true) -> {
                PokemonToItem.config.coins.COINS_PER_TRAINER_VICTORY
            }
            // Batalla contra Pokémon salvaje (PvW)
            format.battleType.name.contains("pvw", ignoreCase = true) -> {
                PokemonToItem.config.coins.COINS_PER_BATTLE_VICTORY
            }
            // Batalla general
            else -> {
                PokemonToItem.config.coins.COINS_PER_BATTLE_VICTORY
            }
        }
    }
    
    private fun getBattleTypeDescription(format: BattleFormat): String {
        return when {
            format.battleType.name.contains("pvp", ignoreCase = true) -> "PvP"
            format.battleType.name.contains("pvn", ignoreCase = true) -> "contra entrenador"
            format.battleType.name.contains("pvw", ignoreCase = true) -> "contra Pokémon salvaje"
            else -> "general"
        }
    }
} 