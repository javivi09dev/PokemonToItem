package me.sreeraj.pokemontoitem.permissions

import com.cobblemon.mod.common.api.permission.Permission
import com.cobblemon.mod.common.api.permission.PermissionLevel
import me.sreeraj.pokemontoitem.PokemonToItem
import net.minecraft.resources.ResourceLocation

data class PokemonToItemPermission(
    private val node: String,
    override val level: PermissionLevel
) : Permission {

    override val identifier = ResourceLocation.fromNamespaceAndPath(PokemonToItem.MODID, this.node)

    override val literal = "${PokemonToItem.MODID}.${this.node}"
}
