# Sistema de Monedas LyxCoins

## Descripción
El mod PokemonToItem ahora incluye un sistema de monedas virtuales llamado **LyxCoins** que recompensa a los jugadores por capturar Pokémon.

## Características

### 🪙 Recompensas por Captura
- **Captura básica**: 10 LyxCoins por cada Pokémon capturado
- **Bonificación Shiny**: +50 LyxCoins adicionales por Pokémon shiny
- **Bonificación Legendario**: +100 LyxCoins adicionales por Pokémon legendario/mítico/ultra bestia

### ⚔️ Recompensas por Batalla
- **Victoria contra Pokémon salvaje**: 25 LyxCoins por batalla ganada
- **Victoria PvP**: 50 LyxCoins por victoria contra otros jugadores
- **Victoria contra entrenador**: 75 LyxCoins por victoria contra NPCs entrenadores

### 📊 Comandos Disponibles

#### `/lyxmon balance`
- **Permiso**: Ninguno (todos los jugadores)
- **Descripción**: Muestra tu balance actual de LyxCoins

#### `/lyxmon balance <jugador>`
- **Permiso**: `lyxmon.balance.others` (Nivel 2)
- **Descripción**: Muestra el balance de otro jugador

#### `/lyxmon add <jugador> <cantidad>`
- **Permiso**: `lyxmon.add` (Nivel 3)
- **Descripción**: Agrega LyxCoins a un jugador

#### `/lyxmon remove <jugador> <cantidad>`
- **Permiso**: `lyxmon.remove` (Nivel 3)
- **Descripción**: Remueve LyxCoins de un jugador

### ⚙️ Configuración

El archivo de configuración se encuentra en `config/pokemontoitem/config.json`:

```json
{
  "coins": {
    "coins_per_pokemon_catch": 10,
    "bonus_coins_for_shiny": 50,
    "bonus_coins_for_legendary": 100,
    "enable_catch_rewards": true,
    "coins_per_battle_victory": 25,
    "coins_per_pvp_victory": 50,
    "coins_per_trainer_victory": 75,
    "enable_battle_rewards": true
  }
}
```

### 💾 Persistencia de Datos
- Los datos se guardan automáticamente cada 5 minutos
- Los datos se guardan cuando un jugador se desconecta
- Los datos se guardan al cerrar el servidor
- Los archivos de datos se almacenan en `config/pokemontoitem/playerdata/`

### 🎮 Experiencia del Jugador

#### Recompensas por Captura
Cuando captures un Pokémon, recibirás un mensaje como:
```
💰 ¡Has capturado un Pokémon y recibido 10 LyxCoins!
```

Para Pokémon especiales:
```
💰 ¡Has capturado un Pokémon y recibido 160 LyxCoins!
Bonificaciones: ✨ Shiny (+50), 🌟 Legendario (+100)
```

#### Recompensas por Batalla
Cuando ganes una batalla, recibirás mensajes como:
```
⚔️ ¡Has ganado la batalla contra Pokémon salvaje y recibido 25 LyxCoins!
⚔️ ¡Has ganado la batalla PvP y recibido 50 LyxCoins!
⚔️ ¡Has ganado la batalla contra entrenador y recibido 75 LyxCoins!
```

### 🔧 Personalización
Puedes modificar las recompensas editando el archivo de configuración:

#### Recompensas de Captura
- `coins_per_pokemon_catch`: Monedas base por captura (predeterminado: 10)
- `bonus_coins_for_shiny`: Bonificación por Pokémon shiny (predeterminado: 50)
- `bonus_coins_for_legendary`: Bonificación por Pokémon legendario (predeterminado: 100)
- `enable_catch_rewards`: Activar/desactivar recompensas de captura (predeterminado: true)

#### Recompensas de Batalla
- `coins_per_battle_victory`: Monedas por victoria contra Pokémon salvaje (predeterminado: 25)
- `coins_per_pvp_victory`: Monedas por victoria PvP (predeterminado: 50)
- `coins_per_trainer_victory`: Monedas por victoria contra entrenador (predeterminado: 75)
- `enable_battle_rewards`: Activar/desactivar recompensas de batalla (predeterminado: true)

### 📋 Notas Técnicas
- Compatible con Fabric 1.21.1
- Requiere Cobblemon 1.6.1+
- Los datos se almacenan en formato JSON
- Sistema thread-safe para múltiples jugadores 