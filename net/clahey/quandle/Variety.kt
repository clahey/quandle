package net.clahey.quandle

class Variety(val type: Type, val operators: List<Operator>, val laws: Set<EquationalLaw>) {
    @OptIn(kotlin.ExperimentalStdlibApi::class)
    val operatorMap: Map<String, Int> by lazy {
        buildMap {
	    for ((i, op) in operators.withIndex()) {
	        put(op.getRepresentation(), i)
	    }
        }
    }
}
