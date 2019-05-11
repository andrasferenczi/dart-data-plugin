package andrasferenczi.templater

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class ConstructorTemplateParams(
    val className: String,
    val publicVariableNames: List<String>
)

fun createConstructorTemplate(
    templateManager: TemplateManager,
    params: ConstructorTemplateParams
): Template {
    val (className, publicVariableNames) = params

    return templateManager.createTemplate(
        TemplateType.NamedParameterConstructor.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment(className)
        addTextSegment("({\n")
        // Todo: Start / End variable ?

        publicVariableNames.forEach {
            addTextSegment("this.")
            addTextSegment(it)
            addTextSegment(",\n")
        }


        addTextSegment("});")

        // Line break helps formatting
        addTextSegment(" ")
    }
}