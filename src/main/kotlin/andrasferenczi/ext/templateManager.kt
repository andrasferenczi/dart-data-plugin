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

fun TemplateManager.createDartTemplate(templateType: TemplateType): Template {
    return createTemplate(templateType.templateKey, TemplateConstants.DART_TEMPLATE_GROUP)
        .apply {
            isToReformat = true
        }
}

