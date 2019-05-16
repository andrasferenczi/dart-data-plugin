package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class JsonTemplateParams(
    val className: String,
    val variables: List<AliasedVariableTemplateParam>,
    val useNewKeyword: Boolean
)

// The 2 will be generated with the same function
fun createMapTemplate(
    templateManager: TemplateManager,
    params: JsonTemplateParams
): Template {

    return templateManager.createTemplate(
        TemplateType.MapTemplate.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        addToMap(params)
        addNewLine()
        addNewLine()
        addFromMap(params)
    }
}

private fun Template.addToMap(params: JsonTemplateParams) {
    val (_, variables, _) = params

    isToReformat = true

    addTextSegment("Map<String, dynamic>")
    addSpace()
    addTextSegment(TemplateConstants.TO_MAP_METHOD_NAME)
    addTextSegment("()")
    addSpace()
    withCurlyBraces {
        addTextSegment("return")
        addSpace()
        withCurlyBraces {
            addNewLine()

            variables.forEach {
                addTextSegment("'${it.mapKeyString}'")
                addTextSegment(":")
                addSpace()
                addTextSegment("this.")
                addTextSegment(it.variableName)
                addComma()
                addNewLine()
            }
        }
        addSemicolon()
    }
}

private fun Template.addFromMap(
    params: JsonTemplateParams
) {
    val (className, variables, useNewKeyword) = params

    isToReformat = true

    addTextSegment("factory")
    addSpace()
    addTextSegment(className)
    addTextSegment(".")
    addTextSegment(TemplateConstants.FROM_MAP_METHOD_NAME)
    withParentheses {
        addTextSegment("Map<String, dynamic>")
        addSpace()
        addTextSegment(TemplateConstants.MAP_VARIABLE_NAME)
    }
    addSpace()
    withCurlyBraces {
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
                addTextSegment(it.publicVariableName)
                addTextSegment(":")
                addSpace()
                addTextSegment(TemplateConstants.MAP_VARIABLE_NAME)
                addTextSegment("['${it.mapKeyString}']")
                addComma()
                addNewLine()
            }
        }
        addSemicolon()
    }
}
