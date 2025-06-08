package me.sreeraj.pokemontoitem.api

/**
 * Proveedor público de la API de LyxCoins
 * Esta clase permite a otros mods acceder fácilmente al sistema de monedas
 * 
 * Ejemplo de uso desde otro mod:
 * ```kotlin
 * // Verificar si LyxCoins está disponible
 * if (LyxCoinsProvider.isAvailable()) {
 *     val api = LyxCoinsProvider.getAPI()
 *     val coins = api.getCoins(player)
 *     api.addCoins(player, 100)
 * }
 * ```
 */
object LyxCoinsProvider {
    
    private var apiInstance: LyxCoinsAPI? = null
    
    /**
     * Inicializa la API (llamado internamente por el mod)
     */
    fun initialize() {
        apiInstance = LyxCoinsAPIImpl()
    }
    
    /**
     * Obtiene la instancia de la API
     * @return La instancia de la API o null si no está disponible
     */
    @JvmStatic
    fun getAPI(): LyxCoinsAPI? {
        return apiInstance
    }
    
    /**
     * Verifica si la API está disponible
     * @return true si la API está disponible y puede ser usada
     */
    @JvmStatic
    fun isAvailable(): Boolean {
        return apiInstance?.isAvailable() == true
    }
    
    /**
     * Obtiene la versión de la API
     * @return La versión de la API como string, o null si no está disponible
     */
    @JvmStatic
    fun getAPIVersion(): String? {
        return apiInstance?.getAPIVersion()
    }
    
    /**
     * Obtiene el ID del mod
     * @return El ID del mod que proporciona la API
     */
    @JvmStatic
    fun getModId(): String {
        return "pokemontoitem"
    }
} 