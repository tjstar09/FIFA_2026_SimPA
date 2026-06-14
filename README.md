# FIFA 2026 SimPA

**World Cup 2026 Simulation & Prediction App** — built natively in Kotlin with Jetpack Compose.

> A complete tournament simulation engine that models every minute of every match using Poisson goal distributions, Monte Carlo event loops, expected goals (xG) shot analysis, player fatigue curves, weather modifiers, referee strictness indices, and momentum dynamics.

---

## Features

### 🏟️ Match Simulation Engine
- **Poisson Distribution Goal Engine** — calculates exact goal probabilities per match using \( P(k) = \frac{\lambda^k e^{-\lambda}}{k!} \)
- **Minute-by-Minute Monte Carlo Loop** — simulates possession shifts, tackles, passes, shots, cards, injuries, and substitutions across 90+ minutes
- **Expected Goals (xG) Shot Simulator** — 8 shot types with distance/angle/header modifiers
- **Match-State Momentum** — trailing teams push harder; leading teams sit deeper

### ⚽ Player & Roster Dynamics
- **Fatigue Engine** — non-linear stamina decay by position (midfielders drain fastest)
- **Card Accumulation** — tournament-wide tracking with automatic suspensions
- **Substitution Logic** — 5-slot system with tactical rotation

### 🌦️ Environmental Modifiers
- **Weather** — Rain boosts long-shot success; Heat accelerates fatigue
- **Referee Strictness** — 1.0–2.0 coefficient scaling foul/card/penalty rates
- **Home Nation Advantage** — statistically backed boost for host nations

### 🏆 Tournament Logic
- **12 Groups (A–L)** × 4 teams each = 48 national teams
- **FIFA Tie-Breakers** — Points → Goal Difference → Goals Scored → Head-to-Head → Fair Play
- **Knockout Stage** — 120-minute matches, 1.5× fatigue, 6th sub slot
- **Penalty Shootouts** — increasing pressure modifier in sudden death

### 📱 Modern Android UI
- **Material 3** — dark/light adaptive theme with dynamic colors
- **Jetpack Compose** — 100% declarative Kotlin UI
- **Bottom Navigation** — Home (match simulator) / Points Table / Credits
- **Live Animations** — smooth transitions, micro-interactions on standings updates
- **Team Flags** — CDN-hosted flag assets via Coil

---

## Architecture

```
app/src/main/java/com/fifa/simpa/
├── data/
│   ├── api/           ← Retrofit interfaces (Sportmonks, API-Football, Machina)
│   ├── cache/         ← NetworkModule (OkHttp, caching)
│   ├── mock/          ← MockData (48 teams, 72 fixtures)
│   ├── model/         ← API DTO models (Kotlinx Serialization)
│   └── repository/    ← MatchRepository (data orchestration)
├── domain/
│   ├── engine/        ← PoissonGoalEngine, MonteCarloMatchEngine, TieBreakerEngine
│   ├── modifier/      ← FatigueEngine, MomentumEngine, WeatherModifier, RefereeEngine
│   └── model/         ← Team, Match, Player domain models
├── presentation/
│   ├── navigation/    ← Bottom nav setup
│   ├── ui/
│   │   ├── theme/     ← Color, Type, Theme (Material 3)
│   │   ├── screens/   ← HomeScreen, PointsTableScreen, CreditsScreen
│   │   └── components/← MatchCard, GroupStandingsTable, GroupSelector
│   └── viewmodel/     ← MainViewModel (StateFlow UDF)
├── MainActivity.kt
└── SimPAApplication.kt
```

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog (2024.1+) or later
- JDK 17
- Gradle 8.10+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/tjstar09/FIFA_2026_SimPA.git
   cd FIFA_2026_SimPA
   ```

2. **Open in Android Studio**
   - File → Open → Select the `FIFA_2026_SimPA` folder
   - Wait for Gradle sync to complete

3. **Add API keys** (optional — app works without them using mock data)
   Create `local.properties` in the project root:
   ```properties
   SPORTMONKS_API_KEY=your_sportmonks_api_key
   API_FOOTBALL_KEY=your_api_football_key
   MACHINA_API_KEY=your_machina_api_key
   ```

4. **Build & Run**
   ```bash
   # Debug build
   ./gradlew assembleDebug

   # Release build (requires keystore configuration)
   ./gradlew assembleRelease
   ```

---

## Data Sources

The app integrates with three REST APIs with robust failover to local mock data:

| API | Endpoint | Purpose |
|-----|----------|---------|
| [Sportmonks](https://www.sportmonks.com/) | `api.sportmonks.com/v3/` | Fixtures, live scores, xG predictions |
| [API-Football](https://www.api-football.com/) | `v3.football.api-sports.io/` | Team rosters, H2H records, statistics |
| [Machina Sports](https://api.machina.gg/) | `api.machina.gg/api/v1/` | Historical telemetry, predictive backtesting |

---

## Release APK

To generate a signed release APK:

```bash
# 1. Generate a keystore
keytool -genkey -v -keystore simpa-release.jks \
  -alias simpa -keyalg RSA -keysize 2048 -validity 10000

# 2. Configure signing in app/build.gradle.kts
#    (replace debug signing config with release config)

# 3. Build
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release.apk
```

---

## Credits

**Developer:** Tanmay Jain  
**GitHub:** [github.com/tjstar09](https://github.com/tjstar09)  
**LinkedIn:** [linkedin.com/in/niaj-yamnat](https://linkedin.com/in/niaj-yamnat)  
**X/Twitter:** [x.com/tjstar09](https://x.com/tjstar09)

---

## License

MIT License — see [LICENSE](LICENSE) for details.