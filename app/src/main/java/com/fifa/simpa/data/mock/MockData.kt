package com.fifa.simpa.data.mock

import com.fifa.simpa.domain.model.*

object MockData {

    // 2026 World Cup Groups A-L with representative teams
    val teams: List<Team> = listOf(
        // Group A
        Team("A1", "Brazil", "BRA", "BR", "A", logoUrl = "https://flagcdn.com/w320/br.png", ranking = 1, continent = Continent.SOUTH_AMERICA, stats = TeamStats(90.0, 85.0, 82.0, 87.0)),
        Team("A2", "Switzerland", "SUI", "CH", "A", logoUrl = "https://flagcdn.com/w320/ch.png", ranking = 15, continent = Continent.EUROPE, stats = TeamStats(65.0, 70.0, 75.0, 70.0)),
        Team("A3", "Cameroon", "CMR", "CM", "A", logoUrl = "https://flagcdn.com/w320/cm.png", ranking = 38, continent = Continent.AFRICA, stats = TeamStats(60.0, 55.0, 58.0, 58.0)),
        Team("A4", "New Zealand", "NZL", "NZ", "A", logoUrl = "https://flagcdn.com/w320/nz.png", ranking = 104, continent = Continent.OCEANIA, stats = TeamStats(45.0, 48.0, 42.0, 45.0)),

        // Group B
        Team("B1", "Argentina", "ARG", "AR", "B", logoUrl = "https://flagcdn.com/w320/ar.png", ranking = 2, continent = Continent.SOUTH_AMERICA, stats = TeamStats(88.0, 86.0, 82.0, 86.0)),
        Team("B2", "Netherlands", "NED", "NL", "B", logoUrl = "https://flagcdn.com/w320/nl.png", ranking = 6, continent = Continent.EUROPE, stats = TeamStats(82.0, 80.0, 78.0, 80.0)),
        Team("B3", "Senegal", "SEN", "SN", "B", logoUrl = "https://flagcdn.com/w320/sn.png", ranking = 20, continent = Continent.AFRICA, stats = TeamStats(68.0, 66.0, 62.0, 65.0)),
        Team("B4", "South Korea", "KOR", "KR", "B", logoUrl = "https://flagcdn.com/w320/kr.png", ranking = 23, continent = Continent.ASIA, stats = TeamStats(62.0, 65.0, 60.0, 62.0)),

        // Group C
        Team("C1", "France", "FRA", "FR", "C", logoUrl = "https://flagcdn.com/w320/fr.png", ranking = 3, continent = Continent.EUROPE, stats = TeamStats(88.0, 84.0, 80.0, 85.0)),
        Team("C2", "Mexico", "MEX", "MX", "C", logoUrl = "https://flagcdn.com/w320/mx.png", ranking = 14, continent = Continent.NORTH_AMERICA, stats = TeamStats(68.0, 65.0, 62.0, 65.0)),
        Team("C3", "Egypt", "EGY", "EG", "C", logoUrl = "https://flagcdn.com/w320/eg.png", ranking = 33, continent = Continent.AFRICA, stats = TeamStats(60.0, 58.0, 55.0, 58.0)),
        Team("C4", "Australia", "AUS", "AU", "C", logoUrl = "https://flagcdn.com/w320/au.png", ranking = 39, continent = Continent.ASIA, stats = TeamStats(55.0, 58.0, 56.0, 56.0)),

        // Group D
        Team("D1", "England", "ENG", "GB-ENG", "D", logoUrl = "https://flagcdn.com/w320/gb-eng.png", ranking = 4, continent = Continent.EUROPE, stats = TeamStats(86.0, 82.0, 78.0, 83.0)),
        Team("D2", "Croatia", "CRO", "HR", "D", logoUrl = "https://flagcdn.com/w320/hr.png", ranking = 10, continent = Continent.EUROPE, stats = TeamStats(72.0, 78.0, 74.0, 75.0)),
        Team("D3", "Japan", "JPN", "JP", "D", logoUrl = "https://flagcdn.com/w320/jp.png", ranking = 17, continent = Continent.ASIA, stats = TeamStats(65.0, 68.0, 62.0, 65.0)),
        Team("D4", "Canada", "CAN", "CA", "D", logoUrl = "https://flagcdn.com/w320/ca.png", ranking = 48, continent = Continent.NORTH_AMERICA, stats = TeamStats(50.0, 52.0, 48.0, 50.0)),

        // Group E
        Team("E1", "Germany", "GER", "DE", "E", logoUrl = "https://flagcdn.com/w320/de.png", ranking = 11, continent = Continent.EUROPE, isHost = true, stats = TeamStats(80.0, 76.0, 72.0, 77.0)),
        Team("E2", "Spain", "ESP", "ES", "E", logoUrl = "https://flagcdn.com/w320/es.png", ranking = 8, continent = Continent.EUROPE, stats = TeamStats(85.0, 78.0, 74.0, 80.0)),
        Team("E3", "Uruguay", "URU", "UY", "E", logoUrl = "https://flagcdn.com/w320/uy.png", ranking = 12, continent = Continent.SOUTH_AMERICA, stats = TeamStats(72.0, 70.0, 76.0, 73.0)),
        Team("E4", "Morocco", "MAR", "MA", "E", logoUrl = "https://flagcdn.com/w320/ma.png", ranking = 22, continent = Continent.AFRICA, stats = TeamStats(62.0, 65.0, 70.0, 66.0)),

        // Group F
        Team("F1", "Portugal", "POR", "PT", "F", logoUrl = "https://flagcdn.com/w320/pt.png", ranking = 7, continent = Continent.EUROPE, stats = TeamStats(82.0, 78.0, 72.0, 78.0)),
        Team("F2", "Colombia", "COL", "CO", "F", logoUrl = "https://flagcdn.com/w320/co.png", ranking = 16, continent = Continent.SOUTH_AMERICA, stats = TeamStats(70.0, 68.0, 64.0, 67.0)),
        Team("F3", "Ivory Coast", "CIV", "CI", "F", logoUrl = "https://flagcdn.com/w320/ci.png", ranking = 42, continent = Continent.AFRICA, stats = TeamStats(58.0, 55.0, 52.0, 55.0)),
        Team("F4", "Saudi Arabia", "KSA", "SA", "F", logoUrl = "https://flagcdn.com/w320/sa.png", ranking = 56, continent = Continent.ASIA, stats = TeamStats(48.0, 50.0, 46.0, 48.0)),

        // Group G
        Team("G1", "Italy", "ITA", "IT", "G", logoUrl = "https://flagcdn.com/w320/it.png", ranking = 9, continent = Continent.EUROPE, stats = TeamStats(78.0, 80.0, 82.0, 80.0)),
        Team("G2", "USA", "USA", "US", "G", logoUrl = "https://flagcdn.com/w320/us.png", ranking = 13, continent = Continent.NORTH_AMERICA, stats = TeamStats(70.0, 68.0, 65.0, 68.0)),
        Team("G3", "Nigeria", "NGA", "NG", "G", logoUrl = "https://flagcdn.com/w320/ng.png", ranking = 40, continent = Continent.AFRICA, stats = TeamStats(60.0, 55.0, 50.0, 55.0)),
        Team("G4", "Iran", "IRN", "IR", "G", logoUrl = "https://flagcdn.com/w320/ir.png", ranking = 21, continent = Continent.ASIA, stats = TeamStats(52.0, 56.0, 60.0, 56.0)),

        // Group H
        Team("H1", "Belgium", "BEL", "BE", "H", logoUrl = "https://flagcdn.com/w320/be.png", ranking = 5, continent = Continent.EUROPE, stats = TeamStats(84.0, 80.0, 72.0, 79.0)),
        Team("H2", "Poland", "POL", "PL", "H", logoUrl = "https://flagcdn.com/w320/pl.png", ranking = 28, continent = Continent.EUROPE, stats = TeamStats(68.0, 65.0, 62.0, 65.0)),
        Team("H3", "Ghana", "GHA", "GH", "H", logoUrl = "https://flagcdn.com/w320/gh.png", ranking = 60, continent = Continent.AFRICA, stats = TeamStats(55.0, 52.0, 48.0, 52.0)),
        Team("H4", "Ecuador", "ECU", "EC", "H", logoUrl = "https://flagcdn.com/w320/ec.png", ranking = 36, continent = Continent.SOUTH_AMERICA, stats = TeamStats(58.0, 55.0, 52.0, 55.0)),

        // Group I
        Team("I1", "Denmark", "DEN", "DK", "I", logoUrl = "https://flagcdn.com/w320/dk.png", ranking = 19, continent = Continent.EUROPE, stats = TeamStats(70.0, 72.0, 68.0, 70.0)),
        Team("I2", "Peru", "PER", "PE", "I", logoUrl = "https://flagcdn.com/w320/pe.png", ranking = 30, continent = Continent.SOUTH_AMERICA, stats = TeamStats(60.0, 58.0, 56.0, 58.0)),
        Team("I3", "Algeria", "ALG", "DZ", "I", logoUrl = "https://flagcdn.com/w320/dz.png", ranking = 44, continent = Continent.AFRICA, stats = TeamStats(56.0, 54.0, 50.0, 53.0)),
        Team("I4", "Iraq", "IRQ", "IQ", "I", logoUrl = "https://flagcdn.com/w320/iq.png", ranking = 68, continent = Continent.ASIA, stats = TeamStats(45.0, 48.0, 44.0, 46.0)),

        // Group J
        Team("J1", "Sweden", "SWE", "SE", "J", logoUrl = "https://flagcdn.com/w320/se.png", ranking = 26, continent = Continent.EUROPE, stats = TeamStats(65.0, 68.0, 66.0, 66.0)),
        Team("J2", "Chile", "CHI", "CL", "J", logoUrl = "https://flagcdn.com/w320/cl.png", ranking = 32, continent = Continent.SOUTH_AMERICA, stats = TeamStats(62.0, 58.0, 55.0, 58.0)),
        Team("J3", "South Africa", "RSA", "ZA", "J", logoUrl = "https://flagcdn.com/w320/za.png", ranking = 65, continent = Continent.AFRICA, stats = TeamStats(50.0, 52.0, 48.0, 50.0)),
        Team("J4", "Uzbekistan", "UZB", "UZ", "J", logoUrl = "https://flagcdn.com/w320/uz.png", ranking = 74, continent = Continent.ASIA, stats = TeamStats(42.0, 45.0, 42.0, 43.0)),

        // Group K
        Team("K1", "Serbia", "SRB", "RS", "K", logoUrl = "https://flagcdn.com/w320/rs.png", ranking = 29, continent = Continent.EUROPE, stats = TeamStats(66.0, 62.0, 58.0, 62.0)),
        Team("K2", "Costa Rica", "CRC", "CR", "K", logoUrl = "https://flagcdn.com/w320/cr.png", ranking = 52, continent = Continent.NORTH_AMERICA, stats = TeamStats(48.0, 50.0, 52.0, 50.0)),
        Team("K3", "Tunisia", "TUN", "TN", "K", logoUrl = "https://flagcdn.com/w320/tn.png", ranking = 31, continent = Continent.AFRICA, stats = TeamStats(54.0, 56.0, 58.0, 56.0)),
        Team("K4", "Jordan", "JOR", "JO", "K", logoUrl = "https://flagcdn.com/w320/jo.png", ranking = 85, continent = Continent.ASIA, stats = TeamStats(40.0, 42.0, 38.0, 40.0)),

        // Group L
        Team("L1", "Ukraine", "UKR", "UA", "L", logoUrl = "https://flagcdn.com/w320/ua.png", ranking = 24, continent = Continent.EUROPE, stats = TeamStats(64.0, 62.0, 60.0, 62.0)),
        Team("L2", "Paraguay", "PAR", "PY", "L", logoUrl = "https://flagcdn.com/w320/py.png", ranking = 50, continent = Continent.SOUTH_AMERICA, stats = TeamStats(52.0, 50.0, 48.0, 50.0)),
        Team("L3", "DR Congo", "DRC", "CD", "L", logoUrl = "https://flagcdn.com/w320/cd.png", ranking = 62, continent = Continent.AFRICA, stats = TeamStats(48.0, 46.0, 44.0, 46.0)),
        Team("L4", "Vietnam", "VIE", "VN", "L", logoUrl = "https://flagcdn.com/w320/vn.png", ranking = 95, continent = Continent.ASIA, stats = TeamStats(38.0, 40.0, 36.0, 38.0))
    )

