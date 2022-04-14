package net.clahey.quandle

data class Generator(val identifier: String) : Term, Lexable {
    override fun getRepresentation(): String = identifier
    override fun toString(): String = identifier
}
