package andrasferenczi.ext.psi

import andrasferenczi.traversal.TraversalType
import andrasferenczi.traversal.createPsiFilterTraversal
import com.intellij.psi.PsiElement

/**
 * No such method error with this function, hence the name to distinguish.
 */
fun PsiElement.mySiblings(forward: Boolean = true): Sequence<PsiElement> {
    return generateSequence(this) {
        if (forward) {
            it.nextSibling
        }
        else {
            it.prevSibling
        }
    }
}

/**
 * Does not omit children that are PsiWhiteSpace or LeafPsiNode
 */
fun PsiElement.allChildren(): Sequence<PsiElement> {
    return firstChild?.mySiblings() ?: emptySequence()
}

fun PsiElement.listChildrenRecursivelyForDebug(depth: Int = 5): List<Any> {
    if (depth <= 0) {
        return emptyList()
    }

    return listOf(this)
        .map {
            it to it.allChildren().map { child ->
                child to child.listChildrenRecursivelyForDebug(depth - 1)
            }.toList()
        }
}

inline fun <reified T : PsiElement> PsiElement.findFirstParentOfType(): T? {
    var current: PsiElement? = this

    while (true) {
        if (current == null) {
            return null
        }

        if (current is T) {
            return current
        }

        current = current.parent
    }
}

inline fun <reified T : PsiElement> PsiElement.findFirstChildByType(
    traversalType: TraversalType = TraversalType.Breadth
): T? = this.findChildrenByType<T>(traversalType).firstOrNull()

inline fun <reified T : PsiElement> PsiElement.findChildrenByType(
    traversalType: TraversalType = TraversalType.Breadth
): Sequence<T> {
    return createPsiFilterTraversal(traversalType) {
        if (it is T) it
        else null
    }.invoke(this)
}

/**
 * Depth First Search
 *
 * action: returns 'false' if DFS should not go deeper into that element
 */
fun PsiElement.iterateDFS(action: (element: PsiElement) -> Boolean) {
    for (child in this.allChildren()) {
        if (action(child)) {
            child.iterateDFS(action)
        }
    }
}
