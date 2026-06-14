package com.fifa.simpa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fifa.simpa.data.repository.MatchRepository
import com.fifa.simpa.domain.engine.MonteCarloMatchEngine
import com.fifa.simpa.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val selectedGroup: String = "A",
    val allMatches: List<Match> = emptyList(),
    val simulatedEvents: List<SimulatedEvent> = emptyList(),
    val lastSimulationResult: MonteCarloMatchEngine.MatchSimulationResult? = null,
    val isSimulating: Boolean = false,
    val simulationProgress: String = "",
    val error: String? = null
)

data class PointsTableUiState(
    val isLoading: Boolean = false,
    val selectedGroup: String = "A",
    val standings: List<TeamGroupStanding> = emptyList(),
    val allStandings: Map<String, List<TeamGroupStanding>> = emptyMap(),
    val error: String? = null
)

class MainViewModel : ViewModel() {
    private val repository = MatchRepository()

    private val _homeState = MutableStateFlow(HomeUiState())
    val homeState: StateFlow<HomeUiState> = _homeState.asStateFlow()

    private val _pointsTableState = MutableStateFlow(PointsTableUiState())
    val pointsTableState: StateFlow<PointsTableUiState> = _pointsTableState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _homeState.value = _homeState.value.copy(
            allMatches = repository.getAllGroupMatches()
        )
        updatePointsTable()
    }

    fun selectGroup(group: String) {
        _homeState.value = _homeState.value.copy(selectedGroup = group)
        _pointsTableState.value = _pointsTableState.value.copy(selectedGroup = group)
        updatePointsTable()
    }

    fun selectPointsTableGroup(group: String) {
        _pointsTableState.value = _pointsTableState.value.copy(selectedGroup = group)
        updatePointsTable()
    }

    private fun updatePointsTable() {
        val group = _pointsTableState.value.selectedGroup
        val standings = repository.getGroupStandings(group)
        _pointsTableState.value = _pointsTableState.value.copy(
            standings = standings,
            allStandings = repository.getAllGroups().associateWith { repository.getGroupStandings(it) }
        )
    }

    fun simulateMatch(matchId: String) {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(
                isSimulating = true,
                simulationProgress = "Simulating match..."
            )
            try {
                val result = repository.simulateMatch(matchId)
                _homeState.value = _homeState.value.copy(
                    allMatches = repository.getAllGroupMatches(),
                    lastSimulationResult = result,
                    simulatedEvents = result.events,
                    isSimulating = false,
                    error = null
                )
                updatePointsTable()
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isSimulating = false,
                    error = "Simulation failed: ${e.message}"
                )
            }
        }
    }

    fun simulateAllGroupMatches() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(
                isSimulating = true,
                simulationProgress = "Simulating all group matches..."
            )
            try {
                val results = repository.simulateAllGroupMatches()
                _homeState.value = _homeState.value.copy(
                    allMatches = repository.getAllGroupMatches(),
                    isSimulating = false,
                    simulationProgress = "All matches simulated!",
                    error = null
                )
                updatePointsTable()
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isSimulating = false,
                    error = "Simulation failed: ${e.message}"
                )
            }
        }
    }

    fun simulateGroupMatches(group: String) {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(
                isSimulating = true,
                simulationProgress = "Simulating Group $group matches..."
            )
            try {
                val results = repository.simulatePartialGroupMatches(listOf(group))
                _homeState.value = _homeState.value.copy(
                    allMatches = repository.getAllGroupMatches(),
                    isSimulating = false,
                    simulationProgress = "Group $group matches simulated!",
                    error = null
                )
                updatePointsTable()
            } catch (e: Exception) {
                _homeState.value = _homeState.value.copy(
                    isSimulating = false,
                    error = "Simulation failed: ${e.message}"
                )
            }
        }
    }

    fun getMatchPrediction(match: Match): Triple<Double, Double, Double> {
        return repository.getMatchPrediction(match)
    }

    fun getTeamsByGroup(group: String): List<Team> = repository.getTeamsByGroup(group)

    fun getAllGroups(): List<String> = repository.getAllGroups()

    fun clearError() {
        _homeState.value = _homeState.value.copy(error = null)
        _pointsTableState.value = _pointsTableState.value.copy(error = null)
    }
}