package andrasferenczi.action.init

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import com.jetbrains.lang.dart.psi.DartFile


fun tryCreateActionData(
    event: AnActionEvent,
    feedbackOnError: Boolean = true
): ActionData? {
    val project = event.getData(PlatformDataKeys.PROJECT)

    if (project === null) {
        if (feedbackOnError) {
            Messages.showErrorDialog(
                "Project instance could not be retrieved",
                "Internal error"
            )
        }
        return null
    }

    val editor = event.getData(PlatformDataKeys.EDITOR)

    if (editor === null) {
        if (feedbackOnError) {
            Messages.showErrorDialog(
                "Editor instance could not be retrieved",
                "Internal error"
            )
        }
        return null
    }


    val dartFile = event.getData(CommonDataKeys.PSI_FILE)

    if (dartFile !is DartFile) {
        if (feedbackOnError) {
            Messages.showErrorDialog(
                "Could not cast the given file type to the DartFile class, aborting action.",
                "Dart file not recognized"
            )
        }
        return null
    }

    val caret = event.getData(CommonDataKeys.CARET)

    if (caret == null) {
        if (feedbackOnError) {
            Messages.showErrorDialog(
                "The caret was not provided by the environment.",
                "No caret detected"
            )
        }
        return null
    }

    return ActionData(
        project,
        editor,
        dartFile,
        caret
    )
}