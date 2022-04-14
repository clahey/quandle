package net.clahey.quandle

data class Punctuation(val identifier: String) : Lexable {
	override fun toString() = identifier

	override fun getRepresentation() = identifier
}