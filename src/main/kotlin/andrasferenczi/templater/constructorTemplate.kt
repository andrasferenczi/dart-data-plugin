package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class ConstructorTemplateParams(
    val className: String,
    val publicVariables: List<PublicVariableTemplateParam>,
    val privateVariables: List<AliasedVariableTemplateParam>,
    val addRequiredAnnotation: Boolean
)

fun createConstructorTemplate(
    templateManager: TemplateManager,
    params: ConstructorTemplateParams
): Template {
    val (className, publicVariableNames, privateVariables, addRequiredAnnotation) = params

    return templateManager.createTemplate(
        TemplateType.NamedParameterConstructor.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment(className)
        withParentheses {
            if (publicVariableNames.isNotEmpty() || privateVariables.isNotEmpty()) {
                withCurlyBraces {
                    addNewLine()

                    publicVariableNames.forEach {
                        if (addRequiredAnnotation) {
                            addTextSegment("@required")
                            addSpace()
                        }

                        addTextSegment("this.")
                        addTextSegment(it.variableName)
                        addComma()
                        addNewLine()
                    }

                    privateVariables.forEach {
                        if (addRequiredAnnotation) {
                            addTextSegment("@required")
                            addSpace()
                        }

                        // No this
                        addTextSegment(it.type)
                        addSpace()
                        addTextSegment(it.publicVariableName)
                        addComma()
                        addNewLine()
                    }
                }
            }
        }

        if (privateVariables.isNotEmpty()) {
            addSpace()
            addTextSegment(":")
            addSpace()

            privateVariables.forEachIndexed { index, it ->
                addTextSegment(it.variableName)
                addSpace()
                addTextSegment("=")
                addSpace()
                // Same as used for function input
                addTextSegment(it.publicVariableName)

                if (index != privateVariables.lastIndex) {
                    addComma()
                }
            }
        }

        addSemicolon()

        // Line break helps formatting
        addSpace()
    }
}