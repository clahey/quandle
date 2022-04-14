package net.clahey.quandle

import java.util.regex.Pattern
import java.text.ParseException

/* Parser for grammar:
 WORD: TERM | '(' WORD OPERATOR WORD ')'
 TERM: VARIABLE | GENERATOR
 GENERATOR: <glossary.generators>
 OPERATOR: <glossary.operators>
 VARIABLE: [A-Za-z][A-Za-z0-9]* (Except operators or generators)
 */

internal enum class State {
	START,
	PARSED_WORD,
	OPEN_PARENTHESIS,
	FIRST_INPUT,
}

internal data class MutableInt(var value: Int)


fun parseWord(glossary: Glossary, input: String): Word {
	val pattern = Pattern.compile(glossary.getTokenRegex())
	val matcher = pattern.matcher(input)
	var position: Int = 0
	val tokens = mutableListOf<Lexable>()

	fun checkForErrors(end: Int) {
		val errorPosition = input.substring(position, end).indexOfFirst { !it.isWhitespace() }
		if (errorPosition != -1) {
			throw ParseException(input, errorPosition + position)
		}
	}

	while (matcher.find()) {
		if (matcher.start() != position) {
			checkForErrors(matcher.start())
		}
		tokens.add(glossary.getLexable(matcher.group()))
		position = matcher.end()
	}
	checkForErrors(input.length)

	val tokenPosition = MutableInt(0)

	return parseToplevelWord(tokens, tokenPosition)
}

internal fun ignoreWhitespace(input: List<Lexable>, position: MutableInt) {
	while (position.value < input.size && input.get(position.value) is Whitespace) {
		position.value++
	}
}

internal fun pop(input: List<Lexable>, position: MutableInt): Lexable {
	ignoreWhitespace(input, position)
	if (position.value < input.size) {
		return input.get(position.value++)
	} else {
		throw newException(input, position)
	}
}

internal fun parseToplevelWord(input: List<Lexable>, position: MutableInt): Word {
	val word1 = parseWord2(input, position)
	ignoreWhitespace(input, position)
	if (position.value == input.size) {
		return word1
	}
	var operator = pop(input, position)
	if (!(operator is Operator)) {
		throw newException(input, position)
	}
	var word2 = parseWord2(input, position)
	ignoreWhitespace(input, position)
	if (position.value == input.size) {
		return Application(operator, listOf(word1, word2))
	} else {
		throw newException(input, position)
	}
}

internal fun newException(input: List<Lexable>, position: MutableInt) =
	ParseException(
		input.map { it.getRepresentation() }.joinToString(""),
		input.take(position.value).map { it.getRepresentation().length }.sum()
	)

internal fun parseWord2(input: List<Lexable>, position: MutableInt): Word {
	var next = pop(input, position)
	if (next == Punctuation("(")) {
		var word1 = parseWord2(input, position)
		var operator = pop(input, position)
		if (!(operator is Operator)) {
			throw newException(input, position)
		}
		var word2 = parseWord2(input, position)
		if (pop(input, position) != Punctuation(")")) {
			throw newException(input, position)
		}
		return Application(operator, listOf(word1, word2))
	} else if (next is Term) {
		return next
	} else {
		throw newException(input, position)
	}
}

interface Word {
    val size: Int
}
