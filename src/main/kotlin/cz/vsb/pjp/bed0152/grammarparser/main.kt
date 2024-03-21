package cz.vsb.pjp.bed0152.grammarparser

fun main() {
    val input = """
        A : b C | B d;
        B : C C | b A;
        C : c C | {e};
    """.trimIndent()

    val grammar = GrammarParser.parse(input)
    grammar.print()

    println("--------------------")

    grammar.printFirstFollow()
}