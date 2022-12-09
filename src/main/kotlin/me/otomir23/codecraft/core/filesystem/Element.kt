package me.otomir23.codecraft.core.filesystem

/**
 * Abstract element of a virtual filesystem.
 *
 * @param name Name of the element
 * @param parent Parent directory of the element
 */
abstract class Element(name: String, parent: Directory? = null) {
    // TODO permissions, process locking, etc.

    /**
     * Path of the element.
     *
     * @throws ElementWithoutParentException If the element doesn't have a parent and is not a root element
     */
    abstract val path: String

    /**
     * Virtual filesystem this element belongs to.
     *
     * @throws ElementWithoutParentException If the element doesn't have a parent and is not a root directory
     */
    val fileSystem: FileSystem
        get() = if (this is FileSystem) this else parent?.fileSystem ?: throw ElementWithoutParentException(this)

    /**
     * Name of the element.
     */
    var name: String = name
        set(value) {
            if (this !is FileSystem && (value.isBlank() || value.contains(Regex("[/\\\\:*?\"<>|]"))))
                throw IllegalElementNameException(this, value)
            val sameNameElement = parent?.find(value)
            if (sameNameElement != null && sameNameElement != this)
                throw ElementAlreadyExistsException(this, value)
            field = value
        }

    /**
     * Parent directory of this element.
     *
     * @throws ElementWithoutParentException If the element doesn't have a parent and is not a root directory
     */
    var parent: Directory? = parent
        get() = when (this is FileSystem) {
            true -> null
            false -> field?: throw ElementWithoutParentException(this)
        }
        set(value) {
            val previousParent = field
            field = value

            previousParent?.sync()
            field?.sync(this)
        }

    init {
        this.parent = parent
        this.name = name
    }

    /**
     * Delete this element.
     */
    open fun delete() {
        this.parent = null
    }
}
