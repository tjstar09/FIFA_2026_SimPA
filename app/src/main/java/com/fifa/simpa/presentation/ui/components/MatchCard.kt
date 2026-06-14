package com.fifa.simpa.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fifa.simpa.domain.model.*
import com.fifa.simpa.presentation.ui.theme.*

@Composable
fun MatchCard(
    match: Match,
    onSimulate: () -> Unit = {},
    isSimulating: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isFinished = match.status == MatchStatus.FINISHED

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Match info header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Group ${match.group}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isFinished) "FT" else if (match.status == MatchStatus.LIVE) "LIVE" else "Upcoming",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isFinished) MaterialTheme.colorScheme.onSurfaceVariant
                            else if (match.status == MatchStatus.LIVE) CardRed
                            else MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Home team row
            TeamScoreRow(
                teamName = match.homeTeam.name,
                teamShortName = match.homeTeam.shortName,
                logoUrl = match.homeTeam.logoUrl,
                score = if (isFinished || match.homeScore != null) match.homeScore else null,
                isHome = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Score divider vs
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Away team row
            TeamScoreRow(
                teamName = match.awayTeam.name,
                teamShortName = match.awayTeam.shortName,
                logoUrl = match.awayTeam.logoUrl,
                score = if (isFinished || match.awayScore != null) match.awayScore else null,
                isHome = false
            )

            // Stats row for finished matches
            if (isFinished) {
                Spacer(modifier = Modifier.height(8.dp))
                MatchStatsRow(match)
            }

            // Prediction and simulate button for upcoming matches
            if (!isFinished) {
                Spacer(modifier = Modifier.height(8.dp))
                PredictionRow(match)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onSimulate,
                    enabled = !isSimulating,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isSimulating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isSimulating) "Simulating..." else "Simulate Match",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun TeamScoreRow(
    teamName: String,
    teamShortName: String,
    logoUrl: String,
    score: Int?,
    isHome: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = logoUrl,
                contentDescription = "$teamName flag",
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Team name
        Text(
            text = teamShortName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        // Score
        if (score != null) {
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (score > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End
            )
        } else {
            Text(
                text = "-",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun MatchStatsRow(match: Match) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("xG", "${"%.2f".format(match.homeXG)}")
        StatItem("Poss", "${match.homePossession}%")
        StatItem("Shots", "${match.homeShots}")
        Divider(
            modifier = Modifier
                .height(32.dp)
                .width(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        StatItem("xG", "${"%.2f".format(match.awayXG)}")
        StatItem("Poss", "${match.awayPossession}%")
        StatItem("Shots", "${match.awayShots}")
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PredictionRow(match: Match) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Placeholder for prediction probabilities
        Text(
            text = "${match.homeTeam.shortName}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "vs",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = "${match.awayTeam.shortName}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}