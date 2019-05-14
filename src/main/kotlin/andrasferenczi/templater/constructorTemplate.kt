package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class ConstructorTemplateParams(
    val className: String,
    val publicVariableNames: List<String>,
    val addRequiredAnnotation: Boolean
)

fun createConstructorTemplate(
    templateManager: TemplateManager,
    params: ConstructorTemplateParams
): Template {
    val (className, publicVariableNames, addRequiredAnnotation) = params

    return templateManager.createTemplate(
        TemplateType.NamedParameterConstructor.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment(className)
        withParentheses {
            if (publicVariableNames.isNotEmpty()) {
                withCurlyBraces {
                    addNewLine()

                    publicVariableNames.forEach {
                        if (addRequiredAnnotation) {
                            addTextSegment("@required")
                            addSpace()
                        }

                        addTextSegment("this.")
                        addTextSegment(it)
                        addComma()
                        addNewLine()
                    }
                }
            }
        }

        addSemicolon()

        // Line break helps formatting
        addSpace()
    }
}