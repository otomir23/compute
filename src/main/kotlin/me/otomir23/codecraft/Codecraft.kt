package me.otomir23.codecraft

import me.otomir23.codecraft.core.filesystem.*
import java.nio.charset.Charset
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Codecraft : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("Codecraft")
    const val MOD_ID: String = "codecraft"

    override fun onInitialize(mod: ModContainer) {
        // Filesystem Demo
        val fs = FileSystem()

        // Create some directories
        val dir = Directory("docs", fs)
        val dir2 = Directory("cats 😻😻", dir)

        // Create some files
        val file = File("indium-1.0.7mc1.18.2.jar", dir2, "whaaa".toByteArray()) // We can specify the content of the file
        val file2 = File("THE MEOWING (МЯУКАНЬЕ)", dir2) // Filenames support UTF-8!

        // Write to files
        file2.write("meow".toByteArray()) // English Characters

        // Append to files
        file2.append("Мяу".toByteArray(Charset.forName("UTF-8"))) // Cyrillic Characters
        file2.append("喵".toByteArray(Charset.forName("UTF-8"))) // Chinese Characters
        file2.append("ネコ".toByteArray(Charset.forName("UTF-8"))) // Japanese Characters
        file2.append("🐈".toByteArray(Charset.forName("UTF-8"))) // Emoji

        // Let's see what we have
        LOGGER.info("Filesystem: \n$fs")

        // Serialize the filesystem into a byte array
        val s = FileSystemSerializer.serialize(fs)

        // Deserialize the filesystem into a new filesystem
        val fs2 = FileSystemSerializer.deserialize(s)

        // Result: the filesystems are identical
        LOGGER.info("Filesystem: \n$fs2")

        // Let's verify that the files are identical
        val element = fs2.getElement("/docs/cats 😻😻/THE MEOWING (МЯУКАНЬЕ)")
        if (element is File) {
            LOGGER.info("File contents: ${String(element.read())}")
            LOGGER.info("Original file contents: ${String(file2.read())}")
        } else {
            // This should never happen, but just in case
            LOGGER.error("Element is not a file!\n$element")
        }

        // Now lets try some edge cases

        // 1. Deserializing invalid data
        try {
            FileSystemSerializer.deserialize("invalid data".toByteArray())
        } catch (e: IllegalArgumentException) {
            LOGGER.error("Deserialization failed! (As expected)", e)
        }

        // 2. Writing a large file into a small filesystem
        try {
            val fs3 = FileSystem(10) // It has only 10 bytes of space 😿😿
            val largeFile = File(
                "large",
                fs3,
                ("h".repeat(32)).toByteArray()
            ) // It's 32 bytes long (+5 bytes in name), so it should not fit into the filesystem
        } catch (e: OutOfSpaceException) {
            LOGGER.error("Writing failed! (As expected)", e)
        }

        // 3. Creating an element with existing name
        try {
            val fs4 = FileSystem()
            val file3 = File("file", fs4)
            val file4 = Directory("file", fs4)
        } catch (e: ElementAlreadyExistsException) {
            LOGGER.error("Creating file failed! (As expected)", e)
        }

        // 4. Creating an element with invalid name
        try {
            val fs5 = FileSystem()
            val file5 = File("invalid:name", fs5)
        } catch (e: IllegalElementNameException) {
            LOGGER.error("Creating file failed! (As expected)", e)
        }

        // 5. Creating an element without a parent
        try {
            val fs6 = FileSystem()
            val file6 = File("file")
            println(file6.path)
        } catch (e: ElementWithoutParentException) {
            LOGGER.error("Creating file failed! (As expected)", e)
        }

        // 6. Creating a file with a name that is too long
        try {
            val fs7 = FileSystem(10)
            val file7 = File("a".repeat(256), fs7)
        } catch (e: OutOfSpaceException) {
            LOGGER.error("Creating file failed! (As expected)", e)
        }

        // That's it for now!
        // I am planning to add permissions, users, groups, and more.

        LOGGER.info("{} successfully loaded!", mod.metadata()?.name())
    }
}
