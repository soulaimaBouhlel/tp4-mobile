package com.example.nowinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nowinandroid.profile.ProfileScreen
import com.example.nowinandroid.recomposition.LikeScreen
import com.example.nowinandroid.recomposition.PackingScreen
import com.example.nowinandroid.ui.theme.NowInAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NowInAndroidTheme {
                Tp4App()
            }
        }
    }
}

// Les écrans du TP4, accessibles depuis un petit menu.
private enum class Tp4Screen(val title: String) {
    NowInAndroid("Exercice 1 : Now in Android"),
    Profile("Exercice 2 : Profil"),
    Like("Exercice 3 : Like Button"),
    Packing("Exercice 4 : Ma valise"),
}

@Composable
private fun Tp4App() {
    // rememberSaveable : on reste sur le même écran après une rotation.
    var current by rememberSaveable { mutableStateOf<Tp4Screen?>(null) }

    // Bouton retour du téléphone → retour au menu.
    BackHandler(enabled = current != null) { current = null }

    when (current) {
        null -> Tp4Menu(onOpen = { current = it })
        Tp4Screen.NowInAndroid -> NowInAndroidScreen()
        Tp4Screen.Profile -> ProfileScreen()
        Tp4Screen.Like -> LikeScreen()
        Tp4Screen.Packing -> PackingScreen()
    }
}

@Composable
private fun Tp4Menu(onOpen: (Tp4Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("TP4 — Jetpack Compose", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Tp4Screen.entries.forEach { screen ->
            Button(onClick = { onOpen(screen) }, modifier = Modifier.fillMaxWidth()) {
                Text(screen.title)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Tp4MenuPreview() {
    NowInAndroidTheme {
        Tp4Menu(onOpen = {})
    }
}
