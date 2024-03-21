package cz.vsb.pjp.bed0152.grammarparser

object FollowProcessor {

    fun process(grammar: Grammar) {
        val terminalFollowMap = mutableMapOf<NonTerminal, MutableSet<Symbol>>()
        terminalFollowMap[grammar.firstNonTerminal] = mutableSetOf(Terminal.endOfLine)

        grammar.nonTerminals.forEach { nonTerminal ->
            val followSet = terminalFollowMap.computeIfAbsent(nonTerminal) { mutableSetOf() }

            grammar.nonTerminals.forEach { otherNonTerminal ->
                otherNonTerminal.rules
                    .filter { it.symbols.contains(nonTerminal) }
                    .forEach { rule ->
                        val ruleSymbols = rule.symbols

                        val index = ruleSymbols.indexOf(nonTerminal)
                        val following = ruleSymbols.drop(index + 1)

                        if (following.isEmpty()) {
                            followSet.add(otherNonTerminal)
                            return@forEach
                        }

                        for (following in following) {
                            if (following is Terminal) {
                                followSet.add(following)
                                break
                            }

                            if (following is NonTerminal) {
                                followSet.addAll(following.first.filterNot { it.isEmpty })
                            }
                        }
                    }
            }
        }

        fun process(): Boolean {
            var changed = false

            terminalFollowMap.forEach { (nonTerminal, followSet) ->
                val followSize = followSet.size

                followSet.toList().forEach { symbol ->
                    if (symbol is NonTerminal) {
                        terminalFollowMap[symbol]?.let {
                            followSet.addAll(it)
                        }
                    }
                }

                if (followSet.size != followSize) {
                    changed = true
                }
            }

            return changed
        }

        while (process()) {
        }

        val followMap = terminalFollowMap.mapValues {
            it.value.filterIsInstance<Terminal>()
        }

        followMap.forEach { (nonTerminal, followSet) ->
            nonTerminal.follow.clear()
            nonTerminal.follow.addAll(followSet)
        }
    }
}