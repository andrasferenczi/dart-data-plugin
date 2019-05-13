package andrasferenczi.templater

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
        addTextSegment("({\n")
        // Todo: Start / End variable ?

        publicVariableNames.forEach {
            if(addRequiredAnnotation) {
                addTextSegment("@required")
                addTextSegment(" ")
            }

            addTextSegment("this.")
            addTextSegment(it)
            addTextSegment(",\n")
        }


        addTextSegment("});")

        // Line break helps formatting
        addTextSegment(" ")
    }
}