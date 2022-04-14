package net.clahey.quandle

class Whitespace(val identifier: String) : Lexable {
	override fun toString() = identifier

	override fun getRepresentation() = identifier
}