package me.sreeraj.pokemontoitem.events

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent
import com.cobblemon.mod.common.pokemon.FormData
import me.sreeraj.pokemontoitem.PokemonToItem
import me.sreeraj.pokemontoitem.util.PlayerDataManager
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

object PokemonCaptureEventHandler {
    
    fun register() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
            handlePokemonCapture(event)
        }
    }
    
    private fun handlePokemonCapture(event: PokemonCapturedEvent) {
        if (!PokemonToItem.config.coins.ENABLE_CATCH_REWARDS) {
            return
        }
        
        val player = event.player as? ServerPlayer ?: return
        val pokemon = event.pokemon
        
        var coinsToGive = PokemonToItem.config.coins.COINS_PER_POKEMON_CATCH
        var bonusReasons = mutableListOf<String>()
        
        // Bonificación por shiny
        if (pokemon.shiny) {
            coinsToGive += PokemonToItem.config.coins.BONUS_COINS_FOR_SHINY
            bonusReasons.add("✨ Shiny (+${PokemonToItem.config.coins.BONUS_COINS_FOR_SHINY})")
        }
        
        // Bonificación por legendario
        if (isLegendaryPokemon(pokemon.form)) {
            coinsToGive += PokemonToItem.config.coins.BONUS_COINS_FOR_LEGENDARY
            bonusReasons.add("🌟 Legendario (+${PokemonToItem.config.coins.BONUS_COINS_FOR_LEGENDARY})")
        }
        
        // Otorgar las monedas
        PlayerDataManager.addCoins(player, coinsToGive)
        
        // Crear mensaje de recompensa
        val baseMessage = Component.literal("💰 ¡Has capturado a ")
            .withStyle(ChatFormatting.GOLD)
            .append(" un Pokémon"
            ).withStyle(ChatFormatting.GOLD)
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
        
        // Agregar información de bonificaciones si hay
        if (bonusReasons.isNotEmpty()) {
            baseMessage.append(
                Component.literal("\n")
            ).append(
                Component.literal("Bonificaciones: ")
                    .withStyle(ChatFormatting.GREEN)
            ).append(
                Component.literal(bonusReasons.joinToString(", "))
                    .withStyle(ChatFormatting.LIGHT_PURPLE)
            )
        }
        
        player.sendSystemMessage(baseMessage)
        
        PokemonToItem.getLogger().info(
            "${player.name.string} capturó un pokemon y recibió $coinsToGive monedas" +
            if (bonusReasons.isNotEmpty()) " (${bonusReasons.joinToString(", ")})" else ""
        )
    }
    
    private fun isLegendaryPokemon(form: FormData): Boolean {
        return form.labels.contains("legendary") || 
               form.labels.contains("mythical") || 
               form.labels.contains("ultra_beast")
    }
} 