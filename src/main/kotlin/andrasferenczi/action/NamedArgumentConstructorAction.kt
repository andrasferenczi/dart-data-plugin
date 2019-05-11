package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.declaration.DeclarationExtractor
import andrasferenczi.declaration.canBeAssignedFromConstructor
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.evalAnchorInClass
import andrasferenczi.ext.extractOuterDartClass
import andrasferenczi.ext.psi.hasMethodWithName
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.psi.findChildrenByType
import andrasferenczi.ext.runWriteAction
import andrasferenczi.ext.setCaretSafe
import andrasferenczi.templater.ConstructorTemplateParams
import andrasferenczi.templater.createConstructorTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
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

        val anchor = editor.evalAnchorInClass(dartClass)

        project.runWriteAction {
            editor.setCaretSafe(dartClass, anchor.textRange.endOffset)
            templateManager.startTemplate(editor, template)
        }
    }
}