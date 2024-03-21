package cz.vsb.pjp.bed0152.grammarparser

class Rule(
    val symbols: List<Symbol>
) {
    val first: Set<Terminal> by lazy {
        val value = mutableSetOf<Terminal>()

        for (symbol in symbols) {
            when (symbol) {
                is NonTerminal -> {
                    value.addAll(symbol.first)

                    if (!symbol.mappedToEmpty) {
                        break
                    }
                }

                is Terminal -> {
                    value.add(symbol)
                    break
                }
            }
        }

        if (value.filter { !it.isEmpty }.size > 1) {
            value.removeIf { it.isEmpty }
        }

        value.toSet()
    }

    val follow: Set<Terminal> by lazy {
        emptySet()
    }
}