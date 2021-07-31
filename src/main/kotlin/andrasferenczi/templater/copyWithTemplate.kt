package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class CopyWithTemplateParams(
    val className: String,
    val variables: List<AliasedVariableTemplateParam>,
    val copyWithMethodName: String,
    val useNewKeyword: Boolean,
    val generateOptimizedCopy: Boolean,
    val nullSafety: Boolean
)

fun createCopyWithConstructorTemplate(
    templateManager: TemplateManager,
    params: CopyWithTemplateParams
): Template {

    val (className, variables, copyWithMethodName, useNewKeyword, generateOptimizedCopy) = params

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
                        if (params.nullSafety) {
                            addTextSegment("?")
                        }
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

            if (generateOptimizedCopy) {
                addTextSegment("if")
                addSpace()
                withParentheses {

                    if(variables.isEmpty()) {
                        // Always return if empty class
                        addTextSegment("true")
                    }

                    variables.forEachIndexed { index, variable ->
                        withParentheses {
                            addTextSegment(variable.publicVariableName)
                            addSpace()
                            addTextSegment("==")
                            addSpace()
                            addTextSegment("null")
                            addSpace()
                            addTextSegment("||")
                            addSpace()
                            addTextSegment("identical")
                            withParentheses {
                                addTextSegment(variable.publicVariableName)
                                addComma()
                                addTextSegment("this.")
                                addTextSegment(variable.variableName)
                            }
                        }

                        if (index != variables.lastIndex) {
                            addSpace()
                            addTextSegment("&&")
                            addNewLine()
                        }
                    }
                }

                withCurlyBraces {
                    addNewLine()
                    addTextSegment("return")
                    addSpace()
                    addTextSegment("this")
                    addSemicolon()
                    addNewLine()
                }
                addNewLine()
                addNewLine()
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