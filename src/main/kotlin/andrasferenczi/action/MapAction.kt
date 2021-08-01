package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction
import andrasferenczi.action.utils.createMapDeleteCall
import andrasferenczi.action.utils.selectFieldsWithDialog
import andrasferenczi.configuration.ConfigurationDataManager
import andrasferenczi.declaration.fullTypeName
import andrasferenczi.declaration.isNullable
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.templater.AliasedVariableTemplateParam
import andrasferenczi.templater.AliasedVariableTemplateParamImpl
import andrasferenczi.templater.MapTemplateParams
import andrasferenczi.templater.createMapTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.lang.dart.psi.DartClassDefinition

class MapAction : BaseAnAction() {

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

            val template = createMapTemplate(
                templateManager,
                MapTemplateParams(
                    className = dartClassName,
                    variables = variableNames,
                    useNewKeyword = configuration.useNewKeyword,
                    addKeyMapper = configuration.addKeyMapperForMap,
                    noImplicitCasts = configuration.noImplicitCasts
                )
            )

            val deleteCall = createMapDeleteCall(dartClass)

            return PerformAction(
                deleteCall,
                template
            )
        }

    }
}