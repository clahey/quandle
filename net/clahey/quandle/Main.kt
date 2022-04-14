package net.clahey.quandle

fun main() {
    testNonInvolTrefoil()
}

fun testNonInvolTrefoil() {
    val a = Generator("a")
    val b = Generator("b")
    val c = Generator("c")
    val generators = setOf(a, b, c)
    val glossary = Glossary(generators=generators, base = quandleGlossary)
    val relations = listOf("a < b = c", "b < c = a" , "c < a = b").map{parseLaw(glossary, it)}.toSet()

    testKnot(generators, relations, 5, quandleVariety)
}

fun testTrefoil() {
    val a = Generator("a")
    val b = Generator("b")
    val c = Generator("c")
    val generators = setOf(a, b, c)
    val glossary = Glossary(generators=generators, base = quandleGlossary)
    val relations = listOf("a < b = c", "b < c = a" , "c < a = b").map{parseLaw(glossary, it)}.toSet()

    testKnot(generators, relations, 2)
}

fun testFigureEight() {
    val a = Generator("a")
    val b = Generator("b")
    val c = Generator("c")
    val d = Generator("d")
    val generators = setOf(a, b, c, d)
    val glossary = Glossary(generators=generators, base = quandleGlossary)
    val relations = listOf("a < b = d", "b < c = a" , "c < d = a", "d < b = c").map{parseLaw(glossary, it)}.toSet()

    testKnot(generators, relations, 3)
}

fun testCinquefoil() {
    val a = Generator("a")
    val b = Generator("b")
    val c = Generator("c")
    val d = Generator("d")
    val e = Generator("e")
    val generators = setOf(a, b, c, d, e)
    val glossary = Glossary(generators=generators, base = quandleGlossary)
    val relations = listOf("a < b = e", "b < c = a" , "c < d = b", "d < e = c", "e < a = d").map{parseLaw(glossary, it)}.toSet()

    testKnot(generators, relations, 3)
}

fun testThreeTwist() {
    val a = Generator("a")
    val b = Generator("b")
    val c = Generator("c")
    val d = Generator("d")
    val e = Generator("e")
    val generators = setOf(a, b, c, d, e)
    val glossary = Glossary(generators=generators, base = quandleGlossary)
    val relations = listOf("a < b = d", "b < c = a" , "c < d = e", "d < e = a", "e < b = c").map{parseLaw(glossary, it)}.toSet()

    testKnot(generators, relations, 4)
}

fun testSeptafoil() {
    val a = Generator("a")
    val b = Generator("b")
    val c = Generator("c")
    val d = Generator("d")
    val e = Generator("e")
    val f = Generator("f")
    val g = Generator("g")
    val generators = setOf(a, b, c, d, e, f, g)
    val glossary = Glossary(generators=generators, base = quandleGlossary)
    val relations = listOf("a < b = g", "b < c = a" , "c < d = b", "d < e = c", "e < f = d", "f < g = e", "g < a = f").map{parseLaw(glossary, it)}.toSet()

    testKnot(generators, relations, 3)
}

fun testKnot(generators: Set<Generator>, relations: Set<EquationalLaw>, repetitions: Int, variety: Variety = involutoryQuandleVariety) {

    val wordCollection = WordCollection(variety, generators, relations, variety.operators)

    println(wordCollection.wordSets.map { it.words.size }.sum())

    for (i in 1..repetitions) {
        println(wordCollection.wordSets.map { it.words.size }.sum())
        wordCollection.process()
    }

    println(wordCollection.wordSets)
    for (op in variety.operators) {
        for (x in wordCollection.wordSets) {
            for (y in wordCollection.wordSets) {
                val xMinWord = x.minWord!!
                val yMinWord = y.minWord!!
                val reduced = wordCollection.wordMap.get(Application(op, listOf(xMinWord, yMinWord)))
                println ("$xMinWord $op $yMinWord = $reduced")
            }
        }
    }
}
