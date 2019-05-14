package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

// Extend later for private variables
data class VariableTemplateParam(
    // Full type name, like Map<String, int>
    // (no imports needed, since this should be already in the class)
    val type: String,
    // Input name for the function
    val functionInputName: String,
    // By the name it can be accessed with this.__name__
    val thisAccessName: String
) {
    // The name the named constructor has
    // Use the same name for now
    val namedConstructorParamName: String
        get() = functionInputName
}

data class CopyWithTemplateParams(
    val className: String,
    val variableNames: List<VariableTemplateParam>,
    val copyWithMethodName: String,
    val useNewKeyword: Boolean
)

fun createCopyWithConstructorTemplate(
    templateManager: TemplateManager,
    params: CopyWithTemplateParams
): Template {

    val (className, variableNames, copyWithMethodName, useNewKeyword) = params

    return templateManager.createTemplate(
        TemplateType.CopyWithMethod.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment(className)
        addSpace()
        addTextSegment(copyWithMethodName)

        withParentheses {
            if (variableNames.isNotEmpty()) {
                withCurlyBraces {
                    addNewLine()

                    variableNames.forEach {
                        addTextSegment(it.type)
                        addTextSegment(" ")
                        addTextSegment(it.functionInputName)
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

                variableNames.forEach {
                    addTextSegment(it.namedConstructorParamName)
                    addTextSegment(":")
                    addSpace()
                    addTextSegment(it.functionInputName)
                    addSpace()
                    addTextSegment("??")
                    addSpace()
                    addTextSegment("this.")
                    addTextSegment(it.thisAccessName)
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