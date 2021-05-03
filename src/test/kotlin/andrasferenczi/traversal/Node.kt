package andrasferenczi.traversal

import java.util.*

class Node<T>(
    val value: T,
    children: List<Node<T>>
) {

    private val children: MutableList<Node<T>> = children.toMutableList()

    operator fun plusAssign(node: Node<T>) {
        this.children.add(node)
    }

    fun iterateChildren(): Sequence<Node<T>> =
        children.asSequence()

}

class NodeBuilder<T>(private val value: T) {

    private val children: MutableList<NodeBuilder<T>> = LinkedList()

    fun node(value: T, builder: NodeBuilder<T>.() -> Unit = {}) {
        val newNodeBuilder = NodeBuilder(value)
        newNodeBuilder.builder()
        this.children.add(newNodeBuilder)
    }

    fun toNode(): Node<T> =
        Node(value, children.map { it.toNode() })

}

fun <T> node(value: T, builder: NodeBuilder<T>.() -> Unit = {}): Node<T> {
    val newNodeBuilder = NodeBuilder(value)
    newNodeBuilder.builder()
    return newNodeBuilder.toNode()
}
