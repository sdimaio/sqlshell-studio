# Sequenza di Implementazione

Questa breve nota registra l'ordine raccomandato per trasformare lo scaffold architetturale corrente in un prodotto funzionante.

## Ordine

1. completare meglio modelli e contratti di `sqlshell-core`
2. implementare l'apertura delle sessioni JDBC in `sqlshell-jdbc`
3. implementare la risoluzione dei dialetti e le test query
4. cablare un primo connection manager in `sqlshell-tui-jexer`
5. implementare il primo workflow verticale completo:
   - creare/aprire un profilo
   - connettersi
   - eseguire una query
   - mostrare il risultato
6. aggiungere history ed export
7. arricchire schema browser e object inspector

## Regola

Non partire dalla UI complessa. Partire da una singola vertical slice completa.
