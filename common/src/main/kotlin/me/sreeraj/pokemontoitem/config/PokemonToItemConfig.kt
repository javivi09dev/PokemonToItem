package me.sreeraj.pokemontoitem.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class PokemonToItemConfig {
    @SerializedName("permissionlevels")
    var permissionLevels: PermissionLevels = PermissionLevels()


    inner class PermissionLevels {
        @SerializedName("command.itemtopoke")
        var COMMAND_ITEMTOPOKE_PERMISSION_LEVEL: Int = 2

        @SerializedName("command.poketoitem")
        var COMMAND_POKETOITEM_PERMISSION_LEVEL: Int = 2

    }

    companion object {
        var GSON: Gson = GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()
    }
}
