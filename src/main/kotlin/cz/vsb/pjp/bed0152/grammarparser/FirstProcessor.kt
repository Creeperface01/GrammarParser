package cz.vsb.pjp.bed0152.grammarparser

object FirstProcessor {

    fun process(grammar: Grammar) {
        val firstMap = mutableMapOf<NonTerminal, MutableSet<Symbol>>()

        fun process() {
            grammar.nonTerminals.forEach { terminal ->
                terminal.rules.forEach { rule ->
                    for (symbol in rule.symbols) {
                        firstMap.computeIfAbsent(terminal) { mutableSetOf() }.add(symbol)

                        if (symbol is Terminal || !grammar.emptyNonTerminals.contains(symbol)) {
                            break
                        }
                    }
                }
            }
        }

        var lastSize = -1
        while (firstMap.size != lastSize) {
            lastSize = firstMap.size
            process()
        }

        val firstTerminalMap = firstMap.mapValues {
            it.value.filterIsInstance<Terminal>().toMutableSet()
        }.toMutableMap()

        fun processFirst(): Boolean {
            var changed = false

            firstMap.forEach { (nonTerminal, symbols) ->
                val firstSymbols = requireNotNull(firstTerminalMap[nonTerminal])
                val firstSize = firstSymbols.size

                symbols.forEach { symbol ->
                    when (symbol) {
                        is Terminal -> {
                            firstSymbols.add(symbol)
                        }

                        is NonTerminal -> {
                            firstSymbols.addAll(firstTerminalMap[symbol]!!)
                        }
                    }
                }

                if (firstSymbols.size != firstSize) {
                    changed = true
                }
            }

            return changed
        }

        while (processFirst()) {
        }

        firstTerminalMap.forEach { (nonTerminal, symbols) ->
            nonTerminal.first.clear()
            nonTerminal.first.addAll(symbols)
        }
    }
}