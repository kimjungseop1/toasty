package com.syncrown.arpang.ui.commons

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.syncrown.arpang.R

object FontManager {
    private val fonts = mutableMapOf<String, Typeface>()
    private val fontTexts = mapOf(
        "bm_jua" to "주아체",
        "cafe24ohsquareair" to "카페24 아네모네에어",
        "cafe24oneprettynight" to "카페24 고운밤"
        // 필요에 따라 다른 폰트와 문구를 추가합니다.
    )

    fun init(context: Context) {
        fonts["bm_jua"] = ResourcesCompat.getFont(context, R.font.bm_jua)!!
        fonts["cafe24ohsquareair"] = ResourcesCompat.getFont(context, R.font.cafe24ohsquareair)!!
        fonts["cafe24oneprettynight"] = ResourcesCompat.getFont(context, R.font.cafe24oneprettynight)!!
    }

    fun getFont(fontName: String): Typeface? {
        return fonts[fontName]
    }

    fun getFontItems(): List<FontItem> {
        return fonts.mapNotNull { (fontName, typeface) ->
            val text = fontTexts[fontName]
            text?.let { FontItem(it, fontName, typeface) }
        }
    }

}

data class FontItem(
    val text: String,
    val fontName: String,
    val typeface: Typeface
)
