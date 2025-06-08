package me.sreeraj.pokemontoitem.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import me.sreeraj.pokemontoitem.PokemonToItem
import me.sreeraj.pokemontoitem.permissions.PokemonToItemPermissions
import me.sreeraj.pokemontoitem.util.ScoreboardManager
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object ScoreboardCommand {
    
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal("lyxscoreboard")
                .then(
                    Commands.literal("toggle")
                        .executes { toggleScoreboard(it) }
                )
                .then(
                    Commands.literal("reload")
                        .requires { source ->
                            PokemonToItemPermissions.checkPermission(source, PokemonToItem.permissions.LYXSCOREBOARD_RELOAD_PERMISSION)
                        }
                        .executes { reloadScoreboard(it) }
                )
                .then(
                    Commands.literal("show")
                        .then(
                            Commands.argument("player", EntityArgument.player())
                                .requires { source ->
                                    PokemonToItemPermissions.checkPermission(source, PokemonToItem.permissions.LYXSCOREBOARD_MANAGE_OTHERS_PERMISSION)
                                }
                                .executes { showScoreboardToPlayer(it) }
                        )
                        .executes { showScoreboard(it) }
                )
                .then(
                    Commands.literal("hide")
                        .then(
                            Commands.argument("player", EntityArgument.player())
                                .requires { source ->
                                    PokemonToItemPermissions.checkPermission(source, PokemonToItem.permissions.LYXSCOREBOARD_MANAGE_OTHERS_PERMISSION)
                                }
                                .executes { hideScoreboardFromPlayer(it) }
                        )
                        .executes { hideScoreboard(it) }
                )
        )
    }
    
    private fun toggleScoreboard(ctx: CommandContext<CommandSourceStack>): Int {
        val player: ServerPlayer = ctx.source.player!!
        
        if (ScoreboardManager.hasScoreboard(player)) {
            ScoreboardManager.removeScoreboard(player)
            player.sendSystemMessage(
                Component.literal("📋 Scoreboard ocultado.")
                    .withStyle { it.withColor(0xFFAA00) }
            )
        } else {
            ScoreboardManager.createScoreboard(player)
            player.sendSystemMessage(
                Component.literal("📋 Scoreboard mostrado.")
                    .withStyle { it.withColor(0x55FF55) }
            )
        }
        
        return 1
    }
    
    private fun reloadScoreboard(ctx: CommandContext<CommandSourceStack>): Int {
        val source = ctx.source
        
        try {
            // Recargar configuración
            PokemonToItem.reloadConfig()
            
            // Actualizar todos los scoreboards
            source.server.playerList.players.forEach { player ->
                if (ScoreboardManager.hasScoreboard(player)) {
                    ScoreboardManager.createScoreboard(player)
                }
            }
            
            source.sendSystemMessage(
                Component.literal("📋 Configuración del scoreboard recargada exitosamente.")
                    .withStyle { it.withColor(0x55FF55) }
            )
            
            PokemonToItem.getLogger().info("Configuración del scoreboard recargada por ${source.entity?.name?.string ?: "Consola"}")
            
        } catch (e: Exception) {
            source.sendSystemMessage(
                Component.literal("❌ Error al recargar la configuración: ${e.message}")
                    .withStyle { it.withColor(0xFF5555) }
            )
            
            PokemonToItem.getLogger().error("Error al recargar configuración del scoreboard: ${e.message}")
        }
        
        return 1
    }
    
    private fun showScoreboard(ctx: CommandContext<CommandSourceStack>): Int {
        val player: ServerPlayer = ctx.source.player!!
        
        ScoreboardManager.createScoreboard(player)
        player.sendSystemMessage(
            Component.literal("📋 Scoreboard mostrado.")
                .withStyle { it.withColor(0x55FF55) }
        )
        
        return 1
    }
    
    private fun showScoreboardToPlayer(ctx: CommandContext<CommandSourceStack>): Int {
        val targetPlayer: ServerPlayer = EntityArgument.getPlayer(ctx, "player")
        val source = ctx.source
        
        ScoreboardManager.createScoreboard(targetPlayer)
        
        source.sendSystemMessage(
            Component.literal("📋 Scoreboard mostrado para ${targetPlayer.name.string}.")
                .withStyle { it.withColor(0x55FF55) }
        )
        
        targetPlayer.sendSystemMessage(
            Component.literal("📋 Tu scoreboard ha sido activado por un administrador.")
                .withStyle { it.withColor(0x55FF55) }
        )
        
        return 1
    }
    
    private fun hideScoreboard(ctx: CommandContext<CommandSourceStack>): Int {
        val player: ServerPlayer = ctx.source.player!!
        
        ScoreboardManager.removeScoreboard(player)
        player.sendSystemMessage(
            Component.literal("📋 Scoreboard ocultado.")
                .withStyle { it.withColor(0xFFAA00) }
        )
        
        return 1
    }
    
    private fun hideScoreboardFromPlayer(ctx: CommandContext<CommandSourceStack>): Int {
        val targetPlayer: ServerPlayer = EntityArgument.getPlayer(ctx, "player")
        val source = ctx.source
        
        ScoreboardManager.removeScoreboard(targetPlayer)
        
        source.sendSystemMessage(
            Component.literal("📋 Scoreboard ocultado para ${targetPlayer.name.string}.")
                .withStyle { it.withColor(0xFFAA00) }
        )
        
        targetPlayer.sendSystemMessage(
            Component.literal("📋 Tu scoreboard ha sido desactivado por un administrador.")
                .withStyle { it.withColor(0xFFAA00) }
        )
        
        return 1
    }
} 