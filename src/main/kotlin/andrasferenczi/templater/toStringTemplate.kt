package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class ToStringTemplateParams(
    val className: String,
    val variables: List<NamedVariableTemplateParam>
)

fun createToStringTemplate(
    templateManager: TemplateManager,
    params: ToStringTemplateParams
): Template {
    val (className, variables) = params

    return templateManager.createTemplate(
        TemplateType.ToString.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment("@override")
        addNewLine()
        addTextSegment("String")
        addSpace()
        addTextSegment(TemplateConstants.TO_STRING_METHOD_NAME)
        withParentheses { }
        addSpace()
        withCurlyBraces {
            addNewLine()
            addTextSegment("return")
            addSpace()
            withSingleQuotes {
                addTextSegment(className)

                // Cannot use with, since they are closed in different order
                addTextSegment("{")
            }

            addSpace()
            addTextSegment("+")
            addNewLine()

            variables.forEach { variable ->
                withSingleQuotes {
                    addSpace()
                    addTextSegment(variable.variableName)
                    addTextSegment(":")
                    addSpace()
                    addTextSegment("\$")
                    addTextSegment(variable.variableName)
                    addComma()
                }

                addSpace()
                addTextSegment("+")
                addNewLine()
            }

            withSingleQuotes {
                addTextSegment("}")
            }

            addSemicolon()
        }

        addSpace()
    }

}
