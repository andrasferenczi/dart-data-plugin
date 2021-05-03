package andrasferenczi.ext

import andrasferenczi.templater.TemplateConstants
import andrasferenczi.templater.TemplateType
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

fun TemplateManager.createSeparatorTemplate(newLineCount: Int = 2): Template {
    return createDartTemplate(TemplateType.Comment)
        .apply {
            repeat(newLineCount) {
                addNewLine()
            }
        }
}

/**
 * Same template group will be formatted differently
 *
 * (There was a problem when comments used different template group - the text overflew with the code on occasion
 */
fun TemplateManager.createDartTemplate(templateType: TemplateType): Template {
    return createTemplate(templateType.templateKey, TemplateConstants.DART_TEMPLATE_GROUP)
        .apply {
            isToReformat = true
        }
}

