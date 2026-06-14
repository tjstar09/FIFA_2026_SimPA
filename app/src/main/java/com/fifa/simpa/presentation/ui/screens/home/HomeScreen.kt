package com.fifa.simpa.presentation.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fifa.simpa.data.mock.MockData
import com.fifa.simpa.domain.model.MatchStatus
import com.fifa.simpa.presentation.ui.components.MatchCard
import com.fifa.simpa.presentation.ui.components.GroupSelector
import com.fifa.simpa.presentation.viewmodel.MainViewModel
import com.fifa.simpa.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToPointsTable: () -> Unit = {}
) {
    val homeState by viewModel.homeState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Header
        Text(
            text = "FIFA 2026 SimPA",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Group selector
        GroupSelector(
            groups = MockData.groups,
            selectedGroup = homeState.selectedGroup,
            onGroupSelected = { viewModel.selectGroup(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Simulation actions row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Simulate group matches
            OutlinedButton(
                onClick = { viewModel.simulateGroupMatches(homeState.selectedGroup) },
                enabled = !homeState.isSimulating,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Sim Group ${homeState.selectedGroup}", style = MaterialTheme.typography.labelMedium)
            }

            // Simulate all
            Button(
                onClick = { viewModel.simulateAllGroupMatches() },
                enabled = !homeState.isSimulating,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sim All Groups", style = MaterialTheme.typography.labelMedium)
            }
        }

        // Simulation progress
        if (homeState.isSimulating) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = homeState.simulationProgress,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Matches list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Group matches
            val groupMatches = homeState.allMatches
                .filter { it.group == homeState.selectedGroup }

            if (groupMatches.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matches found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Scheduled matches
                val scheduledMatches = groupMatches.filter { it.status == MatchStatus.SCHEDULED }
                if (scheduledMatches.isNotEmpty()) {
                    item {
                        SectionHeader("Upcoming Matches")
                    }
                    items(scheduledMatches, key = { it.id }) { match ->
                        MatchCard(
                            match = match,
                            onSimulate = { viewModel.simulateMatch(match.id) },
                            isSimulating = homeState.isSimulating
                        )
                    }
                }

                // Finished matches
                val finishedMatches = groupMatches.filter { it.status == MatchStatus.FINISHED }
                if (finishedMatches.isNotEmpty()) {
                    item {
                        SectionHeader("Finished Matches")
                    }
                    items(finishedMatches, key = { it.id }) { match ->
                        MatchCard(match = match)
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}