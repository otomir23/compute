package me.otomir23.compute.core.filesystem

/**
 * Abstract element of a virtual filesystem.
 *
 * Every child class should call [validate] in their constructor!
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
            if (this !is FileSystem) {
                if (value.isBlank() || value.contains(Regex("[/\\\\:*?\"<>|]")))
                    throw IllegalElementNameException(this, value)
                val sameNameElement = parent?.find(value)
                if (sameNameElement != null && sameNameElement != this)
                    throw ElementAlreadyExistsException(this, value)
                if (fileSystem.freeSpace + name.length < value.length)
                    throw OutOfSpaceException(this)
            }
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

    /**
     * Validate name and parent.
     *
     * EVERY CHILD MUST CALL THIS METHOD IN THEIR CONSTRUCTOR!
     *
     * @throws IllegalElementNameException If the name is invalid
     * @throws ElementWithoutParentException If the element doesn't have a parent and is not a root directory
     * @throws ElementAlreadyExistsException If the element already exists in the parent directory
     * @throws OutOfSpaceException If there is not enough space to create the element
     */
    fun validate() {
        // I wish I could use a constructor, but then children properties would be uninitialized,
        // so I have to use this method instead.
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
