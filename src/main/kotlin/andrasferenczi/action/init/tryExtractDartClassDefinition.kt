package andrasferenczi.action.init

import andrasferenczi.ext.psi.findParentClassDefinition
import com.intellij.openapi.ui.Messages
import com.jetbrains.lang.dart.psi.DartClassDefinition

fun tryExtractDartClassDefinition(
    actionData: ActionData,
    feedbackOnError: Boolean = true
): DartClassDefinition? {
    val currentElement = actionData.dartFile.findElementAt(actionData.caret.caretModel.offset)

    if (currentElement == null) {
        if (feedbackOnError) {
            Messages.showErrorDialog(
                "No element was found at the caret.",
                "No element found"
            )
        }

        return null
    }

    val dartClassBody = currentElement.findParentClassDefinition()

    if (dartClassBody == null) {
        if (feedbackOnError) {
            Messages.showErrorDialog(
                "The caret has to be placed inside the class in which the code generator should run",
                "Caret is not inside the class"
            )
        }

        return null
    }

    return dartClassBody
}