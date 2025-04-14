package me.sreeraj.pokemontoitem.neoforge;

import me.sreeraj.pokemontoitem.PokemonToItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;


public class ForgeEventHandler {
    public void register() {
        NeoForge.EVENT_BUS.register(this);
        PokemonToItem.INSTANCE.getLogger().info("Registered PokemonToItem Forge Event Handler");
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerStopped(ServerStoppedEvent event) {
        PokemonToItem.INSTANCE.getLogger().error("PokemonToItem Server Stopped");
        PokemonToItem.INSTANCE.onShutdown();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerStopping(ServerStoppingEvent event) {
        PokemonToItem.INSTANCE.getLogger().error("Server stopping, shutting down PokemonToItem");
        PokemonToItem.INSTANCE.onShutdown();
    }

}