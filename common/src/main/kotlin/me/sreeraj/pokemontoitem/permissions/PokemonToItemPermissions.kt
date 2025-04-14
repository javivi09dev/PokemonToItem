package me.sreeraj.pokemontoitem.permissions

import com.cobblemon.mod.common.Cobblemon.permissionValidator
import com.cobblemon.mod.common.api.permission.PermissionLevel
import me.sreeraj.pokemontoitem.PokemonToItem
import net.minecraft.commands.CommandSourceStack

class PokemonToItemPermissions {
    val POKETOITEM_PERMISSION: PokemonToItemPermission
    val ITEMTOPOKE_PERMISSION: PokemonToItemPermission

    init {
        this.POKETOITEM_PERMISSION = PokemonToItemPermission("pokemontoitem.command.poketoitem", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_POKETOITEM_PERMISSION_LEVEL))
        this.ITEMTOPOKE_PERMISSION = PokemonToItemPermission("pokemontoitem.command.itemtopoke", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_ITEMTOPOKE_PERMISSION_LEVEL))
         }

    fun toPermLevel(permLevel: Int): PermissionLevel {
        for (value in PermissionLevel.entries) {
            if (value.ordinal == permLevel) {
                return value
            }
        }
        return PermissionLevel.CHEAT_COMMANDS_AND_COMMAND_BLOCKS
    }

    companion object {
        fun checkPermission(source: CommandSourceStack?, permission: PokemonToItemPermission?): Boolean {
            return permissionValidator.hasPermission(source!!, permission!!)
        }
    }
}
