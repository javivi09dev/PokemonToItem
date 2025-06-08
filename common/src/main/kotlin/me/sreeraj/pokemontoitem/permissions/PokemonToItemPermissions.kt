package me.sreeraj.pokemontoitem.permissions

import com.cobblemon.mod.common.Cobblemon.permissionValidator
import com.cobblemon.mod.common.api.permission.PermissionLevel
import me.sreeraj.pokemontoitem.PokemonToItem
import net.minecraft.commands.CommandSourceStack

class PokemonToItemPermissions {
    val POKETOITEM_PERMISSION: PokemonToItemPermission
    val ITEMTOPOKE_PERMISSION: PokemonToItemPermission
    val LYXMON_BALANCE_OTHERS_PERMISSION: PokemonToItemPermission
    val LYXMON_ADD_PERMISSION: PokemonToItemPermission
    val LYXMON_REMOVE_PERMISSION: PokemonToItemPermission
    val LYXSCOREBOARD_RELOAD_PERMISSION: PokemonToItemPermission
    val LYXSCOREBOARD_MANAGE_OTHERS_PERMISSION: PokemonToItemPermission

    init {
        this.POKETOITEM_PERMISSION = PokemonToItemPermission("command.poketoitem", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_POKETOITEM_PERMISSION_LEVEL))
        this.ITEMTOPOKE_PERMISSION = PokemonToItemPermission("command.itemtopoke", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_ITEMTOPOKE_PERMISSION_LEVEL))
        this.LYXMON_BALANCE_OTHERS_PERMISSION = PokemonToItemPermission("command.lyxmon.balance.others", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_LYXMON_BALANCE_OTHERS_PERMISSION_LEVEL))
        this.LYXMON_ADD_PERMISSION = PokemonToItemPermission("command.lyxmon.add", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_LYXMON_ADD_PERMISSION_LEVEL))
        this.LYXMON_REMOVE_PERMISSION = PokemonToItemPermission("command.lyxmon.remove", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_LYXMON_REMOVE_PERMISSION_LEVEL))
        this.LYXSCOREBOARD_RELOAD_PERMISSION = PokemonToItemPermission("command.lyxscoreboard.reload", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_LYXSCOREBOARD_RELOAD_PERMISSION_LEVEL))
        this.LYXSCOREBOARD_MANAGE_OTHERS_PERMISSION = PokemonToItemPermission("command.lyxscoreboard.manage_others", toPermLevel(PokemonToItem.config.permissionLevels.COMMAND_LYXSCOREBOARD_MANAGE_OTHERS_PERMISSION_LEVEL))
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
