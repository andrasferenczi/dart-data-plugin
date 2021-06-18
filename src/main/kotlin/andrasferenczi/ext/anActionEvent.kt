package andrasferenczi.ext

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.lang.dart.psi.DartClass

val AnActionEvent.editorAndPsiFile: Pair<Editor?, PsiFile?>
    get() {
        getData(CommonDataKeys.PROJECT) ?: return null to null

        val editor = getData(CommonDataKeys.EDITOR)
        val psiFile = getData(CommonDataKeys.PSI_FILE)
        return editor to psiFile
    }

fun AnActionEvent.isInDartFile(): Boolean {
    val (editor, psiFile) = editorAndPsiFile

    if (editor == null || psiFile === null) {
        return false
    }

    if (psiFile.name.substringAfterLast(".") != "dart") {
        return false
    }

    return true
}

fun AnActionEvent.extractOuterDartClass(): DartClass? {
    val (editor, psiFile) = editorAndPsiFile
    // Todo: Maybe not return early when editor is null
    if (editor == null || psiFile === null) {
        return null
    }

    if (psiFile.name.substringAfterLast(".") != "dart") {
        return null
    }

    val element = psiFile.findElementAt(editor.caretModel.offset)
    return PsiTreeUtil.getParentOfType(element, DartClass::class.java)
}
