package andrasferenczi.traversal

import andrasferenczi.ext.psi.allChildren
import com.intellij.psi.PsiElement
import java.lang.RuntimeException

interface PsiTraversal : Function2<PsiElement, (PsiElement) -> Boolean, Sequence<PsiElement>> {
    override fun invoke(root: PsiElement, excludeChildren: (PsiElement) -> Boolean): Sequence<PsiElement>
}

private fun createPsiExtractor(): (PsiElement) -> Sequence<PsiElement> =
    { it.allChildren() }

private fun createTraversal(
    traversal: CommonTraversal<PsiElement>
): PsiTraversal {
    val extractor = createPsiExtractor()

    return object : PsiTraversal {
        override fun invoke(
            root: PsiElement,
            excludeChildren: (PsiElement) -> Boolean
        ): Sequence<PsiElement> {
            return traversal(root, excludeChildren, extractor)
        }
    }
}

fun createPsiTraversal(traversalType: TraversalType): PsiTraversal {
    val traversal = when (traversalType) {
        TraversalType.Breadth -> CommonBfsTraversal<PsiElement>()
        TraversalType.Depth -> CommonDfsTraversal<PsiElement>()
    }

    return createTraversal(traversal)
}

/**
 * If an item is accepted, it will be returned and none of it's children will be checked.
 *
 * No restriction is made how many times itemMapper is called
 *
 * itemMapper returns null if value is not desired
 */
fun <T : PsiElement> createPsiFilterTraversal(
    traversalType: TraversalType,
    itemMapper: (PsiElement) -> T?
): (PsiElement) -> Sequence<T> {
    val traversal = createPsiTraversal(traversalType)

    // Todo: This should be probably done somehow better to make sure the function is only called once
    return { root ->
        traversal(root)
        // If an item is not accepted, it returns null
        // if null is returned, no children should be checked
        // not null return -> check further
        { itemMapper(it) !== null }
            // And then filter the requested types only
            .filter { itemMapper(it) !== null }
            .map(itemMapper)
            .onEach {
                if (it === null) {
                    throw RuntimeException("The mapper function is not a pure function")
                }
            }
            // Values should be filtered out
            .map { it!! }
    }
}

