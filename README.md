# DAA Assignment 4 – Dynamic Programming and String Algorithms

Edit Distance in three versions (recursive, table, two rows), Naive and KMP string search, own counters,
own input generator (fixed seed), JUnit 5 tests, an experiment and a report.

Requirements: JDK 17+, Maven 3.8+, Python 3 with matplotlib (only for the plots).

## Build and test
```bash
mvn clean test
```

## Run the experiment (writes `results/results.csv`, seed 42, takes about 1–2 minutes)
```bash
mvn -q compile exec:java
```
Without the exec plugin: `mvn -q compile && java -cp target/classes com.example.experiment.ExperimentRunner`.

## Draw the plots (writes 5 PNG files to `results/plots/`)
```bash
python3 scripts/plot.py
```

## Layout
```
src/main/java/com/example/edit/        EditDistance + Recursive / Table / TwoRow versions
src/main/java/com/example/search/      StringSearch + NaiveSearch / KmpSearch (public lps)
src/main/java/com/example/experiment/  InputGenerator (seeded) and ExperimentRunner
src/test/java/com/example/             JUnit 5 tests (reference values, random cross-checks, edge cases)
results/results.csv, results/plots/    experiment output
REPORT.md                              Experiment Design Card, plots, prediction vs result, answers
```

## AI use acknowledgement
I used an AI assistant for debugging and understanding purposes