package net.clahey.quandle

data class Operator(val identifier: String) : Lexable {
	override fun getRepresentation() = identifier
	
	override fun toString() = identifier
}