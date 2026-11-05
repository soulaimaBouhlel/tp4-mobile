package com.example.nowinandroid.profile


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────
// Charte graphique (Exercice 2)
// ─────────────────────────────────────────────
private val Background          = Color(0xFFFAFAFA)
private val AvatarBackground    = Color(0xFFE8DEF8) // cercle pastel
private val Primary             = Color(0xFF6750A4) // violet Material 3
private val OnPrimary           = Color(0xFFFFFFFF)
private val SecondaryButtonBg   = Color(0xFFFFFFFF)
private val PrimaryText         = Color(0xFF1C1B1F)
private val SecondaryText       = Color(0xFF49454F)
private val DividerColor        = Color(0xFFE7E0EC)
private val StatNumber          = Color(0xFF6750A4)

// ─────────────────────────────────────────────
// Modèle de données pour les stats
// ─────────────────────────────────────────────
data class Stat(val value: String, val label: String)

// ─────────────────────────────────────────────
// Écran complet
// ─────────────────────────────────────────────
@Composable
fun ProfileScreen() {
    val stats = listOf(
        Stat("128", "Posts"),
        Stat("1.2k", "Followers"),
        Stat("342", "Following")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileTopBar(onBack = { }, onMore = { })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileAvatar()

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sarah Mitchell",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText
            )
            Text(
                text = "@sarah_dev · Paris",
                fontSize = 14.sp,
                color = SecondaryText
            )

            Spacer(modifier = Modifier.height(24.dp))
            // ② Composable générique
            StatsRow(
                items = stats,
                value = { it.value },
                label = { it.label }
            )

            Spacer(modifier = Modifier.height(24.dp))
            // ③ Composable avec état interne
            FollowButton(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(12.dp))
            MessageButton(onClick = { }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(32.dp))
            // ① Composable avec slot
            ProfileSection(title = "À propos") {
                InfoRow(icon = "🎯", text = "Développeuse Android passionnée")
                InfoRow(icon = "📍", text = "Paris, France")
                InfoRow(icon = "🔗", text = "sarah.dev")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────
// TopBar simple : retour · titre · menu
// ─────────────────────────────────────────────
@Composable
private fun ProfileTopBar(onBack: () -> Unit, onMore: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = PrimaryText)
            }
            Text(
                text = "Profil",
                modifier = Modifier.weight(1f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryText
            )
            IconButton(onClick = onMore) {
                Icon(Icons.Filled.MoreVert, contentDescription = "Plus d'options", tint = PrimaryText)
            }
        }
        HorizontalDivider(color = DividerColor)
    }
}

// ─────────────────────────────────────────────
// Avatar 96dp
// ─────────────────────────────────────────────
@Composable
private fun ProfileAvatar() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(AvatarBackground),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Avatar",
            tint = Primary,
            modifier = Modifier.size(48.dp)
        )
    }
}

// ─────────────────────────────────────────────
// ① Composable avec SLOT : content: @Composable () -> Unit
// La section ne sait pas ce qu'elle contient : le parent décide.
// ─────────────────────────────────────────────
@Composable
fun ProfileSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // ── À propos ─────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.width(24.dp), color = DividerColor)
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = SecondaryText
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            content() // ← le slot
        }
    }
}

// ─────────────────────────────────────────────
// InfoRow : réutilisé 3 fois dans le slot
// ─────────────────────────────────────────────
@Composable
fun InfoRow(icon: String, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = PrimaryText)
    }
}

// ─────────────────────────────────────────────
// ② Composable GÉNÉRIQUE pour les stats
// <T> : accepte n'importe quel type, le parent fournit
// comment extraire la valeur et le libellé.
// ─────────────────────────────────────────────
@Composable
fun <T> StatsRow(
    items: List<T>,
    value: (T) -> String,
    label: (T) -> String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // pour que les VerticalDivider prennent la hauteur du Row
            .border(1.dp, DividerColor, RoundedCornerShape(12.dp))
    ) {
        items.forEachIndexed { index, item ->
            if (index > 0) VerticalDivider(color = DividerColor)
            StatItem(
                value = value(item),
                label = label(item),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = StatNumber)
        Text(text = label, fontSize = 12.sp, color = SecondaryText)
    }
}

// ─────────────────────────────────────────────
// ③ Composable avec ÉTAT INTERNE (remember)
// Le bouton gère lui-même son état "suivi / non suivi".
// ─────────────────────────────────────────────
@Composable
fun FollowButton(modifier: Modifier = Modifier) {
    var isFollowing by remember { mutableStateOf(false) }

    Button(
        onClick = { isFollowing = !isFollowing },
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary,
            contentColor = OnPrimary
        )
    ) {
        if (isFollowing) {
            Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = if (isFollowing) "Suivi(e)" else "Suivre (Follow)")
    }
}

// ─────────────────────────────────────────────
// Bouton secondaire : fond blanc, bordure violette
// ─────────────────────────────────────────────
@Composable
fun MessageButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        border = BorderStroke(1.dp, Primary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SecondaryButtonBg,
            contentColor = Primary
        )
    ) {
        Text(text = "Message")
    }
}

// ─────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
private fun StatsRowPreview() = StatsRow(
    items = listOf(Stat("128", "Posts"), Stat("1.2k", "Followers"), Stat("342", "Following")),
    value = { it.value },
    label = { it.label }
)

@Preview(showBackground = true)
@Composable
private fun FollowButtonPreview() = FollowButton()

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() = ProfileScreen()