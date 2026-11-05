package com.example.nowinandroid

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────
// Charte graphique (Exercice 1)
// ─────────────────────────────────────────────
private val ScreenBackground = Color(0xFFD6E3C8) // vert sauge clair
private val AvatarBackground = Color(0xFFF0F5E8) // blanc cassé
private val PersonIconColor  = Color(0xFF1E6F3A) // vert foncé
private val PrimaryText      = Color(0xFF1A1A1A) // noir doux
private val InactiveTab      = Color(0xFF5F6368) // gris

// ─────────────────────────────────────────────
// Écran complet
// ─────────────────────────────────────────────
@Composable
fun NowInAndroidScreen() {
    // 0 = Topics, 1 = People (People actif par défaut, comme la maquette)
    var selectedTab by remember { mutableIntStateOf(1) }
    val people = listOf("Daniel Maas", "Lee", "Marty Reyes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .statusBarsPadding()
    ) {
        NiaTopBar()

        // TabButton réutilisé 2 fois
        Row(modifier = Modifier.fillMaxWidth()) {
            TabButton(
                text = "Topics",
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier.weight(1f)
            )
            TabButton(
                text = "People",
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier.weight(1f)
            )
        }
        HorizontalDivider(color = InactiveTab.copy(alpha = 0.3f))

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 1) {
            // PersonRow réutilisé 3 fois
            people.forEach { name -> PersonRow(name = name) }
        } else {
            Text(
                text = "Aucun topic pour le moment",
                color = InactiveTab,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────
// TopBar : recherche · titre centré · compte
// ─────────────────────────────────────────────
@Composable
private fun NiaTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) {
            Icon(Icons.Filled.Search, contentDescription = "Rechercher", tint = PrimaryText)
        }
        Text(
            text = "Now in Android",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryText
        )
        IconButton(onClick = { }) {
            Icon(Icons.Outlined.AccountCircle, contentDescription = "Compte", tint = PrimaryText)
        }
    }
}

// ─────────────────────────────────────────────
// Composable réutilisable n°1 : TabButton
// Stateless : l'état "selected" vient du parent.
// ─────────────────────────────────────────────
@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            color = if (selected) PrimaryText else InactiveTab,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(12.dp))
        // Soulignement : visible uniquement pour l'onglet actif
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(if (selected) PrimaryText else Color.Transparent)
        )
    }
}

// ─────────────────────────────────────────────
// Composable réutilisable n°2 : PersonRow
// ─────────────────────────────────────────────
@Composable
fun PersonRow(
    name: String,
    modifier: Modifier = Modifier,
    onFollowClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Avatar : cercle blanc cassé + silhouette vert foncé
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AvatarBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                tint = PersonIconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = name,
            modifier = Modifier.weight(1f),
            color = PrimaryText,
            fontSize = 16.sp
        )

        IconButton(onClick = onFollowClick) {
            Icon(Icons.Filled.Add, contentDescription = "Suivre $name", tint = PrimaryText)
        }
    }
}

// ─────────────────────────────────────────────
// Previews (test incrémental de chaque brique)
// ─────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFFD6E3C8)
@Composable
private fun PersonRowPreview() = PersonRow(name = "Daniel Maas")

@Preview(showBackground = true, backgroundColor = 0xFFD6E3C8)
@Composable
private fun TabButtonPreview() {
    Row {
        TabButton("Topics", selected = false, onClick = {}, modifier = Modifier.weight(1f))
        TabButton("People", selected = true, onClick = {}, modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NowInAndroidScreenPreview() = NowInAndroidScreen()