    fun getTeamsByGroup(group: String): List<Team> = teams.filter { it.group == group }

    val groups = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L")

    fun generateMockGroupStandings(): List<TeamGroupStanding> {
        return teams.map { team ->
            TeamGroupStanding(
                teamId = team.id,
                teamName = team.name,
                shortName = team.shortName,
                group = team.group,
                played = 3,
                wins = (0..2).random(),
                draws = (0..2).random(),
                losses = (0..2).random(),
                goalsFor = (0..6).random(),
                goalsAgainst = (0..6).random()
            ).let {
                val gd = it.goalsFor - it.goalsAgainst
                it.copy(
                    goalDifference = gd,
                    points = it.wins * 3 + it.draws
                )
            }
        }
    }

    fun generateMockMatch(home: Team, away: Team): Match {
        return Match(
            id = "${home.id}-${away.id}",
            homeTeam = home,
            awayTeam = away,
            group = home.group,
            stage = MatchStage.GROUP,
            date = System.currentTimeMillis() + (24..72).random() * 3600000L,
            venue = "Merkur Spiel-Arena, Düsseldorf",
            status = MatchStatus.SCHEDULED,
            homeScore = null,
            awayScore = null,
            minute = 0,
            weather = WeatherCondition.NORMAL,
            refereeStrictness = 1.0
        )
    }

    fun generateAllGroupMatches(): List<Match> {
        val matches = mutableListOf<Match>()
        for (group in groups) {
            val groupTeams = getTeamsByGroup(group)
            // Each team plays each other (6 matches per group)
            for (i in groupTeams.indices) {
                for (j in i + 1 until groupTeams.size) {
                    matches.add(generateMockMatch(groupTeams[i], groupTeams[j]))
                }
            }
        }
        return matches
    }
}