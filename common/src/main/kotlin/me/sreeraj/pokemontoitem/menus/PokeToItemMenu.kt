package me.sreeraj.pokemontoitem.menus

import com.cobblemon.mod.common.Cobblemon.storage
import me.sreeraj.pokemontoitem.util.ItemBuilder
import me.sreeraj.pokemontoitem.util.PokemonUtility
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.CustomData


class PokeToItemMenu : MenuProvider {
    private val container: Container = SimpleContainer(9 * 4)
    private var playerToView: ServerPlayer? = null

    constructor (playerToView: ServerPlayer) {
        this.playerToView = playerToView
        setupContainer()
    }

    private fun fillWithPanes() {
        for (i in 0 until container.containerSize) {
            container.setItem(i, ItemBuilder(Items.GRAY_STAINED_GLASS_PANE).setCustomName(Component.literal(" ")).build())
        }
    }

    private fun setupContainer() {
        fillWithPanes()
    }

    override fun createMenu(i: Int, inventory: Inventory?, player: Player?): AbstractContainerMenu {
        var serverPlayer = player as ServerPlayer?
        if (this.playerToView != null) {
            serverPlayer = this.playerToView
        }
        val storage = storage.getParty(serverPlayer!!)
        for (i in 0..5) {
            val pokemon = storage.get(i)
            val slotIndex = 12 + i + if (i >= 3) 6 else 0

            if (pokemon != null) {
                val item = PokemonUtility.pokemonToItem(pokemon)

                // Save Pokémon data to NBT using RegistryAccess
                val registryAccess = player?.level()!!.registryAccess()
                val pokemonNBT = pokemon.saveToNBT(registryAccess)

                // Inject both 'slot' and 'pokemon' NBT into CustomData
                val customData = CustomData.of(CompoundTag().apply {
                    putInt("slot", i)
                    put("PTI_NBT", pokemonNBT)
                })

                val finalItem = ItemBuilder(item)
                    .setCustomData(customData)
                    .build()

                container.setItem(slotIndex, finalItem)
            } else {
                val emptyItem = ItemBuilder(Items.RED_STAINED_GLASS_PANE)
                    .setCustomName(Component.literal("Vacío").withStyle { it.withColor(ChatFormatting.GRAY) })
                    .build()

                container.setItem(slotIndex, emptyItem)
            }
        }


        return object : ChestMenu(MenuType.GENERIC_9x4, i, inventory, container, 4
        ) {
            override fun clicked(slotIndex: Int, buttonNo: Int, clickType: ClickType, player: Player) {
                val clickedItem = container.getItem(slotIndex)

                // Skip if the item is a placeholder or empty
                if (clickedItem.isEmpty || clickedItem.`is`(Items.GRAY_STAINED_GLASS_PANE) || clickedItem.`is`(Items.RED_STAINED_GLASS_PANE)) {
                    return
                }

                // Read custom data from item to get the Pokémon slot index
                val customData = clickedItem.get(DataComponents.CUSTOM_DATA)
                val slot = customData?.copyTag()?.getInt("slot") ?: -1

                if (slot == -1) {
                    return
                }

                val pokemon = storage.get(slot)
                if (pokemon != null) {
                    val clonedItem = clickedItem.copy()

                    // Add to inventory or drop if full
                    if (!player.inventory.add(clonedItem)) {
                        player.drop(clonedItem, false)
                    }


                    storage.remove(pokemon)

                    // Clear the item from the container
                    container.setItem(slotIndex, ItemStack.EMPTY)
                }

                // Handle default click behavior
                super.clicked(slotIndex, buttonNo, clickType, player)
            }





            override fun quickMoveStack(player: Player, i: Int): ItemStack {
                return ItemStack.EMPTY
            }

            override fun stillValid(player: Player): Boolean {
                return true
            }
        }
    }

    override fun getDisplayName(): Component {
        return Component.literal("Equipo de ${playerToView?.name!!.string}")
    }
}