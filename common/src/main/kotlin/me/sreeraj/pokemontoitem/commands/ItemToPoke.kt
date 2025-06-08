package me.sreeraj.pokemontoitem.commands

import com.cobblemon.mod.common.Cobblemon.storage
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import me.sreeraj.pokemontoitem.PokemonToItem
import me.sreeraj.pokemontoitem.permissions.PokemonToItemPermissions
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack

class ItemToPoke {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
                Commands.literal("itemtopoke")
                        .requires { src: CommandSourceStack? -> PokemonToItemPermissions.checkPermission(src, PokemonToItem.permissions.ITEMTOPOKE_PERMISSION) }
                        .executes { ctx: CommandContext<CommandSourceStack> -> this.self(ctx) }
        )

    }

    private fun self(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.source.player ?: return 0

        val item: ItemStack = player.mainHandItem

        // Make sure the item has CustomData
        val customData = item.get(DataComponents.CUSTOM_DATA)
        if (customData != null) {
            val tag = customData.copyTag()

            // Check if it has a "pokemon" tag
            if (tag.contains("PTI_NBT")) {
                val pokemonTag = tag.getCompound("PTI_NBT")
                val registryAccess = player.level().registryAccess()

                val pokemon = Pokemon()
                pokemon.loadFromNBT(registryAccess, pokemonTag)

                // Add to party
                val party = storage.getParty(player)
                val added = party.add(pokemon)
                if(added){
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY)
                    player.displayClientMessage(Component.literal("[§2§lLyxMon§f] §a§lPokémon restaurado en tu party o PC!"), false)
                }else {
                    player.displayClientMessage(
                        Component.literal("[§2§lLyxMon§f] §a§lPokémon falló al restaurar!"),
                        false
                    )
                }
            } else {
                player.displayClientMessage(Component.literal("[§2§lLyxMon§f] §4§lEste item no contiene un Pokémon."), false)
            }
        } else {
            player.displayClientMessage(Component.literal("[§2§lLyxMon§f] §4§lNo se encontró datos de Pokémon en este item."), false)
        }

        return 1
    }


}
