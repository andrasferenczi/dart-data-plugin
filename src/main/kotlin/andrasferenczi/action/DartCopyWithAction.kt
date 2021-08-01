package andrasferenczi.action

import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction
import andrasferenczi.action.init.ActionData
import andrasferenczi.action.utils.createCopyWithDeleteCall
import andrasferenczi.action.utils.selectFieldsWithDialog
import andrasferenczi.configuration.ConfigurationDataManager
import andrasferenczi.declaration.allMembersFinal
import andrasferenczi.declaration.fullTypeName
import andrasferenczi.declaration.isNullable
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.templater.AliasedVariableTemplateParam
import andrasferenczi.templater.AliasedVariableTemplateParamImpl
import andrasferenczi.templater.CopyWithTemplateParams
import andrasferenczi.templater.createCopyWithConstructorTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.lang.dart.psi.DartClassDefinition

class DartCopyWithAction : BaseAnAction() {

    override fun processAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    ): PerformAction? {
        val declarations = selectFieldsWithDialog(actionData.project, dartClass) ?: return null

        return Companion.processAction(
            GenerationData(actionData, dartClass, declarations)
        )
    }

    companion object : StaticActionProcessor {

        override fun processAction(generationData: GenerationData): PerformAction {
            val (actionData, dartClass, declarations) = generationData

            val (project, _, _, _) = actionData

            val variableNames: List<AliasedVariableTemplateParam> = declarations
                .map {
                    AliasedVariableTemplateParamImpl(
                        variableName = it.variableName,
                        type = it.fullTypeName
                            ?: throw RuntimeException("No type is available - this variable should not be assignable from constructor"),
                        publicVariableName = it.publicVariableName,
                        isNullable = it.isNullable
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
                    generateOptimizedCopy = generateOptimizedCopy,
                    nullSafety = configuration.nullSafety
                )
            )

            val copyWithDeleteCall = createCopyWithDeleteCall(
                dartClass,
                configuration.copyWithMethodName
            )

            return PerformAction(
                copyWithDeleteCall,
                template
            )
        }

    }
}