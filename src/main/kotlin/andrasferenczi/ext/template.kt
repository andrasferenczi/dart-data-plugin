package andrasferenczi.ext

import andrasferenczi.templater.TemplateConstants
import andrasferenczi.templater.TemplateType
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager

inline fun Template.withParentheses(action: Template.() -> Unit) {
    this.addTextSegment("(")
    this.action()
    this.addTextSegment(")")
}

inline fun Template.withCurlyBraces(action: Template.() -> Unit) {
    this.addTextSegment("{")
    this.action()
    this.addTextSegment("}")
}

inline fun Template.withBrackets(action: Template.() -> Unit) {
    this.addTextSegment("[")
    this.action()
    this.addTextSegment("]")
}

inline fun Template.withSingleQuotes(action: Template.() -> Unit) {
    this.addTextSegment("'")
    this.action()
    this.addTextSegment("'")
}


// For different editor highlight in case of dividers

fun Template.addNewLine() = this.addTextSegment("\n")

fun Template.addSemicolon() = this.addTextSegment(";")

fun Template.addSpace() = this.addTextSegment(" ")

fun Template.addComma() = this.addTextSegment(",")

fun Template.addDot() = this.addTextSegment(".")

@Deprecated(
    "If 2 templates are combined, the anchor will not be placed at the right position. " +
            "Currently it seems like a new line before the first might solve the issue, " +
            "but no one knows what happens with more complicated examples."
)
fun List<Template>.combineTemplates(
    templateManager: TemplateManager,
    separator: Template.() -> Unit = { (0..2).map { this.addNewLine() } }
): Template {
    return templateManager.createTemplate(
        TemplateType.Combined.templateKey,
        TemplateConstants.DART_TEMPLATE_GROUP
    ).apply {
        this@combineTemplates.map {
            this.addTextSegment(it.templateText)
            this.separator()
        }
    }

}