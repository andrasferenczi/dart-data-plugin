package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.utils.createCopyWithDeleteCall
import andrasferenczi.action.utils.selectFieldsWithDialog
import andrasferenczi.configuration.ConfigurationDataManager
import andrasferenczi.declaration.allMembersFinal
import andrasferenczi.declaration.fullTypeName
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.evalAnchorInClass
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.runWriteAction
import andrasferenczi.ext.setCaretSafe
import andrasferenczi.templater.*
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class DartCopyWithAction : BaseAnAction() {

    override fun performAction(event: AnActionEvent, actionData: ActionData, dartClass: DartClassDefinition) {
        val (project, editor, _, _) = actionData

        val declarations = selectFieldsWithDialog(project, dartClass) ?: return

        val variableNames: List<AliasedVariableTemplateParam> = declarations
            .map {
                AliasedVariableTemplateParamImpl(
                    variableName = it.variableName,
                    type = it.fullTypeName
                        ?: throw RuntimeException("No type is available - this variable should not be assignable from constructor"),
                    publicVariableName = it.publicVariableName
                )
            }

        val templateManager = TemplateManager.getInstance(project)
        val configuration = ConfigurationDataManager.retrieveData(project)
        val dartClassName = dartClass.extractClassName()
        val generateOptimizedCopy = configuration.optimizeConstCopy && declarations.allMembersFinal()

        val template = createCopyWithConstructorTemplate(
            templateManager,
            CopyWithTemplateParams(
                className = dartClassName,
                variables = variableNames,
                copyWithMethodName = configuration.copyWithMethodName,
                useNewKeyword = configuration.useNewKeyword,
                generateOptimizedCopy = generateOptimizedCopy
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