package com.explorefaith.pixelkey.latin.uix.settings.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.explorefaith.pixelkey.latin.Subtypes
import com.explorefaith.pixelkey.latin.stripExtensionsIfNeeded
import com.explorefaith.pixelkey.latin.uix.settings.DropDownPicker
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.ScrollableList
import com.explorefaith.pixelkey.latin.uix.settings.SettingItem

val QwertyVariants = listOf("qwerty")

fun makeQwertyWithPrimary(primary: String): List<String> {
    return listOf(primary) + QwertyVariants.filter { it != primary }
}

fun makeQwertyWithPrimary(primary: String, secondary: String): List<String> {
    return listOf(primary, secondary) + QwertyVariants.filter { it != primary && it != secondary }
}

// Pixel Key supports exactly these languages:
// Bangla, English, Hindi, Spanish, Chinese (Pinyin), Arabic
val LocaleLayoutMap = mapOf(
    "ar"     to   listOf("arabic"),
    "bn"     to   listOf("bengali_avro", "bengali_probhat", "bengali_jatiyo"),
    "bn_BD"  to   listOf("bengali_avro", "bengali_probhat", "bengali_jatiyo"),
    "bn_IN"  to   listOf("bengali_avro", "bengali_probhat", "bengali_jatiyo"),
    "en"     to   listOf("qwerty"),
    "en_IN"  to   listOf("qwerty"),
    "en_US"  to   listOf("qwerty"),
    "en_GB"  to   listOf("qwerty"),
    "es"     to   listOf("spanish"),
    "es_US"  to   listOf("spanish"),
    "es_419" to   listOf("spanish"),
    "hi"     to   listOf("hindi", "hindi_compact"),
    "zh"     to   listOf("pinyin"),
    "zh_CN"  to   listOf("pinyin"),
    "zh_TW"  to   listOf("pinyin"),
    "zh_HK"  to   listOf("pinyin"),
)

@Preview
@Composable
fun AddLanguageScreen(navController: NavHostController = rememberNavController(), defaultLocale: String? = null) {
    val context = LocalContext.current

    val defaultLocaleVal = defaultLocale ?: context.resources.configuration.locale.stripExtensionsIfNeeded().toString()
    val defaultLayout = Subtypes.findClosestLocaleLayouts(Subtypes.getLocale(defaultLocaleVal)).firstOrNull() ?: "qwerty"

    val selectedLocale: MutableState<String> = remember { mutableStateOf(defaultLocale ?: context.resources.configuration.locale.stripExtensionsIfNeeded().toString()) }
    val selectedLayout: MutableState<String> = remember { mutableStateOf(defaultLayout) }

    val keys = remember { LocaleLayoutMap.keys.toList() }
    ScrollableList {
        ScreenTitle("Add Language", showBack = true, navController)

        SettingItem(title = "Language") {
            DropDownPicker(
                "",
                keys,
                selectedLocale.value,
                {
                    selectedLocale.value = it
                    selectedLayout.value = LocaleLayoutMap[it]!!.first()
                },
                {
                    Subtypes.getNameForLocale(it)
                },
                modifier = Modifier.width(180.dp)
            )
        }

        SettingItem(title = "Layout") {
            DropDownPicker(
                "",
                LocaleLayoutMap[selectedLocale.value] ?: listOf(),
                selectedLayout.value,
                { selectedLayout.value = it },
                { Subtypes.getLayoutName(context, it) },
                modifier = Modifier.width(180.dp)
            )
        }

        Button(onClick = {
            Subtypes.addLanguage(
                context,
                Subtypes.getLocale(selectedLocale.value),
                selectedLayout.value
            )

            navController.navigateUp()
        }, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Add")
        }
    }
}