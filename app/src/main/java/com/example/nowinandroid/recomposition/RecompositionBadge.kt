package com.example.nowinandroid.recomposition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonSkippableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────
// RecompositionBadge (indice du TP)
//
// - intArrayOf(0) et NON mutableStateOf : incrémenter le compteur
//   ne doit pas lui-même déclencher une recomposition (sinon boucle infinie).
// - @NonSkippableComposable : sans paramètres, Compose considère ce
//   composable comme "skippable" et ne le ré-exécuterait jamais quand
//   le parent se recompose → le badge resterait bloqué à 1.
//   Avec l'annotation, il s'exécute à chaque recomposition du parent,
//   donc il mesure bien les recompositions de l'endroit où on le place.
// ─────────────────────────────────────────────
@NonSkippableComposable
@Composable
fun RecompositionBadge(
    modifier: Modifier = Modifier,
    background: Color = Color(0xFFE8DEF8),
    textColor: Color = Color(0xFF6750A4),
    label: String = "Recompositions",
) {
    val count = remember { intArrayOf(0) }
    count[0]++
    Text(
        text = "$label : ${count[0]}",
        modifier = modifier
            .background(background, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
    )
}
