package me.sreeraj.pokemontoitem.fabric

import me.sreeraj.pokemontoitem.PokemonToItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

class PokemonToItemFabric : ModInitializer {
    override fun onInitialize() {
        PokemonToItem.getLogger().info("[PokemonToItem] Fabric Mod init")
        PokemonToItem.initialize()
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            PokemonToItem.registerCommands(dispatcher)
        }
        ServerLifecycleEvents.SERVER_STOPPING.register { PokemonToItem.onShutdown() }
        ServerLifecycleEvents.SERVER_STOPPED.register { PokemonToItem.onShutdown() }
    }
}