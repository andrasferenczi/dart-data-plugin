package andrasferenczi.templater

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
    val variableNames: List<VariableTemplateParam>
)

fun createCopyWithConstructorTemplate(
    templateManager: TemplateManager,
    params: CopyWithTemplateParams
): Template {

    val (className, variableNames) = params

    return templateManager.createTemplate(
        TemplateType.CopyWithMethod.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        isToReformat = true

        addTextSegment(className)
        addTextSegment(" ")
        addTextSegment(TemplateConstants.COPYWITH_METHOD_NAME)
        addTextSegment("({\n")

        variableNames.forEach {
            addTextSegment(it.type)
            addTextSegment(" ")
            addTextSegment(it.functionInputName)
            addTextSegment(",\n")
        }

        addTextSegment("}) {\n")

        addTextSegment("return")
        addTextSegment(" ")
        addTextSegment("new")
        addTextSegment(" ")
        addTextSegment(className)
        addTextSegment("(\n")

        variableNames.forEach {
            addTextSegment(it.namedConstructorParamName)
            addTextSegment(": ")
            addTextSegment(it.functionInputName)
            addTextSegment(" ?? ")
            addTextSegment("this.")
            addTextSegment(it.thisAccessName)
            addTextSegment(",\n")
        }

        addTextSegment(");")

        addTextSegment("}")

        // Formatting ...
        addTextSegment(" ")
    }

}