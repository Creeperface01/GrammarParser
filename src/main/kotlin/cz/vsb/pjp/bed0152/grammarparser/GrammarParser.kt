package cz.vsb.pjp.bed0152.grammarparser

object GrammarParser {

    fun parse(input: String): Grammar {
        val lines = input.lines().map { it.trimEnd(';') }

        val symbolMap = lines
            .associateBy { it.substringBefore(" :") }

        val symbols = symbolMap
            .keys
            .map {
                NonTerminal(it)
            }
            .associateBy { it.symbol }

        symbols.forEach { (symbolKey, symbol) ->
            symbol.rules.addAll(
                parseRules(
                    symbolMap[symbolKey]!!,
                    symbols
                )
            )
        }

        return Grammar(symbols.values.toList())
    }

    private fun parseRules(input: String, symbols: Map<String, Symbol>) = input
        .substringAfter(": ")
        .split(" | ")
        .map {
            it.split(' ')
        }
        .map {
            it.map { s ->
                symbols[s] ?: Terminal.from(s)
            }
        }
        .map {
            Rule(it)
        }
}