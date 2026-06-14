package com.fifa.simpa.presentation.ui.screens.pointstable

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
import com.fifa.simpa.presentation.ui.components.GroupSelector
import com.fifa.simpa.presentation.ui.components.GroupStandingsTable
import com.fifa.simpa.presentation.viewmodel.MainViewModel
import com.fifa.simpa.data.mock.MockData

@Composable
fun PointsTableScreen(
    viewModel: MainViewModel
) {
    val pointsState by viewModel.pointsTableState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Header
        Text(
            text = "Group Standings",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Subtitle
        Text(
            text = "Group ${pointsState.selectedGroup}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Group selector
        GroupSelector(
            groups = MockData.groups,
            selectedGroup = pointsState.selectedGroup,
            onGroupSelected = { viewModel.selectPointsTableGroup(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Standings table
        GroupStandingsTable(
            standings = pointsState.standings
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(
                    color = MaterialTheme.colorScheme.primary,
                    label = "Top 2 = Knockouts"
                )
            }
        }
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .padding(end = 4.dp)
        ) {
            Surface(
                modifier = Modifier.size(10.dp),
                shape = RoundedCornerShape(2.dp),
                color = color
            ) {}
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}