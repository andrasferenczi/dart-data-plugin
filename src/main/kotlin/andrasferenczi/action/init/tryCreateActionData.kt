package andrasferenczi.action.init

import andrasferenczi.constants.Constants
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.jetbrains.lang.dart.psi.DartFile


fun tryCreateActionData(
    event: AnActionEvent,
    feedbackOnError: Boolean = true
): ActionData? {
    val project = event.getData(PlatformDataKeys.PROJECT)

    if (project === null) {
        if (feedbackOnError) {
            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "Internal error",
                    "Project instance could not be retrieved",
                    NotificationType.ERROR
                )
            )
        }
        return null
    }

    val editor = event.getData(PlatformDataKeys.EDITOR)

    if (editor === null) {
        if (feedbackOnError) {
            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "Internal error",
                    "Editor instance could not be retrieved",
                    NotificationType.ERROR
                )
            )
        }
        return null
    }


    val dartFile = event.getData(CommonDataKeys.PSI_FILE)

    if (dartFile !is DartFile) {
        if (feedbackOnError) {
            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "Dart file not recognized",
                    "Could not cast the given file type to the DartFile class, aborting action.",
                    NotificationType.ERROR
                )
            )
        }
        return null
    }

    val caret = event.getData(CommonDataKeys.CARET)

    if (caret == null) {
        if (feedbackOnError) {
            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "No caret detected",
                    "The caret was not provided by the environment.",
                    NotificationType.ERROR
                )
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
