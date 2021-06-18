package andrasferenczi.action.init

import andrasferenczi.constants.Constants
import andrasferenczi.ext.psi.findParentClassDefinition
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.editor.Caret
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.jetbrains.lang.dart.psi.DartClassDefinition

fun extractCurrentReference(
    actionData: ActionData,
    offsetRelativeToCaret: Int = 0,
    feedbackOnError: Boolean = true
): PsiReference? {
    val currentReference: PsiReference? =
        actionData.dartFile.findReferenceAt(actionData.caret.caretModel.offset + offsetRelativeToCaret)

    if (currentReference == null) {
        if (feedbackOnError) {
            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "No reference found",
                    "No reference was found at the caret.",
                    NotificationType.ERROR
                )
            )
        }

        return null
    }

    return currentReference
}

fun extractCurrentElement(
    actionData: ActionData,
    offsetRelativeToCaret: Int = 0,
    feedbackOnError: Boolean = true
): PsiElement? =
    extractCurrentElement(
        actionData.dartFile,
        actionData.caret,
        offsetRelativeToCaret,
        feedbackOnError
    )

fun extractCurrentElement(
    file: PsiFile,
    caret: Caret,
    offsetRelativeToCaret: Int = 0,
    feedbackOnError: Boolean = true
): PsiElement? {
    val currentElement: PsiElement? =
        file.findElementAt(caret.caretModel.offset + offsetRelativeToCaret)

    if (currentElement == null) {
        if (feedbackOnError) {
            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "No element found",
                    "No element was found at the caret.",
                    NotificationType.ERROR
                )
            )
        }

        return null
    }

    return currentElement
}

fun tryExtractDartClassDefinition(
    actionData: ActionData,
    feedbackOnError: Boolean = true
): DartClassDefinition? {
    val currentElement = extractCurrentElement(actionData, feedbackOnError = feedbackOnError) ?: return null

    val dartClassBody = currentElement.findParentClassDefinition()

    if (dartClassBody == null) {
        if (feedbackOnError) {
            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "Caret is not inside the class",
                    "The caret has to be placed inside the class in which the code generator should run",
                    NotificationType.ERROR
                )
            )
        }

        return null
    }

    return dartClassBody
}
