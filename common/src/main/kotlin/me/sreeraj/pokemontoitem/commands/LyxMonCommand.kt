package me.sreeraj.pokemontoitem.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import me.sreeraj.pokemontoitem.PokemonToItem
import me.sreeraj.pokemontoitem.permissions.PokemonToItemPermissions
import me.sreeraj.pokemontoitem.util.PlayerDataManager
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

class LyxMonCommand {
    
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal("lyxmon")
                .then(
                    Commands.literal("balance")
                        .executes { context -> 
                            executeBalance(context)
                        }
                        .then(
                            Commands.argument("player", EntityArgument.player())
                                .requires { source -> 
                                    PokemonToItemPermissions.checkPermission(source, PokemonToItem.permissions.LYXMON_BALANCE_OTHERS_PERMISSION)
                                }
                                .executes { context ->
                                    executeBalanceOthers(context)
                                }
                        )
                )
                .then(
                    Commands.literal("add")
                        .requires { source -> 
                            PokemonToItemPermissions.checkPermission(source, PokemonToItem.permissions.LYXMON_ADD_PERMISSION)
                        }
                        .then(
                            Commands.argument("player", EntityArgument.player())
                                .then(
                                    Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes { context ->
                                            executeAddCoins(context)
                                        }
                                )
                        )
                )
                .then(
                    Commands.literal("remove")
                        .requires { source -> 
                            PokemonToItemPermissions.checkPermission(source, PokemonToItem.permissions.LYXMON_REMOVE_PERMISSION)
                        }
                        .then(
                            Commands.argument("player", EntityArgument.player())
                                .then(
                                    Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes { context ->
                                            executeRemoveCoins(context)
                                        }
                                )
                        )
                )
        )
    }
    
    private fun executeBalance(context: CommandContext<CommandSourceStack>): Int {
        val source = context.source
        val player = source.playerOrException
        
        val coins = PlayerDataManager.getCoins(player)
        
        source.sendSuccess(
            {
                Component.literal("💰 Tu balance: ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(
                        Component.literal("$coins LyxCoins")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                    )
            },
            false
        )
        
        return 1
    }
    
    private fun executeBalanceOthers(context: CommandContext<CommandSourceStack>): Int {
        val source = context.source
        val targetPlayer = EntityArgument.getPlayer(context, "player")
        
        val coins = PlayerDataManager.getCoins(targetPlayer)
        
        source.sendSuccess(
            {
                Component.literal("💰 Balance de ")
                    .withStyle(ChatFormatting.GOLD)
                    .append(
                        Component.literal(targetPlayer.name.string)
                            .withStyle(ChatFormatting.AQUA)
                    )
                    .append(
                        Component.literal(": $coins LyxCoins")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                    )
            },
            false
        )
        
        return 1
    }
    
    private fun executeAddCoins(context: CommandContext<CommandSourceStack>): Int {
        val source = context.source
        val targetPlayer = EntityArgument.getPlayer(context, "player")
        val amount = IntegerArgumentType.getInteger(context, "amount")
        
        PlayerDataManager.addCoins(targetPlayer, amount)
        
        source.sendSuccess(
            {
                Component.literal("✅ Se agregaron ")
                    .withStyle(ChatFormatting.GREEN)
                    .append(
                        Component.literal("$amount LyxCoins")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                    )
                    .append(
                        Component.literal(" a ")
                            .withStyle(ChatFormatting.GREEN)
                    )
                    .append(
                        Component.literal(targetPlayer.name.string)
                            .withStyle(ChatFormatting.AQUA)
                    )
            },
            true
        )
        
        // Notificar al jugador objetivo
        targetPlayer.sendSystemMessage(
            Component.literal("💰 ¡Has recibido ")
                .withStyle(ChatFormatting.GOLD)
                .append(
                    Component.literal("$amount LyxCoins")
                        .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                )
                .append(
                    Component.literal("!")
                        .withStyle(ChatFormatting.GOLD)
                )
        )
        
        return 1
    }
    
    private fun executeRemoveCoins(context: CommandContext<CommandSourceStack>): Int {
        val source = context.source
        val targetPlayer = EntityArgument.getPlayer(context, "player")
        val amount = IntegerArgumentType.getInteger(context, "amount")
        
        val currentCoins = PlayerDataManager.getCoins(targetPlayer)
        
        if (currentCoins < amount) {
            source.sendFailure(
                Component.literal("❌ El jugador ")
                    .withStyle(ChatFormatting.RED)
                    .append(
                        Component.literal(targetPlayer.name.string)
                            .withStyle(ChatFormatting.AQUA)
                    )
                    .append(
                        Component.literal(" no tiene suficientes LyxCoins. Tiene: $currentCoins")
                            .withStyle(ChatFormatting.RED)
                    )
            )
            return 0
        }
        
        PlayerDataManager.removeCoins(targetPlayer, amount)
        
        source.sendSuccess(
            {
                Component.literal("✅ Se removieron ")
                    .withStyle(ChatFormatting.GREEN)
                    .append(
                        Component.literal("$amount LyxCoins")
                            .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                    )
                    .append(
                        Component.literal(" de ")
                            .withStyle(ChatFormatting.GREEN)
                    )
                    .append(
                        Component.literal(targetPlayer.name.string)
                            .withStyle(ChatFormatting.AQUA)
                    )
            },
            true
        )
        
        // Notificar al jugador objetivo
        targetPlayer.sendSystemMessage(
            Component.literal("💰 Se te removieron ")
                .withStyle(ChatFormatting.RED)
                .append(
                    Component.literal("$amount LyxCoins")
                        .withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)
                )
        )
        
        return 1
    }
} 