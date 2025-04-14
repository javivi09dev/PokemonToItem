package me.sreeraj.pokemontoitem.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import me.sreeraj.pokemontoitem.PokemonToItem
import me.sreeraj.pokemontoitem.menus.PokeToItemMenu
import me.sreeraj.pokemontoitem.permissions.PokemonToItemPermissions
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerPlayer

class PokeToItem {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
                Commands.literal("poketoitem")
                        .requires { src: CommandSourceStack? -> PokemonToItemPermissions.checkPermission(src, PokemonToItem.permissions.POKETOITEM_PERMISSION) }
                        .executes { ctx: CommandContext<CommandSourceStack> -> this.self(ctx) }
        )

    }

    private fun self(ctx: CommandContext<CommandSourceStack>): Int {
        if (ctx.source.player != null) {
            val player: ServerPlayer = ctx.source.player!!
            player.openMenu(PokeToItemMenu(player))
        }
        return 1
    }

}
