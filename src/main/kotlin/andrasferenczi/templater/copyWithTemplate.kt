package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class CopyWithTemplateParams(
    val className: String,
    val variables: List<AliasedVariableTemplateParam>,
    val copyWithMethodName: String,
    val useNewKeyword: Boolean
)

fun createCopyWithConstructorTemplate(
    templateManager: TemplateManager,
    params: CopyWithTemplateParams
): Template {

    val (className, variables, copyWithMethodName, useNewKeyword) = params

    return templateManager.createTemplate(
        TemplateType.CopyWithMethod.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment(className)
        addSpace()
        addTextSegment(copyWithMethodName)

        withParentheses {
            if (variables.isNotEmpty()) {
                withCurlyBraces {
                    addNewLine()

                    variables.forEach {
                        addTextSegment(it.type)
                        addTextSegment(" ")
                        addTextSegment(it.publicVariableName)
                        addTextSegment(",")
                        addNewLine()
                    }
                }
            }
        }

        withCurlyBraces {
            addNewLine()

            addTextSegment("return")
            addSpace()
            if (useNewKeyword) {
                addTextSegment("new")
                addSpace()
            }
            addTextSegment(className)

            withParentheses {
                addNewLine()

                variables.forEach {
                    addTextSegment(it.namedConstructorParamName)
                    addTextSegment(":")
                    addSpace()
                    addTextSegment(it.publicVariableName)
                    addSpace()
                    addTextSegment("??")
                    addSpace()
                    addTextSegment("this.")
                    addTextSegment(it.variableName)
                    addComma()
                    addNewLine()
                }
            }

            addSemicolon()
        }

        // Formatting ...
        addSpace()
    }

}