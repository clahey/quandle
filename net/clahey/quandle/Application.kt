package net.clahey.quandle

data class Application(val op: Operator, val inputs: List<Word>) : Word {
    override val size: Int = 1 + inputs.map {it.size}.sum()
    override fun toString(): String {
	if (inputs.size == 2) {
	    return "(" + inputs.get(0) + op + inputs.get(1) + ")"
	} else {
	    return op.toString() + "(" + inputs.map { it.toString() }.joinToString("") + ")"
	}
    }
}
