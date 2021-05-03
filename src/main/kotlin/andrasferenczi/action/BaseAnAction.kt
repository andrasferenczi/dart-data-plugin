package andrasferenczi.action

import andrasferenczi.action.data.PerformAction
import andrasferenczi.action.init.ActionData
import andrasferenczi.action.init.tryCreateActionData
import andrasferenczi.action.init.tryExtractDartClassDefinition
import andrasferenczi.ext.*
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

abstract class BaseAnAction : AnAction() {

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabledAndVisible =
            event.extractOuterDartClass() !== null
    }

    // Actions that change PSI elements should be
    override fun startInTransaction(): Boolean = true

    final override fun actionPerformed(event: AnActionEvent) {
        val actionData = tryCreateActionData(event) ?: return
        val dartClass = tryExtractDartClassDefinition(actionData) ?: return

        val performAction = this.processAction(
            event,
            actionData,
            dartClass
        ) ?: return

        val (project, editor, _, _) = actionData

        val templateManager = TemplateManager.getInstance(project)

        project.runWriteAction {
            performAction.deleteAction?.let {
                it.invoke()

                PsiDocumentManager.getInstance(project)
                    .doPostponedOperationsAndUnblockDocument(editor.document)
            }

            val templates = performAction.templatesToAdd

            editor.setCaretToEndOfTheClass(dartClass)

            // Todo: Set caret at the end of each template (?)

            templateManager.startTemplate(editor, templateManager.createSeparatorTemplate())

            templates.forEachIndexed { index, template ->
                templateManager.startTemplate(editor, template)

                if (index != templates.lastIndex) {
                    templateManager.startTemplate(editor, templateManager.createSeparatorTemplate())
                }
            }

        }
    }

    protected abstract fun processAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    ): PerformAction?

}