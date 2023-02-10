package me.otomir23.compute.core.filesystem

/**
 * Base exception for errors that occur while executing operations on the virtual filesystem.
 */
open class VirtualFileSystemException(val element: Element, msg: String) : RuntimeException(msg)

/**
 * Exception thrown when there is not enough space to write to the virtual filesystem.
 */
open class OutOfSpaceException(element: Element) : VirtualFileSystemException(element, "Virtual filesystem doesn't have enough space for this write")

/**
 * Exception thrown when an element in the virtual filesystem doesn't have a parent and is not a root element.
 */
open class ElementWithoutParentException(element: Element) : VirtualFileSystemException(element, "Element doesn't have a parent and is not a root directory")

/**
 * Exception thrown when name of an element is invalid.
 */
open class IllegalElementNameException(element: Element, name: String) : VirtualFileSystemException(element, "Invalid name for element: $name")

/**
 * Exception thrown if there is already an element with the same name in the same directory.
 */
open class ElementAlreadyExistsException(element: Element, name: String) : VirtualFileSystemException(element, "Element already exists: $name")
