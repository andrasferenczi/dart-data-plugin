package andrasferenczi.action.data

import andrasferenczi.utils.mergeCalls
import com.intellij.codeInsight.template.Template
import sun.misc.Perf

class PerformAction(
    // Some objects might be deleted
    val deleteAction: (() -> Unit)?,
    // The code that will be added
    val templatesToAdd: List<Template>
) {

    constructor(
        deleteAction: (() -> Unit)?,
        template: Template
    ) : this(deleteAction, listOf(template))

    fun combineWith(action: PerformAction): PerformAction {
        val deleteAction = listOfNotNull(this.deleteAction, action.deleteAction).mergeCalls()
        val templates = listOf(this.templatesToAdd, action.templatesToAdd).flatten()

        return PerformAction(
            deleteAction,
            templates
        )
    }

    companion object {

        fun createEmpty() = PerformAction(null, emptyList())

        fun combineAll(vararg actions: PerformAction?): PerformAction {
            var result = createEmpty()
            actions
                .filterNotNull()
                .forEach { result = result.combineWith(it) }
            return result
        }

    }
}

fun Iterable<PerformAction?>.combineAll(): PerformAction {
    return PerformAction.Companion.combineAll(*this.toList().toTypedArray())
}