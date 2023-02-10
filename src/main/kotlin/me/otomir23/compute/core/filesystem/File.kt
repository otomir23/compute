package me.otomir23.compute.core.filesystem

/**
 * A file that is stored in the virtual filesystem.
 *
 * @param name The name of the file
 * @param parent The parent directory
 * @param content The content of the file
 */
class File(name: String, parent: Directory? = null, content: ByteArray? = null) : Element(name, parent) {
    override val path: String
        get() = "${parent?.path}$name"

    private var content: ByteArray = ByteArray(0)

    init {
        validate()
        if (content != null) write(content)
    }

    /**
     * Read the contents of the file.
     *
     * @return The contents of the file
     */
    fun read(): ByteArray {
        return content
    }

    /**
     * Write the contents of the file.
     *
     * @param content The new contents of the file
     * @throws OutOfSpaceException If there is not enough space to write the file
     */
    fun write(content: ByteArray) {
        val neededSpace = content.size
        if (fileSystem.freeSpace + this.content.size < neededSpace)
            throw OutOfSpaceException(this)
        this.content = content
    }

    /**
     * Write the contents of the file, appending to the end of the file.
     *
     * @param content The contents to append to the file
     * @throws OutOfSpaceException If there is not enough space to write the file
     */
    fun append(content: ByteArray) {
        write(this.content + content)
    }

    /**
     * Remove the contents of the file.
     */
    fun clear() {
        write(ByteArray(0))
    }

    /**
     * Size of the file.
     */
    val size: Int
        get() = content.size

    /**
     * Is the file is empty?
     */
    val isEmpty: Boolean
        get() = content.isEmpty()

    override fun toString(): String {
        return "$name ($size bytes)"
    }
}
