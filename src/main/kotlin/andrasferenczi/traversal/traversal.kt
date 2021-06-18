package andrasferenczi.traversal

import java.util.*

/**
 * This files serves as a generalization for traversing all the elements in a lazy way,
 * to reduce unnecessary calls.
 *
 * This case is especially visible when trying the get a single element of a specific type.
 *
 * BFS search in finding the first elem only will be way faster in bigger classes.
 */

enum class TraversalType {
    Breadth,
    Depth;
}

interface CommonTraversal<N> : Function3<N, (N) -> Boolean, N.() -> Sequence<N>, Sequence<N>> {

    override fun invoke(
        root: N,
        // Does not go deeper into the hierarchy if this returns true
        excludeChildren: (N) -> Boolean,
        extractChildren: N.() -> Sequence<N>
    ): Sequence<N>

}

class CommonBfsTraversal<N> : CommonTraversal<N> {

    override fun invoke(
        root: N,
        excludeChildren: (N) -> Boolean,
        extractChildren: N.() -> Sequence<N>
    ): Sequence<N> {
        return sequence {
            val excludeAll = excludeChildren(root)
            yield(root)

            if (excludeAll) {
                return@sequence
            }

            var currentChildren: List<N> = listOf(root)
            var nextChildren: MutableList<N>

            while (currentChildren.isNotEmpty()) {
                nextChildren = LinkedList()

                currentChildren
                    .asSequence()
                    .flatMap { it.extractChildren() }
                    .forEach {
                        if (!excludeChildren(it)) {
                            nextChildren.add(it)
                        }
                        yield(it)
                    }

                currentChildren = nextChildren
            }
        }
    }
}

class CommonDfsTraversal<N> : CommonTraversal<N> {

    override fun invoke(
        root: N,
        excludeChildren: (N) -> Boolean,
        extractChildren: N.() -> Sequence<N>
    ): Sequence<N> {
        return sequenceOf(root)
            .flatMap { child ->
                if (excludeChildren(child))
                    sequenceOf(child)
                else
                    sequence {
                        yield(child)
                        yieldAll(
                            child
                                .extractChildren()
                                .flatMap { invoke(it, excludeChildren, extractChildren) }
                        )
                    }
            }
    }
}