package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class MapTemplateParams(
    val className: String,
    val variables: List<AliasedVariableTemplateParam>,
    val useNewKeyword: Boolean,
    val addKeyMapper: Boolean,
    val noImplicitCasts: Boolean
)

// The 2 will be generated with the same function
fun createMapTemplate(
    templateManager: TemplateManager,
    params: MapTemplateParams
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

private fun Template.addAssignKeyMapperIfNotValid() {
    addTextSegment(TemplateConstants.KEYMAPPER_VARIABLE_NAME)
    addSpace()
    addTextSegment("??=")
    addSpace()
    withParentheses {
        addTextSegment(TemplateConstants.KEY_VARIABLE_NAME)
    }
    addSpace()
    addTextSegment("=>")
    addSpace()
    addTextSegment(TemplateConstants.KEY_VARIABLE_NAME)
    addSemicolon()
    addNewLine()
    addNewLine()
}

private fun Template.addToMap(params: MapTemplateParams) {
    val (_, variables, _, addKeyMapper, _) = params

    isToReformat = true

    addTextSegment("Map<String, dynamic>")
    addSpace()
    addTextSegment(TemplateConstants.TO_MAP_METHOD_NAME)
    withParentheses {
        if (addKeyMapper) {
            withCurlyBraces {
                addNewLine()
                addTextSegment("String Function(String key)? ${TemplateConstants.KEYMAPPER_VARIABLE_NAME}")
                addComma()
                addNewLine()
            }
        }
    }
    addSpace()
    withCurlyBraces {

        if (addKeyMapper) {
            addAssignKeyMapperIfNotValid()
        }

        addTextSegment("return")
        addSpace()
        withCurlyBraces {
            addNewLine()

            variables.forEach {
                "'${it.mapKeyString}'".also { keyParam ->
                    if (addKeyMapper) {
                        addTextSegment(TemplateConstants.KEYMAPPER_VARIABLE_NAME)
                        withParentheses {
                            addTextSegment(keyParam)
                        }
                    } else {
                        addTextSegment(keyParam)
                    }
                }

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
    params: MapTemplateParams
) {
    val (className, variables, useNewKeyword, addKeyMapper, noImplicitCasts) = params

    isToReformat = true

    addTextSegment("factory")
    addSpace()
    addTextSegment(className)
    addTextSegment(".")
    addTextSegment(TemplateConstants.FROM_MAP_METHOD_NAME)
    withParentheses {
        if (addKeyMapper) {
            addNewLine()
            // New line does not format, no matter what is in this if statement
            addSpace()
        }
        addTextSegment("Map<String, dynamic>")
        addSpace()
        addTextSegment(TemplateConstants.MAP_VARIABLE_NAME)

        if (addKeyMapper) {
            addComma()
            addSpace()
            withCurlyBraces {
                addNewLine()
                addTextSegment("String Function(String ${TemplateConstants.KEY_VARIABLE_NAME})?")
                addSpace()
                addTextSegment(TemplateConstants.KEYMAPPER_VARIABLE_NAME)
                addComma()
                addNewLine()
            }
        }
    }
    addSpace()
    withCurlyBraces {

        if (addKeyMapper) {
            addAssignKeyMapperIfNotValid()
        }

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

                if (it.type == TemplateConstants.DATETIME_TYPE) {
                    addTextSegment(TemplateConstants.DATETIME_TYPE)
                    addDot()
                    addTextSegment(TemplateConstants.TRY_PARSE_METHOD_NAME)
                    withParentheses {
                        addMapSegment(it, addKeyMapper)
                    }
                } else {
                    addMapSegment(it, addKeyMapper)

                    if (noImplicitCasts) {
                        addSpace()
                        addTextSegment("as")
                        addSpace()
                        addTextSegment(it.type)
                    }
                }

                addComma()
                addNewLine()
            }
        }
        addSemicolon()
    }
}

private fun Template.addMapSegment(
    it: AliasedVariableTemplateParam,
    addKeyMapper: Boolean
) {
    addTextSegment(TemplateConstants.MAP_VARIABLE_NAME)
    withBrackets {
        "'${it.mapKeyString}'".also { keyParam ->
            if (addKeyMapper) {
                addTextSegment(TemplateConstants.KEYMAPPER_VARIABLE_NAME)
                withParentheses {
                    addTextSegment(keyParam)
                }
            } else {
                addTextSegment(keyParam)
            }
        }
    }
}
