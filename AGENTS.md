# AGENTS.md

## Project overview
This repository is a Java/Gradle game project built with JavaFX. The application code lives under `app/src/main/java`, and tests live under `app/src/test/java`.

## Required environment
- Java 21
- Gradle wrapper is checked in at the repo root
- Prefer repo-local commands over ad hoc tooling

## Working conventions
- Keep changes scoped to the relevant package or feature.
- Match the existing project structure and naming conventions.
- Do not modify generated build artifacts, wrapper files, or IDE metadata unless the task explicitly requires it.
- Preserve JavaFX conventions and the repository's existing package layout.
- For every feature, bug fix, or behavior change, add or update tests that validate the new behavior.
- Prefer feature-level tests that exercise the real logic and cover the important code paths; do not rely only on superficial happy-path checks.
- Maintain proper feature test coverage and code-path coverage across the project, especially for economy, politics, market, and simulation logic.

## Build and validation
Run commands from the repository root:

```bash
./gradlew test --no-daemon --console=plain
```

Useful additional commands:

```bash
./gradlew build --no-daemon --console=plain
./gradlew run --no-daemon --console=plain
```

## Priority workflow for agents
1. Inspect the smallest relevant files first.
2. Implement the root-cause fix or feature change with minimal scope.
3. Add the corresponding tests before or alongside the change to validate the new feature and key code paths.
4. Validate with the closest relevant Gradle task, usually `./gradlew test`.
5. Keep output concise and avoid speculative edits.

## Notes
This is a simulation-heavy strategy game with economy, politics, and market logic. Favor consistency with existing domain classes and factories rather than introducing new abstractions unless required.
