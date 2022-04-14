package net.clahey.quandle

class WordSet(val words: Set<Word>) {
    val minSize: Int by lazy { words.minOfOrNull { it.size } ?: 0 }
    val minWord: Word? = words.minByOrNull { it.size }
    override fun toString(): String = minWord.toString() ?: "()"
    companion object {
        fun union(set1: WordSet, set2: WordSet): WordSet {
            if (set1.words.isEmpty()) {
                return set2;
            } else if (set2.words.isEmpty()) {
                return set1;
            } else {
                return WordSet(set1.words.union(set2.words))
            }
        }
        fun flatten(sets: Set<WordSet>): WordSet {
            return WordSet(sets.flatMap { it.words }.toSet())
        }
    }
}

