package net.clahey.quandle

import java.util.regex.Pattern

class Glossary(operators: Set<Operator> = setOf(), generators: Set<Generator> = setOf(), base: Glossary? = null) {
	val operators: Set<Operator>
	val generators: Set<Generator>
	val lexableMap: Map<String, Lexable>

	init {
		if (base != null) {
			this.operators = operators.union(base.operators)
			this.generators = generators.union(base.generators)
		} else {
			this.operators = operators
			this.generators = generators
		}
		@OptIn(kotlin.ExperimentalStdlibApi::class)
		lexableMap = buildMap {
			this@Glossary.operators.forEach {
				put(it.getRepresentation(), it)
			}
			this@Glossary.generators.forEach {
				put(it.getRepresentation(), it)
			}
			put("(", Punctuation("("))
			put(")", Punctuation(")"))
		}
	}

	fun getTokenRegex(): String {
		return ((operators.union(generators).map(Lexable::getRepresentation).map(Pattern::quote)
			.asSequence() + sequenceOf("[a-zA-Z][a-zA-Z0-9]*", "\\(", "\\)", "\\s+")).joinToString("|"))
	}

	internal fun isWhitespace(str: String) = str.toList().all { it.isWhitespace() }

	fun getLexable(representation: String): Lexable {
		return lexableMap.get(representation)
			?: if (isWhitespace(representation)) Whitespace(representation) else Variable(representation)
	}
}