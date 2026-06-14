package com.fifa.simpa.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.fifa.simpa.domain.model.TeamGroupStanding
import com.fifa.simpa.domain.model.MatchResult
import com.fifa.simpa.presentation.ui.theme.*

@Composable
fun GroupStandingsTable(
    standings: List<TeamGroupStanding>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("#", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("Team", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f))
                Text("Pld", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(28.dp), textAlign = TextAlign.Center)
                Text("W", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("D", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("L", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                Text("GD", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
                Text("Pts", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Team rows with animation
            standings.forEachIndexed { index, standing ->
                StandingRow(
                    position = index + 1,
                    standing = standing,
                    isTopTwo = index < 2
                )
                if (index < standings.size - 1) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StandingRow(
    position: Int,
    standing: TeamGroupStanding,
    isTopTwo: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 2.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Position number
        Text(
            text = position.toString(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isTopTwo) FontWeight.Bold else FontWeight.Normal,
            color = if (isTopTwo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center
        )

        // Team flag and name
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = standing.shortName,
                    contentDescription = "${standing.teamName} flag",
                    modifier = Modifier.size(18.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = standing.shortName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isTopTwo) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1
            )
        }

        Text(text = "${standing.played}", style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(28.dp), textAlign = TextAlign.Center)
        Text(text = "${standing.wins}", style = MaterialTheme.typography.labelSmall, color = WinColor,
            modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
        Text(text = "${standing.draws}", style = MaterialTheme.typography.labelSmall, color = DrawColor,
            modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
        Text(text = "${standing.losses}", style = MaterialTheme.typography.labelSmall, color = LossColor,
            modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
        Text(text = "${standing.goalDifference}", style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = when {
                standing.goalDifference > 0 -> WinColor
                standing.goalDifference < 0 -> LossColor
                else -> MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)

        // Points with highlight background
        Box(
            modifier = Modifier
                .width(32.dp)
                .background(
                    if (isTopTwo) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else Color.Transparent,
                    shape = RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${standing.points}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isTopTwo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun GroupSelector(
    groups: List<String>,
    selectedGroup: String,
    onGroupSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        groups.chunked(6).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { group ->
                    FilterChip(
                        selected = group == selectedGroup,
                        onClick = { onGroupSelected(group) },
                        label = {
                            Text(
                                text = "Group $group",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }
    }
}