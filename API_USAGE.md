# API de LyxCoins - Guía de Uso para Otros Mods

## Descripción

La API de LyxCoins permite a otros mods interactuar con el sistema de monedas virtuales del mod PokemonToItem. Puedes consultar balances, añadir/remover monedas, y más.

## Dependencia en build.gradle

Para usar la API en tu mod, añade PokemonToItem como dependencia en tu `build.gradle`:

```gradle
dependencies {
    // Para desarrollo - incluye el JAR del mod
    compileOnly files('libs/pokemontoitem-1.0.0.jar')
    
    // O si está publicado en un repositorio Maven
    compileOnly 'me.sreeraj:pokemontoitem:1.0.0'
    
    // El mod debe estar presente en runtime
    runtimeOnly 'me.sreeraj:pokemontoitem:1.0.0'
}
```

## Uso Básico en Java

```java
import me.sreeraj.pokemontoitem.api.LyxCoinsProvider;
import me.sreeraj.pokemontoitem.api.LyxCoinsAPI;
import net.minecraft.server.level.ServerPlayer;

public class MiMod {
    
    public void ejemploDeUso(ServerPlayer player) {
        // Verificar si LyxCoins está disponible
        if (LyxCoinsProvider.isAvailable()) {
            LyxCoinsAPI api = LyxCoinsProvider.getAPI();
            
            // Obtener el balance del jugador
            long coins = api.getCoins(player);
            System.out.println("El jugador tiene: " + coins + " LyxCoins");
            
            // Añadir monedas
            if (api.addCoins(player, 100L)) {
                System.out.println("Se añadieron 100 LyxCoins");
            }
            
            // Verificar si tiene suficientes monedas para comprar algo
            if (api.hasCoins(player, 50L)) {
                // El jugador puede comprar algo que cuesta 50 monedas
                api.removeCoins(player, 50L);
                System.out.println("Compra realizada!");
            }
            
            // Establecer una cantidad específica
            api.setCoins(player, 1000L);
        } else {
            System.out.println("LyxCoins no está disponible");
        }
    }
}
```

## Uso Básico en Kotlin

```kotlin
import me.sreeraj.pokemontoitem.api.LyxCoinsProvider
import net.minecraft.server.level.ServerPlayer

class MiMod {
    
    fun ejemploDeUso(player: ServerPlayer) {
        // Verificar si LyxCoins está disponible
        if (LyxCoinsProvider.isAvailable()) {
            val api = LyxCoinsProvider.getAPI()!!
            
            // Obtener el balance del jugador
            val coins = api.getCoins(player)
            println("El jugador tiene: $coins LyxCoins")
            
            // Añadir monedas
            if (api.addCoins(player, 100L)) {
                println("Se añadieron 100 LyxCoins")
            }
            
            // Verificar si tiene suficientes monedas para comprar algo
            if (api.hasCoins(player, 50L)) {
                // El jugador puede comprar algo que cuesta 50 monedas
                api.removeCoins(player, 50L)
                println("Compra realizada!")
            }
            
            // Establecer una cantidad específica
            api.setCoins(player, 1000L)
        } else {
            println("LyxCoins no está disponible")
        }
    }
}
```

## Métodos Disponibles en la API

### Consulta de Monedas

```java
// Por jugador
long getCoins(ServerPlayer player)

// Por UUID
long getCoins(UUID playerUUID)

// Verificar si tiene suficientes monedas
boolean hasCoins(ServerPlayer player, long amount)
boolean hasCoins(UUID playerUUID, long amount)
```

### Modificación de Monedas

```java
// Añadir monedas
boolean addCoins(ServerPlayer player, long amount)
boolean addCoins(UUID playerUUID, long amount)

// Remover monedas (falla si no tiene suficientes)
boolean removeCoins(ServerPlayer player, long amount)
boolean removeCoins(UUID playerUUID, long amount)

// Establecer cantidad exacta
boolean setCoins(ServerPlayer player, long amount)
boolean setCoins(UUID playerUUID, long amount)
```

### Información del Sistema

