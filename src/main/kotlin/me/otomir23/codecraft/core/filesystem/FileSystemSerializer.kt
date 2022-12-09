package me.otomir23.codecraft.core.filesystem

import java.nio.charset.Charset

object FileSystemSerializer {
    /**
     * Magic number for the filesystem.
     */
    private const val MAGIC_NUMBER = 0xFADE23

    /**
     * Serialize a filesystem to a byte array.
     *
     * @param fileSystem The filesystem to serialize
     * @return The serialized filesystem
     */
    fun serialize(fileSystem: FileSystem): ByteArray {
        return intToBytes(MAGIC_NUMBER) + intToBytes(fileSystem.space) + serializeDirectoryContents(fileSystem)
    }

    /**
     * Deserialize a filesystem from a byte array.
     *
     * @param source The bytes to deserialize
     * @return The deserialized filesystem
     */
    fun deserialize(source: ByteArray): FileSystem {
        if (bytesToInt(source.sliceArray(0..3)) != MAGIC_NUMBER)
            throw IllegalArgumentException("Invalid filesystem byte array")
        val space = bytesToInt(source.sliceArray(4..7))
        val system = FileSystem(space)

        deserializeDirectoryContents(source.sliceArray(8 until source.size), system)
        return system
    }

    private fun serializeFile(file: File): ByteArray {
        val name = file.name.toByteArray(Charset.forName("UTF-8"))

        val nameSize = intToBytes(name.size)
        val contentSize = intToBytes(file.size)

        return nameSize + name + contentSize + file.read()
    }

    private fun serializeDirectory(directory: Directory): ByteArray {
        val name = directory.name.toByteArray(Charset.forName("UTF-8"))
        val nameSize = intToBytes(name.size)

        return nameSize + name + serializeDirectoryContents(directory)
    }

    private fun serializeDirectoryContents(directory: Directory): ByteArray {
        val subDirCount = intToBytes(directory.subdirectories.size)
        val fileCount = intToBytes(directory.files.size)

        var arr = subDirCount + fileCount

        directory.subdirectories.forEach { arr += serializeDirectory(it) }
        directory.files.forEach { arr += serializeFile(it) }

        return arr
    }

    private fun deserializeDirectory(source: ByteArray, parent: Directory): Int {
        val nameSize = bytesToInt(source.sliceArray(0..3))
        val name = String(source.sliceArray(4 until 4 + nameSize), Charset.forName("UTF-8"))
        val dir = Directory(name, parent)

        return 4 + nameSize + deserializeDirectoryContents(source.sliceArray(4 + nameSize until source.size), dir)
    }

    private fun deserializeDirectoryContents(source: ByteArray, directory: Directory): Int {
        val subDirCount = bytesToInt(source.sliceArray(0..3))
        val fileCount = bytesToInt(source.sliceArray(4..7))

        var offset = 8

        repeat(subDirCount) {
            offset += deserializeDirectory(source.sliceArray(offset until source.size), directory)
        }

        repeat(fileCount) {
            offset += deserializeFile(source.sliceArray(offset until source.size), directory)
        }

        return offset
    }

    private fun deserializeFile(source: ByteArray, parent: Directory): Int {
        val nameSize = bytesToInt(source.sliceArray(0..3))
        val name = String(source.sliceArray(4 until 4 + nameSize), Charset.forName("UTF-8"))
        val contentSize = bytesToInt(source.sliceArray(4 + nameSize..7 + nameSize))
        val content = source.sliceArray(8 + nameSize until 8 + nameSize + contentSize)

        val file = File(name, parent)
        file.write(content)

        return 8 + nameSize + contentSize
    }

    /**
     * Writes an int to a byte array.
     *
     * @param value The int to write
     * @return The byte array
     */
    private fun intToBytes(value: Int): ByteArray {
        val buffer = ByteArray(4)
        for (i in 0..3) buffer[i] = (value shr (i * 8)).toByte()
        return buffer
    }

    /**
     * Reads an int from a byte array.
     *
     * @param source The byte array to read from
     * @return The int read from the byte array
     */
    private fun bytesToInt(source: ByteArray): Int {
        return (source[3].toInt() shl 24) or
                (source[2].toInt() and 0xff shl 16) or
                (source[1].toInt() and 0xff shl 8) or
                (source[0].toInt() and 0xff)
    }
}
