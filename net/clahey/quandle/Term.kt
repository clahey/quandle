package net.clahey.quandle

interface Term : Word {
    override val size: Int get() = 1
}
