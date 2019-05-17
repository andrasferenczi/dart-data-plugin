package andrasferenczi.ext

import andrasferenczi.DartFileNotWellFormattedException
import andrasferenczi.ext.psi.body
import andrasferenczi.ext.psi.mySiblings
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.jetbrains.lang.dart.psi.DartClass

fun Editor.evalAnchorInClass(dartClass: DartClass): PsiElement {
    val caretOffset = caretModel.offset
    val body = dartClass.body ?: throw DartFileNotWellFormattedException("No Class body found for the dart class")

    val lastValidChild = body
        // Not the allChildren function - not all leaf nodes count
        .children
        .lastOrNull { it.textRange.startOffset > caretOffset }

    return lastValidChild
        ?.mySiblings()
        ?.filter { it is PsiWhiteSpace || it.text == ";" }
        ?.lastOrNull()
        ?: body
}

fun Editor.setCaretSafe(dartClass: DartClass, offset: Int) {
    val body = dartClass.body

    if (body == null) {
        caretModel.moveToOffset(offset)
    } else {
        val bodyRange = body.textRange
        caretModel.moveToOffset(
            if (bodyRange.containsOffset(offset)) offset
            else bodyRange.endOffset
        )
    }

}
