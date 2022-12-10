package me.otomir23.codecraft.core.filesystem

/**
 * Representation of a directory in the virtual filesystem.
 * Can contain other elements.
 *
 * @param name The name of the directory
 * @param parent The parent directory
 */
open class Directory(name: String, parent: Directory? = null) : Element(name, parent) {
    override val path: String
        get() = "${parent?.path}$name/"

    private val _children: MutableList<Element> = mutableListOf()

    /**
     * Elements stored in this directory.
     */
    val children: List<Element>
        get() = _children.toList()

    /**
     * Subdirectories stored in this directory.
     */
    val subdirectories: List<Directory>
        get() = children.filterIsInstance<Directory>()

    /**
     * Files stored in this directory.
     */
    val files: List<File>
        get() = children.filterIsInstance<File>()


    /**
     * Is this directory empty?
     */
    val isEmpty: Boolean
        get() = _children.isEmpty()

    init {
        validate()
    }

    /**
     * Update the directory's children list.
     * Removes all elements that are not actually in the directory.
     *
     * @param additions Elements that should be added to the directory
     */
    fun sync(vararg additions: Element) {
        _children.removeAll { it.parent != this }
        _children.addAll(additions.filter { it.parent == this && _children.contains(it).not() })
    }

    /**
     * Recursively delete all elements in this directory and the directory itself.
     */
    override fun delete() {
        _children.forEach { it.delete() }
        super.delete()
    }

    /**
     * Finds an element with the given name in this directory.
     * Note that this method does not search recursively.
     *
     * @return The element with the given name, or null if no such element exists
     */
    fun find(name: String): Element? {
        return _children.find { it.name == name }
    }

    /**
     * Finds a file with the given name in this directory.
     * Note that this method does not search recursively.
     *
     * @return The element with the given name, or null if no such element exists
     */
    fun findFile(name: String): File? {
        return files.find { it.name == name }
    }

    /**
     * Finds a subdirectory with the given name.
     * Note that this method does not search recursively.
     *
     * @return The element with the given name, or null if no such element exists
     */
    fun findDirectory(name: String): Directory? {
        return subdirectories.find { it.name == name }
    }

    /**
     * Get all files in this directory and its subdirectories.
     *
     * @return A list of all files in this directory and its subdirectories
     */
    fun getAllFiles(): List<File> {
        return (files + subdirectories.flatMap { it.getAllFiles() }).toList()
    }

    override fun toString(): String =
        "$name (DIR)\n${_children.joinToString("\n") { it.toString().prependIndent(" ├ " ) }}"
}
