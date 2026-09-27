# SQLShell Studio

**SQLShell Studio** is a shell-native JDBC database client with a professional text user interface.

It is designed for:

- SSH and headless environments
- servers without X11
- keyboard-first workflows
- developers and DBAs who want a richer experience than plain SQL shells

The project vision is inspired by:

- Oracle SQL Developer
- Toad
- terminal-first operational workflows

## Current Status

Architecture, delivery standards, and the first multi-module Maven scaffold are now in place.

## Module Layout

- `sqlshell-core` — domain model and service contracts
- `sqlshell-jdbc` — JDBC implementation layer
- `sqlshell-dialects` — Oracle/PostgreSQL/generic dialect layer
- `sqlshell-tui-jexer` — Jexer-based TUI layer
- `sqlshell-app` — bootstrap application

## Documentation

### Guides

See:
- `docs/guides/README.md`

### Design

See:
- `docs/design/README.md`

## Build and Bootstrap Notes

The repository is intentionally pinned to **Java 25**. Building it with older JDKs will fail by design.

Useful commands:

```bash
./build.sh
./verify.sh
./bin/test-java25-env.sh
./bin/start-dev.sh
```

The application now includes a small professional bootstrap layer that:

- detects the current runtime platform
- prints a startup diagnostics banner
- refuses to start on runtimes older than Java 25
