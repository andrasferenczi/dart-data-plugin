package andrasferenczi.ext

import com.intellij.codeInsight.template.Template

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


// For different editor highlight in case of dividers

fun Template.addNewLine() = this.addTextSegment("\n")

fun Template.addSemicolon() = this.addTextSegment(";")

fun Template.addSpace() = this.addTextSegment(" ")

fun Template.addComma() = this.addTextSegment(",")

