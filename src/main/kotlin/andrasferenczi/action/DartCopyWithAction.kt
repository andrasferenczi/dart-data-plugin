package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.utils.createCopyWithDeleteCall
import andrasferenczi.action.utils.selectFieldsWithDialog
import andrasferenczi.configuration.ConfigurationDataManager
import andrasferenczi.declaration.canBeAssignedFromConstructor
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.evalAnchorInClass
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.runWriteAction
import andrasferenczi.ext.setCaretSafe
import andrasferenczi.templater.CopyWithTemplateParams
import andrasferenczi.templater.VariableTemplateParam
import andrasferenczi.templater.createCopyWithConstructorTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class DartCopyWithAction : BaseAnAction() {

    override fun performAction(event: AnActionEvent, actionData: ActionData, dartClass: DartClassDefinition) {
        val (project, editor, _, _) = actionData

        val declarations = selectFieldsWithDialog(project, dartClass) ?: return

        val variableNames: List<VariableTemplateParam> = declarations
            .filter { it.canBeAssignedFromConstructor }
            .map {
                VariableTemplateParam(
                    it.dartType?.text
                        ?: throw RuntimeException("No type is available - this variable should not be assignable from constructor"),
                    it.variableName,
                    it.variableName
                )
            }

        val templateManager = TemplateManager.getInstance(project)
        val configuration = ConfigurationDataManager.retrieveData(project)
        val dartClassName = dartClass.extractClassName()

        val template = createCopyWithConstructorTemplate(
            templateManager,
            CopyWithTemplateParams(
                className = dartClassName,
                variableNames = variableNames,
                copyWithMethodName = configuration.copyWithMethodName,
                useNewKeyword = configuration.useNewKeyword
            )
        )

        val copyWithDeleteCall = createCopyWithDeleteCall(
            dartClass,
            configuration.copyWithMethodName
        )

        project.runWriteAction {
            copyWithDeleteCall?.let {
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