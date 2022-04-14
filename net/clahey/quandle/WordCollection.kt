package net.clahey.quandle

class WordCollection(val variety: Variety, val generators: Set<Generator>, val relations: Set<EquationalLaw>, val ops: List<Operator>) {
    var words = mutableListOf<Set<Word>>()
    var wordMap = mutableMapOf<Word, WordSet>()
    var wordSets = mutableSetOf<WordSet>()
    var age: Int = 1
    var combinedLaws = variety.laws.union(relations)
    init {
        words.add(
            buildSet {
                for (generator in generators) {
                    add(generator)
                    addWord(generator)
                }
        })
    }
    
    internal fun addWord(word: Word) {
        val mergeableWordSets = mutableSetOf<WordSet>(WordSet(setOf(word)))
        for (law in combinedLaws) {
            val reduced: Word? = law.reduce(word)
            if (reduced != null) {
                val wordSet = wordMap.get(reduced)
                if (wordSet != null) {
                    println ("$word reduces to $reduced")
                    mergeableWordSets.add(wordSet)
                }
            }
        }
        mergeWordSets(mergeableWordSets)
    }

    fun mergeWordSets(mergeableWordSets: MutableSet<WordSet>) {
        for(wordSet in mergeableWordSets) {
            wordSets.remove(wordSet)
        }
        val newWordSet = WordSet.flatten(mergeableWordSets)
        wordSets.add(newWordSet)
        for (w in newWordSet.words) {
            wordMap.set(w, newWordSet)
        }
    }

    fun process() {
        age ++
        words.add(
            buildSet {
                for (i in 1..age-1) {
                    for (op in ops) {
                        for (a in words[i - 1]) {
                            for (b in words[age - i - 1]) {
                                val word = Application(op, listOf(a, b))
                                add(word)
                                addWord(word)
                            }
                        }
                    }
                }
        })
        println("Clean up sets")
        mergeSets()
    }

    fun mergeSets() {
        while(mergeSetsOnce()) {
        }
    }

    fun mergeSetsOnce(): Boolean {
        println ("mergeSetsOnce")
        var changed = false
        for (wordList in words) {
            for (word in wordList) {
                val wordSet = wordMap.get(word)
                if (wordSet != null) {
                    if (handleMerge(word, wordSet)) {
                        changed = true
                    }
                } else {
                    println("$word not found in map")
                }
            }
        }
        return changed
    }

    fun handleMerge(word: Word, wordSet: WordSet): Boolean {
        val mergeableWordSets = mutableSetOf<WordSet>()
        if (word is Application) {
            val (left, right) = word.inputs
            val leftSet = wordMap.get(left)!!
            val rightSet = wordMap.get(right)!!
            if (left != leftSet.minWord) {
                val reduced = Application(word.op, listOf(leftSet.minWord!!, right))
                val otherWordSet = wordMap.get(reduced)!!
                if (otherWordSet != wordSet) {
                    println ("$word merges to $otherWordSet")
                    mergeableWordSets.add(otherWordSet)
                }
            }
            if (right != rightSet.minWord) {
                val reduced = Application(word.op, listOf(left, rightSet.minWord!!))
                val otherWordSet = wordMap.get(reduced)!!
                if (otherWordSet != wordSet) {
                    println ("$word merges to $otherWordSet")
                    mergeableWordSets.add(otherWordSet)
                }
            }
            if (left != leftSet.minWord && right != rightSet.minWord) {
                val reduced = Application(word.op, listOf(leftSet.minWord, rightSet.minWord))
                val otherWordSet = wordMap.get(reduced)!!
                if (otherWordSet != wordSet) {
                    println ("$word merges to $otherWordSet")
                    mergeableWordSets.add(otherWordSet)
                }
            }
            if (!mergeableWordSets.isEmpty()) {
                mergeableWordSets.add(wordSet)
                mergeWordSets(mergeableWordSets)
                return true
            }
        }
        return false
    }
}
