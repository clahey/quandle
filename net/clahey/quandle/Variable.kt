package net.clahey.quandle

data class Variable(val identifier: String) : Term, Lexable {
    override fun toString() = identifier
    override fun getRepresentation() = identifier

}
