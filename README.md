# EventFlow - Large-Scale Event Management Platform

DSA-3 project, KL University. Team: M Rama Srujana (2500031630), B Gnanitha (2500031073), N Geethika (2500090106).

## Problem Statement

Large campus events bring together thousands of registrations, overlapping activities, limited venues and a small pool of volunteers. Managed by hand, records are slow to search, schedule clashes are found too late, and venue capacity and staff are assigned by guesswork.

**Task:** design and build a desktop platform that takes events, activities, participants, venues and volunteers as input, and automatically searches, schedules, allocates and analyzes them using algorithms with proven time complexity instead of manual effort.

**Success criteria:** correct results on test cases, complexity stated and measured, every module usable from the GUI.

## DSA-3 Mapping

| Module / CO | EventFlow problem | Algorithm | Complexity | Package |
|---|---|---|---|---|
| M1 / CO1 | Find events by keyword in titles and descriptions | KMP, Rabin-Karp | O(n+m); Rabin-Karp worst O(nm) | `algorithms/m1` |
| M2 / CO2 | Shared text between two event descriptions | Suffix Array + Kasai LCP | O(n log^2 n) build, O(n) LCP | `algorithms/m2` |
| M3 / CO3 | Best activities in a time budget; fix misspelt names | Bitmask DP, Levenshtein DP | O(2^n) with n <= 20; O(mn) | `algorithms/m3` |
| M4 / CO4 | Allocate seats to venues; pair volunteers to activities | Dinic max-flow, bipartite matching | O(V^2 E); O(V*E) | `algorithms/m4` |
| M5 / CO5 | Check schedule feasibility; resolve activity conflicts | 3-SAT (DPLL), Vertex Cover 2-approximation | exponential worst case; cover <= 2*OPT | `algorithms/m5` |
| M6 / CO6 | Rank events; sample a registration stream | Randomized QuickSort, Reservoir Sampling | expected O(n log n); O(n) time, O(k) space | `algorithms/m6` |

## Project Structure

```
EventFlow/
├── src/
│   ├── main/        Main.java (entry point)
│   ├── model/       Event, Participant, Registration, Activity, Venue, Volunteer, Schedule
│   ├── service/     DataService (in-memory sample data)
│   ├── ui/          MainFrame, Dashboard, Event/Participant/Venue panels, M1-M6 panels, UIUtils
│   └── algorithms/  m1 ... m6 (one package per module)
├── test/            EventFlowTests.java (automated checks and timings)
├── run.bat          compile and launch the GUI (Windows)
├── run_tests.bat    compile and run the tests (Windows)
└── run_tests.sh     compile and run the tests (Linux/macOS)
```

## Sample Data

25 events (8 active), 20 participants, 30 registrations, 15 venues, 8 activities, 8 volunteers, all held in memory by `DataService`.

## How to Run

Requires JDK 8 or higher (tested on JDK 21).

Windows: double-click `run.bat`.

Linux/macOS:
```bash
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin main.Main
```

## Testing

```bash
./run_tests.sh        # or run_tests.bat on Windows
```

13 automated checks, all passing:
- Known answers: suffix array of "banana", LCP of "banana", longest common substring, Levenshtein "kitten"/"sitting" = 3 and "Fest"/"Fset" = 2, Dinic max-flow = 15, bipartite matching = 3
- Cross-checks: Rabin-Karp results equal KMP; Randomized QuickSort equals `Arrays.sort`
- Properties: vertex cover is valid and within 2x optimum; reservoir sample has the right size with no duplicates

## Performance (JDK 21, warm runs, best of repeated runs; varies by machine)

| Algorithm | Input | Time |
|---|---|---|
| KMP / Rabin-Karp | 1,000,000 characters | about 8 ms / 12 ms |
| Suffix array + LCP | 20,000 characters | about 63 ms |
| Levenshtein DP | 2,000 x 2,000 | about 21 ms |
| Bitmask DP | n = 15 / n = 20 | about 0.09 ms / 3 ms (grows as 2^n) |
| Randomized QuickSort | 1,000,000 values | about 226 ms (`Arrays.sort`: 162 ms) |
| Reservoir sampling | k = 50 from 1,000,000 | about 30 ms |