```java
// Verificar disponibilidad
boolean isAvailable()

// Obtener versión de la API
String getAPIVersion()

// Obtener ID del mod
String getModId() // Retorna "pokemontoitem"
```

## Ejemplo: Sistema de Tienda

```java
public class TiendaEjemplo {
    
    public boolean comprarItem(ServerPlayer player, long precio) {
        if (!LyxCoinsProvider.isAvailable()) {
            player.sendSystemMessage(Component.literal("Error: LyxCoins no está disponible"));
            return false;
        }
        
        LyxCoinsAPI api = LyxCoinsProvider.getAPI();
        
        // Verificar si el jugador tiene suficientes monedas
        if (!api.hasCoins(player, precio)) {
            long coins = api.getCoins(player);
            player.sendSystemMessage(Component.literal(
                "No tienes suficientes LyxCoins. Necesitas: " + precio + 
                ", tienes: " + coins
            ));
            return false;
        }
        
        // Realizar la compra
        if (api.removeCoins(player, precio)) {
            // Dar el item al jugador aquí
            player.sendSystemMessage(Component.literal(
                "¡Compra realizada! Se descontaron " + precio + " LyxCoins"
            ));
            return true;
        }
        
        return false;
    }
    
    public void darRecompensa(ServerPlayer player, long recompensa) {
        if (LyxCoinsProvider.isAvailable()) {
            LyxCoinsAPI api = LyxCoinsProvider.getAPI();
            api.addCoins(player, recompensa);
            player.sendSystemMessage(Component.literal(
                "¡Has recibido " + recompensa + " LyxCoins!"
            ));
        }
    }
}
```

## Ejemplo: Quest/Misiones

```kotlin
class QuestManager {
    
    fun completarMision(player: ServerPlayer, coinsReward: Long) {
        if (LyxCoinsProvider.isAvailable()) {
            val api = LyxCoinsProvider.getAPI()!!
            
            // Dar recompensa
            api.addCoins(player, coinsReward)
            
            // Notificar al jugador
            player.sendSystemMessage(
                Component.literal("¡Misión completada! +$coinsReward LyxCoins")
                    .withStyle(ChatFormatting.GREEN)
            )
            
            // Log para el servidor
            println("${player.name.string} completó una misión y recibió $coinsReward LyxCoins")
        }
    }
    
    fun verificarPrerrequisito(player: ServerPlayer, coinsRequeridas: Long): Boolean {
        if (!LyxCoinsProvider.isAvailable()) {
            return false
        }
        
        val api = LyxCoinsProvider.getAPI()!!
        return api.hasCoins(player, coinsRequeridas)
    }
}
```

## Integración con mcmod.info

Si quieres que tu mod indique que depende de PokemonToItem, añade esto a tu `mcmod.info`:

```json
{
  "modid": "tu_mod_id",
  "name": "Tu Mod Name",
  "dependencies": ["pokemontoitem"],
  "useDependencyInformation": true
}
```

## Manejo de Errores

Siempre verifica que la API esté disponible antes de usarla:

```java
public void usoSeguro(ServerPlayer player) {
    // Verificar que el mod esté cargado
    if (!LyxCoinsProvider.isAvailable()) {
        System.out.println("LyxCoins no está disponible, saltando operación");
        return;
    }
    
    LyxCoinsAPI api = LyxCoinsProvider.getAPI();
    if (api == null) {
        System.out.println("No se pudo obtener la API de LyxCoins");
        return;
    }
    
    // Usar la API de forma segura
    try {
        long coins = api.getCoins(player);
        // ... resto del código
    } catch (Exception e) {
        System.out.println("Error al usar la API de LyxCoins: " + e.getMessage());
    }
}
```

## Información de Versiones

- **API Versión**: 1.0.0
- **Mod ID**: pokemontoitem
- **Minecraft**: 1.21.1
- **Fabric**: Compatible
- **NeoForge**: Compatible

## Soporte

Si encuentras problemas al usar la API:

1. Verifica que PokemonToItem esté instalado en el servidor
2. Asegúrate de que esté inicializado correctamente
3. Comprueba los logs del servidor para errores relacionados

La API es thread-safe y se puede usar desde cualquier thread del servidor de forma segura. 