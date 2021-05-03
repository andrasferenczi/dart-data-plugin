package andrasferenczi.action.init

import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

data class ActionData(
    val project: Project,
    val editor: Editor,
    val dartFile: PsiFile,
    val caret: Caret
)