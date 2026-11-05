package com.example.nowinandroid.recomposition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
// Charte graphique (Exercice 3)
// ─────────────────────────────────────────────
private val ScreenBackground = Color(0xFFFFF8F6) // rose pâle
private val CardBackground   = Color(0xFFFFFFFF)
private val HeartInactive    = Color(0xFF79747E) // gris
private val HeartActive      = Color(0xFFE53935) // rouge
private val PrimaryText      = Color(0xFF1C1B1F)
private val SecondaryText    = Color(0xFF79747E)
private val BadgeBackground  = Color(0xFFE8DEF8)
private val BadgeText        = Color(0xFF6750A4)
private val DividerColor     = Color(0xFFF0E4E1)

// ─────────────────────────────────────────────
// Écran : c'est LUI qui possède l'état (state hoisting)
//
// Question : que devient le compteur après une rotation ?
//   Avec `remember { mutableStateOf(...) }`, la rotation recrée
//   l'Activity → la composition est détruite → liked repasse à false
//   et count revient à 128 : le like est perdu.
//   Correction : `rememberSaveable` enregistre la valeur dans le
//   Bundle (savedInstanceState) et la restaure après la rotation.
// ─────────────────────────────────────────────
@Composable
fun LikeScreen() {
    // Avant correction :
    // var liked by remember { mutableStateOf(false) }
    // var count by remember { mutableIntStateOf(128) }
    var liked by rememberSaveable { mutableStateOf(false) }
    var count by rememberSaveable { mutableIntStateOf(128) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Mon post",
            modifier = Modifier.padding(vertical = 20.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
        )

        PostCard(
            author = "Yasmine",
            time = "Il y a 2 h",
            title = "Mon premier projet Compose !",
            liked = liked,
            count = count,
            onToggle = {
                liked = !liked
                count += if (liked) 1 else -1
            },
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Badge placé dans l'écran : il lit la même portée que liked/count,
        // donc il passe de 1 à 2 après le premier tap (comme la maquette).
        RecompositionBadge(background = BadgeBackground, textColor = BadgeText)
    }
}

// ─────────────────────────────────────────────
// Carte de publication (sans état, elle aussi)
// ─────────────────────────────────────────────
@Composable
private fun PostCard(
    author: String,
    time: String,
    title: String,
    liked: Boolean,
    count: Int,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .border(1.dp, DividerColor, RoundedCornerShape(16.dp))
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(BadgeBackground),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = BadgeText,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(author, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
                Text(time, fontSize = 11.sp, color = SecondaryText)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = DividerColor)
        Spacer(modifier = Modifier.height(20.dp))

        LikeButton(liked = liked, count = count, onToggle = onToggle)
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ─────────────────────────────────────────────
// LikeButton SANS ÉTAT : il reçoit liked, count et onToggle.
// Il ne fait qu'afficher ce qu'on lui donne et remonter l'événement.
// ─────────────────────────────────────────────
@Composable
fun LikeButton(
    liked: Boolean,
    count: Int,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onToggle)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (liked) "Je n'aime plus" else "J'aime",
            tint = if (liked) HeartActive else HeartInactive,
            modifier = Modifier.size(28.dp),
        )
        Text(
            text = count.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (liked) HeartActive else PrimaryText,
        )
    }
}

// ─────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────
@Preview(showBackground = true)
@Composable
private fun LikeButtonPreview() {
    Column {
        LikeButton(liked = false, count = 128, onToggle = {})
        LikeButton(liked = true, count = 129, onToggle = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LikeScreenPreview() = LikeScreen()
