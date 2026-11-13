package com.explorefaith.pixelkey.latin.uix.settings.pages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_TITLE
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.explorefaith.pixelkey.latin.R
import com.explorefaith.pixelkey.latin.uix.settings.EXPORT_GGUF_MODEL_REQUEST
import com.explorefaith.pixelkey.latin.uix.settings.IMPORT_GGUF_MODEL_REQUEST
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItem
import com.explorefaith.pixelkey.latin.uix.settings.NavigationItemStyle
import com.explorefaith.pixelkey.latin.uix.settings.ScreenTitle
import com.explorefaith.pixelkey.latin.uix.settings.ScrollableList
import com.explorefaith.pixelkey.latin.uix.settings.SettingsActivity
import com.explorefaith.pixelkey.latin.uix.settings.Tip
import com.explorefaith.pixelkey.latin.uix.settings.pages.modelmanager.FinetuneModelScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.modelmanager.ModelDeleteConfirmScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.modelmanager.ModelListScreen
import com.explorefaith.pixelkey.latin.uix.settings.pages.modelmanager.ModelScreenNav
import com.explorefaith.pixelkey.latin.uix.settings.pages.modelmanager.PrivateModelExportConfirmation
import com.explorefaith.pixelkey.latin.uix.settings.useDataStore
import com.explorefaith.pixelkey.latin.uix.theme.Typography
import com.explorefaith.pixelkey.latin.uix.urlDecode
import com.explorefaith.pixelkey.latin.uix.urlEncode
import com.explorefaith.pixelkey.latin.xlm.MODEL_OPTION_KEY
import com.explorefaith.pixelkey.latin.xlm.ModelInfo
import com.explorefaith.pixelkey.latin.xlm.ModelInfoLoader
import com.explorefaith.pixelkey.latin.xlm.ModelPaths
import com.explorefaith.pixelkey.latin.xlm.ModelPaths.updateModelOption
import com.explorefaith.pixelkey.latin.xlm.TrainingState
import com.explorefaith.pixelkey.latin.xlm.TrainingStateWithModel
import com.explorefaith.pixelkey.latin.xlm.TrainingWorkerStatus
import com.explorefaith.pixelkey.latin.xlm.scheduleTrainingWorkerImmediately
import com.explorefaith.pixelkey.updates.openURI
import java.io.File
import java.net.URLEncoder
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import kotlin.math.roundToInt

fun NavGraphBuilder.addModelManagerNavigation(
    navController: NavHostController
) {
    composable("models") { ModelListScreen(navController) }
    composable("finetune/{modelPath}") {
        val path = it.arguments!!.getString("modelPath")!!.urlDecode()
        FinetuneModelScreen(
            File(path), navController
        )

    }
    composable("finetune") {
        FinetuneModelScreen(file = null, navController = navController)
    }
    composable("model/{modelPath}") {
        val path = it.arguments!!.getString("modelPath")!!.urlDecode()
        ModelScreenNav(
            File(path), navController
        )
    }
    dialog("modelExport/{modelPath}") {
        PrivateModelExportConfirmation(
            File(it.arguments!!.getString("modelPath")!!.urlDecode()),
            navController
        )
    }
    dialog("modelDelete/{modelPath}") {
        val path = it.arguments!!.getString("modelPath")!!.urlDecode()
        ModelDeleteConfirmScreen(File(path), navController)
    }
}