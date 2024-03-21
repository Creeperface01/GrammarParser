package cz.vsb.pjp.bed0152.grammarparser

class Terminal private constructor(
    symbol: String
) : Symbol(symbol) {

    val isEmpty: Boolean
        get() = this === epsilon

    val isEndOfLine: Boolean
        get() = this === endOfLine

    companion object {

        private val terminals = mutableMapOf<String, Terminal>()

        val endOfLine = from(Constants.END_OF_LINE)

        val epsilon = from(Constants.EPSILON)

        fun from(symbol: String) = terminals.computeIfAbsent(symbol) {
            Terminal(it)
        }
    }
}