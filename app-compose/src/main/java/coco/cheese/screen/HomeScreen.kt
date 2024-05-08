package coco.cheese.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coco.cheese.core.Env
import coco.cheese.core.R
import coco.cheese.core.aidl.ServiceManager
import coco.cheese.core.api.OsApi
import coco.cheese.core.engine.javet.node
import coco.cheese.core.run.debug.PCDebug
import coco.cheese.core.ui.textList
import coco.cheese.core.utils.HttpUtils
import coco.cheese.core.utils.WORKING_DIRECTORY
import coco.cheese.core.utils.ZipUtils


//class ProfileViewModel : ViewModel() {
//    val textList = mutableStateListOf<String>()
//}
//val textList = ProfileViewModel().textList
//


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("一睹人间盛世颜")
                },

                )
        },
    ) {
        Column(modifier = Modifier.padding(top = 60.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                var text by remember {  mutableStateOf("") }
                val savedText = getSavedText()
                if (savedText != null) {
                    text = savedText
                }
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        text = newText
                        saveText(newText)},
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                )
                Button(onClick = {
                    Env.get().runTime.ip="http://$text"
                    val s = ServiceManager()
                    s.Bind(true)
                }, modifier = Modifier.padding(16.dp)) {
                    Text("连接")
                }
//                this@Column.AnimatedFloatingActionButton(
//                    onClick = {
//
//                    }
//                )
            }
        }


    }
}

fun saveText(text: String) {
    val sharedPref = Env.get().context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString("ip", text)
    editor.apply()
}

fun getSavedText(): String? {
    val sharedPref = Env.get().context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    return sharedPref.getString("ip", null)
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ColumnScope.AnimatedFloatingActionButton(
    onClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }
    val favorite = "连接"
    FloatingActionButton(
        onClick = {
            onClick()
            expanded = !expanded
        },
        modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
        Row(Modifier.padding(start = 12.dp, end = 12.dp)) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = favorite,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            AnimatedVisibility(
                expanded,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(modifier = Modifier.padding(start = 12.dp), text = favorite)
            }
        }
    }
    Spacer(Modifier.requiredHeight(20.dp))
}




