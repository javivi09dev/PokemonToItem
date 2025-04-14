package me.sreeraj.pokemontoitem.neoforge;

import me.sreeraj.pokemontoitem.PokemonToItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
@Mod(PokemonToItem.MODID)
public final class PokemonToItemForge {

    final ForgeEventHandler forgeEventHandler = new ForgeEventHandler();

    public PokemonToItemForge() {
        PokemonToItem.INSTANCE.getLogger().info("PokemonToItem NeoForge Starting...");
        PokemonToItem.INSTANCE.initialize();
        NeoForge.EVENT_BUS.register(PokemonToItemForge.class);
        forgeEventHandler.register();
    }

    @SubscribeEvent
    public static void onCommandRegistration(final RegisterCommandsEvent event) {
        PokemonToItem.INSTANCE.registerCommands(event.getDispatcher());
    }
}