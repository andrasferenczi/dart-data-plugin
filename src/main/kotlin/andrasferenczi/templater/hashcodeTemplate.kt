package andrasferenczi.templater

import andrasferenczi.ext.*
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

data class HashCodeTemplateParams(
    val variables: List<NamedVariableTemplateParam>
)

fun createHashCodeTemplate(
    templateManager: TemplateManager,
    params: HashCodeTemplateParams
): Template {

    val (variables) = params

    return templateManager.createDartTemplate(TemplateType.HashCode)
        .apply {
            addTextSegment("@override")
            addNewLine()
            addTextSegment("int")
            addSpace()
            addTextSegment("get")
            addSpace()
            addTextSegment(TemplateConstants.HASHCODE_NAME)
            addSpace()
            addTextSegment("=>")
            addNewLine()

            if(variables.isEmpty()) {
                // Just a 0
                addTextSegment("0")
            }

            variables.forEachIndexed { index, variable ->
                addTextSegment(variable.variableName)
                addDot()
                addTextSegment(TemplateConstants.HASHCODE_NAME)

                if (index != variables.lastIndex) {
                    addSpace()
                    addTextSegment("^")
                    addNewLine()
                }
            }
            addSemicolon()
            addSpace()
        }

}