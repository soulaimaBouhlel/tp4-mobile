package com.example.nowinandroid.recomposition

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────
// Charte graphique (Exercice 4)
// ─────────────────────────────────────────────
private val ScreenBackground = Color(0xFFF4F9F4) // vert très pâle
private val CardBackground   = Color(0xFFFFFFFF)
private val PrimaryGreen     = Color(0xFF2E7D32) // case cochée + barre remplie
private val TrackGreen       = Color(0xFFC8E6C9) // piste de la barre
private val PrimaryText      = Color(0xFF1B1B1B)
private val CheckedText      = Color(0xFF8A8A8A)
private val BadgeBackground  = Color(0xFFC8E6C9)

// ─────────────────────────────────────────────
// Modèle : objet IMMUABLE. Pour cocher, on remplace l'élément
// dans la liste (copy) → mutableStateListOf détecte le changement.
// ─────────────────────────────────────────────
data class PackingItem(val label: String, val packed: Boolean = false)

// ─────────────────────────────────────────────
// Écran : la seule source de vérité est la liste.
// ─────────────────────────────────────────────
@Composable
fun PackingScreen() {
    val items = remember {
        mutableStateListOf(
            PackingItem("Passeport", packed = true),
            PackingItem("Chargeur"),
            PackingItem("Maillot de bain"),
            PackingItem("Appareil photo"),
        )
    }

    // Calculés À PARTIR de la liste à chaque recomposition,
    // jamais stockés dans un état séparé → impossible d'être désynchronisés.
    val packedCount = items.count { it.packed }
    val total = items.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .statusBarsPadding()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = "Ma valise",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryText,
            textAlign = TextAlign.Center,
        )

        PackingHeader(packedCount = packedCount, total = total)

        Spacer(modifier = Modifier.height(24.dp))

        // PackingRow réutilisé 4 fois
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items.forEachIndexed { index, item ->
                PackingRow(
                    label = item.label,
                    packed = item.packed,
                    onToggle = { items[index] = item.copy(packed = !item.packed) },
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// En-tête : « 2 / 4 prêts » + barre de progression
// ─────────────────────────────────────────────
@Composable
fun PackingHeader(packedCount: Int, total: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$packedCount / $total prêts",
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
            )
            RecompositionBadge(
                background = BadgeBackground,
                textColor = PrimaryGreen,
                label = "En-tête",
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LinearProgressIndicator(
            progress = { if (total == 0) 0f else packedCount / total.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = PrimaryGreen,
            trackColor = TrackGreen,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
            drawStopIndicator = {},
        )
    }
}

// ─────────────────────────────────────────────
// PackingRow : sans état, réutilisé 4 fois.
// Le badge dans chaque ligne montre QUELLE ligne se recompose.
// ─────────────────────────────────────────────
@Composable
fun PackingRow(
    label: String,
    packed: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .clickable(onClick = onToggle)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = packed,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = PrimaryGreen,
                uncheckedColor = CheckedText,
                checkmarkColor = Color.White,
            ),
        )
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = if (packed) CheckedText else PrimaryText,
            textDecoration = if (packed) TextDecoration.LineThrough else TextDecoration.None,
        )
        RecompositionBadge(
            background = BadgeBackground,
            textColor = PrimaryGreen,
            label = "Rec.",
        )
    }
}

// ─────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────
@Preview(showBackground = true, backgroundColor = 0xFFF4F9F4)
@Composable
private fun PackingRowPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PackingRow(label = "Passeport", packed = true, onToggle = {})
        PackingRow(label = "Chargeur", packed = false, onToggle = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PackingScreenPreview() = PackingScreen()
