package andrasferenczi.traversal

interface NodeTraversal<T> : Function2<Node<T>, (Node<T>) -> Boolean, Sequence<Node<T>>> {
    override fun invoke(root: Node<T>, excludeChildren: (Node<T>) -> Boolean): Sequence<Node<T>>
}

private fun <T> createNodeExtractor(callback: (Node<T>) -> Unit): (Node<T>) -> Sequence<Node<T>> =
    {
        callback(it)
        it.iterateChildren()
    }

private fun <T> createTraversal(
    traversal: CommonTraversal<Node<T>>,
    extractChildrenCallback: (Node<T>) -> Unit
): NodeTraversal<T> {
    val extractor = createNodeExtractor(extractChildrenCallback)

    return object : NodeTraversal<T> {
        override fun invoke(
            root: Node<T>,
            excludeChildren: Node<T>.() -> Boolean
        ): Sequence<Node<T>> {
            return traversal(root, excludeChildren, extractor)
        }
    }
}

fun <T> createNodeTraversal(traversalType: TraversalType, extractChildrenCallback: (Node<T>) -> Unit = {}): NodeTraversal<T> {
    val traversal = when (traversalType) {
        TraversalType.Breadth -> CommonBfsTraversal<Node<T>>()
        TraversalType.Depth -> CommonDfsTraversal<Node<T>>()
    }

    return createTraversal(traversal, extractChildrenCallback)
}
