package andrasferenczi.action.my

import andrasferenczi.action.StaticActionProcessor
import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction
import andrasferenczi.declaration.isNullable
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.psi.findMethodsByName
import andrasferenczi.templater.EqualsTemplateParams
import andrasferenczi.templater.NamedVariableTemplateParamImpl
import andrasferenczi.templater.TemplateConstants
import andrasferenczi.templater.createEqualsTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class MyEqualsAction {

    companion object : StaticActionProcessor {

        private fun createDeleteCall(dartClass: DartClassDefinition): (() -> Unit)? {
            val equals = dartClass.findMethodsByName(TemplateConstants.EQUALS_OPERATOR_METHOD_NAME)
                .firstOrNull()
                ?: return null

            return { equals.delete() }
        }

        override fun processAction(generationData: GenerationData): PerformAction? {
            val (actionData, dartClass, declarations) = generationData

            val project = actionData.project

            val templateManager = TemplateManager.getInstance(project)
            val dartClassName = dartClass.extractClassName()

            val template = createEqualsTemplate(
                templateManager,
                EqualsTemplateParams(
                    className = dartClassName,
                    variables = declarations.map {
                        NamedVariableTemplateParamImpl(
                            it.variableName,
                            isNullable = it.isNullable
                        )
                    }
                )
            )

            return PerformAction(
                createDeleteCall(dartClass),
                template
            )
        }
    }

}