package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.utils.DartConstructorType
import andrasferenczi.action.utils.createConstructorDeleteCallWithUserPrompt
import andrasferenczi.action.utils.deleteAllPsiElements
import andrasferenczi.action.utils.extractMethodConstructorInfos
import andrasferenczi.declaration.DeclarationExtractor
import andrasferenczi.declaration.canBeAssignedFromConstructor
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.evalAnchorInClass
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.runWriteAction
import andrasferenczi.ext.setCaretSafe
import andrasferenczi.templater.ConstructorTemplateParams
import andrasferenczi.templater.createConstructorTemplate
import andrasferenczi.utils.mergeCalls
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class NamedArgumentConstructorAction : BaseAnAction() {

    override fun performAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    ) {

        val (project, editor, _, _) = actionData

        val dartClassName = dartClass.extractClassName()
        val declarations = DeclarationExtractor
            .extractDeclarationsFromClass(dartClass)

        val variableNames = declarations
            .filter { it.canBeAssignedFromConstructor }
            .map { it.variableName }

        val templateManager = TemplateManager.getInstance(project)

        val template = createConstructorTemplate(
            templateManager,
            ConstructorTemplateParams(
                className = dartClassName,
                publicVariableNames = variableNames
            )
        )

        val constructorDeleteCall = createConstructorDeleteCallWithUserPrompt(project, dartClass)

        project.runWriteAction {
            constructorDeleteCall?.let {
                it.invoke()

                PsiDocumentManager.getInstance(project)
                    .doPostponedOperationsAndUnblockDocument(editor.document)
            }

            val anchor = editor.evalAnchorInClass(dartClass)
            editor.setCaretSafe(dartClass, anchor.textRange.endOffset)
            templateManager.startTemplate(editor, template)
        }
    }
}