package andrasferenczi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.siblings
import java.util.*

/**
 * Does not omit children that are PsiWhiteSpace or LeafPsiNode
 */
fun PsiElement.allChildren(): List<PsiElement> {
    return firstChild?.siblings()?.toList() ?: emptyList()
}

fun PsiElement.calculateGlobalOffset(): Int {
    return this.startOffsetInParent + (this.parent?.startOffsetInParent ?: 0)
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

inline fun <reified T : PsiElement> PsiElement.findChildrenByType(): List<T> {
    // Special case, do not go further
    if (this is T) {
        return listOf(this)
    }

    val result: MutableList<T> = LinkedList()

    var currentChildren: List<PsiElement> = LinkedList<PsiElement>().also { it += this }
    var nextChildren: MutableList<PsiElement>

    while (currentChildren.isNotEmpty()) {
        nextChildren = LinkedList()

        currentChildren
            .flatMap { it.allChildren() }
            .forEach { currentChild ->
                if (currentChild is T) {
                    result += currentChild
                } else {
                    nextChildren.add(currentChild)
                }
            }

        currentChildren = nextChildren
    }

    return result
}

/**
 * Depth First Search
 *
 * action: returns 'false' if DFS should not go deeper into that
 */
fun PsiElement.iterateDFS(action: (element: PsiElement) -> Boolean) {
    for (child in this.allChildren()) {
        if (action(child)) {
            child.iterateDFS(action)
        }
    }
}
