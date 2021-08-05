package andrasferenczi.action.my

import andrasferenczi.action.StaticActionProcessor
import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction
import andrasferenczi.declaration.isNullable
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.psi.findMethodsByName
import andrasferenczi.templater.NamedVariableTemplateParamImpl
import andrasferenczi.templater.TemplateConstants
import andrasferenczi.templater.ToStringTemplateParams
import andrasferenczi.templater.createToStringTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.jetbrains.lang.dart.psi.DartClassDefinition

class MyToStringAction {
    companion object : StaticActionProcessor {

        private fun createDeleteCall(dartClass: DartClassDefinition): (() -> Unit)? {
            val toString = dartClass.findMethodsByName(TemplateConstants.TO_STRING_METHOD_NAME)
                .firstOrNull()
                ?: return null

            return { toString.delete() }
        }

        override fun processAction(generationData: GenerationData): PerformAction? {
            val (actionData, dartClass, declarations) = generationData

            val project = actionData.project

            val templateManager = TemplateManager.getInstance(project)
            val dartClassName = dartClass.extractClassName()

            val template = createToStringTemplate(
                templateManager = templateManager,
                params = ToStringTemplateParams(
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
