package net.clahey.quandle

fun parseLaw(glossary: Glossary, input: String): EquationalLaw {
    val (leftInput, rightInput) = input.split("=", limit = 2)
    val leftWord = parseWord(glossary, leftInput)
    val rightWord = parseWord(glossary, rightInput)
    if (leftWord.size < rightWord.size) {
        return EquationalLaw(rightWord, leftWord)
    } else {
        return EquationalLaw(leftWord, rightWord)
    }
}

internal fun applyVariableMap(word: Word, variableMap: Map<Variable, Word>, requireMatches: Boolean): Word? {
    if (word is Variable) {
        if (requireMatches) {
            return variableMap.get(word)
        } else {
            return variableMap.get(word) ?: word
        }
    } else if (word is Application) {
        val inputs = word.inputs.map { applyVariableMap(it, variableMap, requireMatches) }
        if (requireMatches && inputs.any { it == null }) {
            return null
        } else {
            return Application(word.op, inputs as List<Word>)
        }
    } else {
        return word
    }
}

class EquationalLaw(val larger: Word, val smaller: Word) {
    fun matchWordLarger(word: Word): Map<Variable, Word>? {
        val variableMap = mutableMapOf<Variable, Word>()
        if (matchWordHelper(larger, word, variableMap)) {
            return variableMap.toMap()
        } else {
            return null
        }
    }

    internal fun matchWordHelper(lawWord: Word, word: Word, variableMap: MutableMap<Variable, Word>): Boolean {
        if (lawWord is Variable) {
            if (variableMap.containsKey(lawWord)) {
                return word == variableMap.get(lawWord)
            } else {
                variableMap.put(lawWord, word)
                return true
            }
        } else if (lawWord is Application && word is Application) {
            if (lawWord.op != word.op || lawWord.inputs.size != word.inputs.size) {
                return false
            }
            for (i in 0..lawWord.inputs.size - 1) {
                if (!matchWordHelper(lawWord.inputs.get(i), word.inputs.get(i), variableMap)) {
                    return false
                }
            }
            return true
        } else if (lawWord == word) {
            return true
        } else {
            return false
        }
    }

    fun reduce(word: Word): Word? {
        val variableMap = matchWordLarger(word)
        if (variableMap == null) {
            if (word is Application) {
                val inputs = word.inputs.map { reduce(it) ?: it }
                if (inputs != word.inputs) {
                    return Application(word.op, inputs)
                }
            }
            return null
        } else {
            return applyVariableMap(smaller, variableMap, true)
        }
    }
}
