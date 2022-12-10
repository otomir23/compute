package me.otomir23.codecraft.core.filesystem

/**
 * Virtual filesystem represented as a directory.
 *
 * @param size The size of the filesystem
 */
class FileSystem(size: Int = 1024 * 1024): Directory("/") {
    val space = size

    /**
     * Free space in the filesystem.
     */
    val freeSpace
        get() = space - getAllFiles().sumOf { it.size + it.name.length }

    override val path: String
        get() = "/"

    /**
     * Finds an element on a given path.
     */
    fun getElement(path: String): Element? {
        val parts = path.split("/").filter { it.isNotBlank() }
        var current: Directory = this
        for (part in parts.slice(0 until parts.size - 1)) {
            current = current.subdirectories.find { it.name == part } ?: return null
        }
        return current.find(parts.last())
    }

    override fun delete() {
        children.forEach { it.delete() }
    }

    override fun toString(): String =
        "[$freeSpace/$space free]\n${super.toString()}"
}
