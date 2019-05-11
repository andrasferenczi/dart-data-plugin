package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.init.tryCreateActionData
import andrasferenczi.action.init.tryExtractDartClassDefinition
import andrasferenczi.ext.extractOuterDartClass
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.lang.dart.psi.DartClassDefinition

abstract class BaseAnAction : AnAction() {

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible =
            event.extractOuterDartClass() !== null
    }

    final override fun actionPerformed(event: AnActionEvent) {
        val actionData = tryCreateActionData(event) ?: return
        val dartClass = tryExtractDartClassDefinition(actionData) ?: return

        this.performAction(
            event,
            actionData,
            dartClass
        )
    }

    protected abstract fun performAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    )

}