package cz.vsb.pjp.bed0152.grammarparser

class Grammar(
    val nonTerminals: List<NonTerminal>
) {

    val emptyNonTerminals = processEmptyNonTerminals()

    val firstNonTerminal = nonTerminals.first()

    init {
        FirstProcessor.process(this)
        FollowProcessor.process(this)
    }

    private fun processEmptyNonTerminals(): Set<NonTerminal> {
        val empty = mutableSetOf<NonTerminal>()

        fun process() {
            nonTerminals.forEach {
                it.rules.forEach { rule ->
                    rule.symbols.forEach { symbol ->
                        when (symbol) {
                            is NonTerminal -> {
                                if (symbol.containsEmptyRule()) {
                                    empty.add(it)
                                }
                            }

                            is Terminal -> {
                                if (symbol.isEmpty) {
                                    empty.add(it)
                                }
                            }
                        }
                    }
                }
            }
        }

        var lastSize = -1
        while (empty.size != lastSize) {
            lastSize = empty.size
            process()
        }

        empty.forEach { it.mappedToEmpty = true }

        return empty
    }

    fun print() {
        println(
            nonTerminals.joinToString(System.lineSeparator()) { nonTerminal ->
                nonTerminal.symbol + " : " + nonTerminal.rules
                    .map { rule ->
                        rule.symbols.joinToString(" ") { it.symbol }
                    }.joinToString(" | ") + ";"
            }
        )
    }

    fun printFirstFollow() {
        nonTerminals.forEach { nonTerminal ->
            nonTerminal.rules.forEach { rule ->
                val ruleString = rule.symbols.joinToString("") { it.symbol }
                val firstString = rule.first.joinToString(" ") { it.symbol }

                println("first[${nonTerminal.symbol}:$ruleString] = $firstString")
            }
        }

        nonTerminals.forEach { nonTerminal ->
            val followString = nonTerminal.follow.joinToString(" ") { it.symbol }

            println("follow[${nonTerminal.symbol}] = $followString")
        }
    }
}