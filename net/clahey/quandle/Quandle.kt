package net.clahey.quandle


val quandleOperators = listOf("<", ">").map({Operator(it)})

val quandleGlossary = Glossary(operators = quandleOperators.toSet())
val quandleLaws: Set<EquationalLaw> =
    listOf(
	"x < x = x",
	"x < (y > x) = y",
	"(x < y) > x = y",
	"x < (y < z) = (x < y) < (x < z)",
	"(y > z) > x = (y > x) < (z > x)",
    ).map { parseLaw(quandleGlossary, it) }.toSet()

val quandleVariety = Variety(Type(listOf(2, 2)), quandleOperators, quandleLaws)
open class Quandle(extraLaws: Set<EquationalLaw> = setOf()) :
    Algebra(Type(listOf(2, 2)), quandleOperators, quandleLaws.union(extraLaws))


val involutoryQuandleApply = Operator("<")
val involutoryQuandleOperators = listOf(involutoryQuandleApply)
val involutoryQuandleGlossary = Glossary(operators = involutoryQuandleOperators.toSet())

val involutoryQuandleLaws: Set<EquationalLaw> =
	listOf(
		"x < x = x",
		"x < (x < y) = y",
		"x < (y < z) = (x < y) < (x < z)",
	).map { parseLaw(involutoryQuandleGlossary, it) }.toSet()

val involutoryQuandleVariety = Variety(Type(listOf(2)), involutoryQuandleOperators, involutoryQuandleLaws)

open class InvolutoryQuandle() :
    Algebra(Type(listOf(2, 2)), involutoryQuandleOperators, involutoryQuandleLaws)
