package me.sreeraj.pokemontoitem.fabric

import me.sreeraj.pokemontoitem.PokemonToItem
import me.sreeraj.pokemontoitem.util.PlayerDataManager
import me.sreeraj.pokemontoitem.util.ScoreboardManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

class PokemonToItemFabric : ModInitializer {
    private var tickCounter = 0
    private var scoreboardTickCounter = 0
    
    override fun onInitialize() {
        PokemonToItem.getLogger().info("[PokemonToItem] Iniciado")
        PokemonToItem.initialize()
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            PokemonToItem.registerCommands(dispatcher)
        }
        
        // Auto-guardar datos cada 5 minutos (6000 ticks) y actualizar scoreboards
        ServerTickEvents.END_SERVER_TICK.register { server ->
            tickCounter++
            scoreboardTickCounter++
            
            if (tickCounter >= 6000) {
                PlayerDataManager.saveAllPlayerData()
                tickCounter = 0
            }
            
            // Actualizar scoreboards según el intervalo configurado
            if (scoreboardTickCounter >= PokemonToItem.config.scoreboard.UPDATE_INTERVAL_TICKS) {
                ScoreboardManager.updateAllScoreboards(server)
                scoreboardTickCounter = 0
            }
        }
        
        // Eventos de conexión de jugadores
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            // Mostrar scoreboard automáticamente cuando se conecta un jugador
            if (PokemonToItem.config.scoreboard.ENABLED) {
                ScoreboardManager.createScoreboard(handler.player)
            }
        }
        
        // Guardar datos cuando un jugador se desconecta
        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            PlayerDataManager.savePlayerData(handler.player.uuid)
            ScoreboardManager.removeScoreboard(handler.player)
        }
        
        ServerLifecycleEvents.SERVER_STOPPING.register { 
            ScoreboardManager.clearAll()
            PokemonToItem.onShutdown() 
        }
        ServerLifecycleEvents.SERVER_STOPPED.register { PokemonToItem.onShutdown() }
    }
}