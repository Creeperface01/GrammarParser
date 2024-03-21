package cz.vsb.pjp.bed0152.grammarparser

class NonTerminal(
    symbol: String
) : Symbol(symbol) {

    val rules = mutableListOf<Rule>()

    val first = mutableSetOf<Terminal>()

    val follow = mutableSetOf<Terminal>()

    var mappedToEmpty = false
        internal set

    fun containsEmptyRule() = rules.any {
        it.symbols
            .filterIsInstance<Terminal>()
            .any { s -> s.isEmpty }
    }
}