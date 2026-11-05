# TP4 — Recomposition : réponses aux questions

## Exercice 3 — « Like Button »

**Que devient le compteur de likes après une rotation de l'écran ?**

Avec `remember { mutableStateOf(...) }`, l'état est perdu : la rotation recrée
l'Activity, la composition est détruite puis reconstruite, donc `liked` repasse à
`false` et `count` revient à 128.

**Correction :** `rememberSaveable` enregistre la valeur dans le `Bundle`
(`savedInstanceState`) et la restaure après la recréation de l'Activity.

```kotlin
var liked by rememberSaveable { mutableStateOf(false) }
var count by rememberSaveable { mutableIntStateOf(128) }
```

Remarque : le `RecompositionBadge` repart, lui, de 1 après la rotation, parce
qu'il utilise `remember` (c'est voulu : il compte les recompositions de la
composition actuelle).

## Exercice 4 — « Ma valise »

**Quand on coche « Chargeur », quels composables se recomposent ?**

- `PackingScreen` : il lit la liste `items` (`count { it.packed }` et la boucle).
- `PackingHeader` : ses paramètres `packedCount` changent (1 → 2).
  Son badge augmente.
- La ligne « Chargeur » uniquement : ses paramètres `packed` changent
  (`false` → `true`). Son badge augmente.

Les trois autres lignes (Passeport, Maillot de bain, Appareil photo) **sont sautées
(skipped)** : leurs paramètres (`label`, `packed`, `onToggle`) sont identiques à la
composition précédente, et le lambda `onToggle` est mémorisé grâce au *strong
skipping*. Leurs badges ne bougent pas.

Le compteur « 2 / 4 prêts » et la barre sont calculés à partir de la liste à chaque
recomposition, sans état séparé. Ils ne peuvent donc jamais être désynchronisés.

## Note sur `RecompositionBadge`

Le badge est annoté `@NonSkippableComposable`. Sans paramètres, Compose le
considère comme *skippable* et ne le ré-exécuterait jamais quand son parent se
recompose, donc il resterait bloqué à 1. Avec l'annotation, il s'exécute à
chaque recomposition du composable qui le contient.
