package me.sreeraj.pokemontoitem.util

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

object ColorUtils {
    
    /**
     * Convierte códigos de color tipo &a en componentes de Minecraft
     */
    fun colorize(text: String): MutableComponent {
        var result = Component.empty()
        var currentText = ""
        var i = 0
        
        while (i < text.length) {
            if (i < text.length - 1 && text[i] == '&') {
                // Agregar texto acumulado antes del código de color
                if (currentText.isNotEmpty()) {
                    result = result.append(Component.literal(currentText))
                    currentText = ""
                }
                
                val colorChar = text[i + 1]
                val formatting = getFormattingByChar(colorChar)
                
                if (formatting != null) {
                    // Encontrar el final del texto con este formato
                    var nextFormatIndex = i + 2
                    while (nextFormatIndex < text.length - 1) {
                        if (text[nextFormatIndex] == '&') break
                        nextFormatIndex++
                    }
                    
                    val formattedText = text.substring(i + 2, nextFormatIndex)
                    if (formattedText.isNotEmpty()) {
                        result = result.append(Component.literal(formattedText).withStyle(formatting))
                    }
                    
                    i = nextFormatIndex
                    continue
                }
            }
            
            currentText += text[i]
            i++
        }
        
        // Agregar cualquier texto restante
        if (currentText.isNotEmpty()) {
            result = result.append(Component.literal(currentText))
        }
        
        return result
    }
    
    private fun getFormattingByChar(char: Char): ChatFormatting? {
        return when (char.lowercaseChar()) {
            '0' -> ChatFormatting.BLACK
            '1' -> ChatFormatting.DARK_BLUE
            '2' -> ChatFormatting.DARK_GREEN
            '3' -> ChatFormatting.DARK_AQUA
            '4' -> ChatFormatting.DARK_RED
            '5' -> ChatFormatting.DARK_PURPLE
            '6' -> ChatFormatting.GOLD
            '7' -> ChatFormatting.GRAY
            '8' -> ChatFormatting.DARK_GRAY
            '9' -> ChatFormatting.BLUE
            'a' -> ChatFormatting.GREEN
            'b' -> ChatFormatting.AQUA
            'c' -> ChatFormatting.RED
            'd' -> ChatFormatting.LIGHT_PURPLE
            'e' -> ChatFormatting.YELLOW
            'f' -> ChatFormatting.WHITE
            'k' -> ChatFormatting.OBFUSCATED
            'l' -> ChatFormatting.BOLD
            'm' -> ChatFormatting.STRIKETHROUGH
            'n' -> ChatFormatting.UNDERLINE
            'o' -> ChatFormatting.ITALIC
            'r' -> ChatFormatting.RESET
            else -> null
        }
    }
} 