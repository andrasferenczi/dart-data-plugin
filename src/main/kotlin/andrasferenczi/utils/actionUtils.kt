package andrasferenczi.utils

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiFile
import com.jetbrains.lang.dart.psi.DartFile

data class ActionData(
    val project: Project,
    val editor: Editor,
    val dartFile: PsiFile,
    val caret: Caret
)


fun createActionData(event: AnActionEvent): ActionData? {
    val project = event.getData(PlatformDataKeys.PROJECT)

    if (project === null) {
        Messages.showErrorDialog(
            "Project instance could not be retrieved",
            "Internal error"
        )
        return null
    }

    val editor = event.getData(PlatformDataKeys.EDITOR)

    if (editor === null) {
        Messages.showErrorDialog(
            "Editor instance could not be retrieved",
            "Internal error"
        )
        return null
    }


    val dartFile = event.getData(CommonDataKeys.PSI_FILE)

    if (dartFile !is DartFile) {
        Messages.showErrorDialog(
            "Could not cast the given file type to the DartFile class, aborting action.",
            "Dart file not recognized"
        )
        return null
    }

    val caret = event.getData(CommonDataKeys.CARET)

    if (caret == null) {
        Messages.showErrorDialog(
            "The caret was not provided by the environment.",
            "No caret detected"
        )
        return null
    }

    return ActionData(
        project,
        editor,
        dartFile,
        caret
    )
}