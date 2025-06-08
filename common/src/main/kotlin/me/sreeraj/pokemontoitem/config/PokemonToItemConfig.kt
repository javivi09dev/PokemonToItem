package me.sreeraj.pokemontoitem.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class PokemonToItemConfig {
    @SerializedName("permissionlevels")
    var permissionLevels: PermissionLevels = PermissionLevels()

    @SerializedName("coins")
    var coins: CoinsConfig = CoinsConfig()

    @SerializedName("scoreboard")
    var scoreboard: ScoreboardConfig = ScoreboardConfig()


    inner class PermissionLevels {
        @SerializedName("command.itemtopoke")
        var COMMAND_ITEMTOPOKE_PERMISSION_LEVEL: Int = 2

        @SerializedName("command.poketoitem")
        var COMMAND_POKETOITEM_PERMISSION_LEVEL: Int = 2

        @SerializedName("command.lyxmon.balance.others")
        var COMMAND_LYXMON_BALANCE_OTHERS_PERMISSION_LEVEL: Int = 2

        @SerializedName("command.lyxmon.add")
        var COMMAND_LYXMON_ADD_PERMISSION_LEVEL: Int = 3

        @SerializedName("command.lyxmon.remove")
        var COMMAND_LYXMON_REMOVE_PERMISSION_LEVEL: Int = 3

        @SerializedName("command.lyxscoreboard.reload")
        var COMMAND_LYXSCOREBOARD_RELOAD_PERMISSION_LEVEL: Int = 3

        @SerializedName("command.lyxscoreboard.manage_others")
        var COMMAND_LYXSCOREBOARD_MANAGE_OTHERS_PERMISSION_LEVEL: Int = 2

    }

    inner class CoinsConfig {
        @SerializedName("coins_per_pokemon_catch")
        var COINS_PER_POKEMON_CATCH: Int = 10

        @SerializedName("bonus_coins_for_shiny")
        var BONUS_COINS_FOR_SHINY: Int = 50

        @SerializedName("bonus_coins_for_legendary")
        var BONUS_COINS_FOR_LEGENDARY: Int = 100

        @SerializedName("enable_catch_rewards")
        var ENABLE_CATCH_REWARDS: Boolean = true

        @SerializedName("coins_per_battle_victory")
        var COINS_PER_BATTLE_VICTORY: Int = 25

        @SerializedName("coins_per_pvp_victory")
        var COINS_PER_PVP_VICTORY: Int = 50

        @SerializedName("coins_per_trainer_victory")
        var COINS_PER_TRAINER_VICTORY: Int = 75

        @SerializedName("enable_battle_rewards")
        var ENABLE_BATTLE_REWARDS: Boolean = true
    }

    inner class ScoreboardConfig {
        @SerializedName("enabled")
        var ENABLED: Boolean = true

        @SerializedName("title")
        var TITLE: String = "&6&lLyxCraft"

        @SerializedName("update_interval_ticks")
        var UPDATE_INTERVAL_TICKS: Int = 20

        @SerializedName("lines")
        var LINES: List<String> = listOf(
            "&7&m--------------------",
            "&e✨ &fJugador: &a{player_name}",
            "&e💰 &fLyxCoins: &6{player_coins}",
            "",
            "&e🌍 &fServidor: &b{server_name}",
            "&e🕐 &fHora: &7{time}",
            "&e📍 &fPosición: &7{player_location}",
            "",
            "&e⚔️ &fPokémon: &d{pokemon_count}/6",
            "&7&m--------------------"
        )

        @SerializedName("placeholders")
        var PLACEHOLDERS: PlaceholderConfig = PlaceholderConfig()

        inner class PlaceholderConfig {
            @SerializedName("server_name")
            var SERVER_NAME: String = "LyxCraft"

            @SerializedName("time_format")
            var TIME_FORMAT: String = "HH:mm:ss"

            @SerializedName("location_format")
            var LOCATION_FORMAT: String = "{x}, {y}, {z}"
        }
    }

    companion object {
        var GSON: Gson = GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()
    }
}
