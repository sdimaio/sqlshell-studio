# Implementation Sequence

This short note records the recommended order for turning the current architectural scaffold into a working product.

## Order

1. implement `sqlshell-core` models and contracts more fully
2. implement JDBC session opening in `sqlshell-jdbc`
3. implement dialect resolution and test queries
4. wire a first connection manager in `sqlshell-tui-jexer`
5. implement the first executable workflow:
   - create/open profile
   - connect
   - run query
   - display result
6. add history and export
7. enrich schema browser and object inspector

## Rule

Do not start with complex UI. Start with one complete vertical slice.
