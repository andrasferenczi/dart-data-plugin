package andrasferenczi.action

import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction
import andrasferenczi.action.init.ActionData
import andrasferenczi.action.utils.createConstructorDeleteCallWithUserPrompt
import andrasferenczi.action.utils.selectFieldsWithDialog
import andrasferenczi.configuration.ConfigurationDataManager
import andrasferenczi.declaration.*
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.templater.*
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.jetbrains.lang.dart.psi.DartClassDefinition

class NamedArgumentConstructorAction : BaseAnAction() {

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

        override fun processAction(generationData: GenerationData): PerformAction? {
            val (actionData, dartClass, declarations) = generationData
            val (project, _, _, _) = actionData

            val publicVariables: List<PublicVariableTemplateParam> = declarations
                .filter { it.isPublic }
                .map {
                    PublicVariableTemplateParamImpl(
                        it.variableName,
                        isNullable = it.isNullable
                    )
                }

            val privateVariables: List<AliasedVariableTemplateParam> = declarations
                .filter { it.isPrivate }
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
            val addConstQualifier = configuration.useConstForConstructor && declarations.allMembersFinal()

            val template = createConstructorTemplate(
                templateManager,
                ConstructorTemplateParams(
                    className = dartClassName,
                    publicVariables = publicVariables,
                    privateVariables = privateVariables,
                    addRequiredAnnotation = configuration.useRequiredAnnotation
                            && !configuration.nullSafety,
                    addConstQualifier = addConstQualifier,
                    nullSafety = configuration.nullSafety
                )
            )

            val constructorDeleteCall = createConstructorDeleteCallWithUserPrompt(project, dartClass)

            return PerformAction(
                constructorDeleteCall,
                template
            )
        }

    }
}