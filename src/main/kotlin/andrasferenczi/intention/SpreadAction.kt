package andrasferenczi.intention

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.init.tryCreateActionData
import andrasferenczi.constants.Constants
import andrasferenczi.dialog.spread.SelectionResult
import andrasferenczi.dialog.spread.SpreadDialog
import andrasferenczi.dialog.spread.toFunctionInnerText
import andrasferenczi.ext.deleteString
import andrasferenczi.ext.isInDartFile
import andrasferenczi.ext.runWriteAction
import andrasferenczi.intention.utils.*
import andrasferenczi.intention.utils.DartServerCompletionUtils.getCompletions
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project

class SpreadAction : AnAction() {

    override fun startInTransaction(): Boolean = true

    // Calculation seems to be a lot, but it has a lot of early checks and seems fast enough
    private fun isEventVisibleFastCalculation(e: AnActionEvent): Boolean {
        val isInDartFile by lazy { e.isInDartFile() }
        val isInFunction by lazy {
            SpreadActionFunctionUtils.extractFunctionPlacementData(e) != null
        }

        return isInDartFile && isInFunction
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = isEventVisibleFastCalculation(e)
    }

    private fun performActionPrivate(actionData: ActionData) {
        val functionPlacement =
            SpreadActionFunctionUtils.extractFunctionPlacementData(actionData.dartFile, actionData.caret) ?: return

        val project = actionData.project
        val virtualFile = actionData.dartFile.virtualFile
        val document = FileDocumentManager.getInstance().getDocument(virtualFile)
            ?: throw RuntimeException("Document could not be extracted.")

        val getCompletionsForOffset =
            { offset: Int -> getCompletions(project, virtualFile, offset) }

        // Place the caret at the beginning and ask for suggestions in regards to the parameters
        val functionBeginCompletions = getCompletionsForOffset(functionPlacement.functionNameStart)
            ?: throw RuntimeException("No completions found for function")

        val variableHints = functionBeginCompletions.first.map { it.suggestion }.extractVariableHints()

        /** For all variable hints write *name*. to and ask for the server analyzer to collect all values needed **/
        // All variables in 1 depth
        val variables = variableHints.map {
            HierarchicalVariableHint(
                it,
                DocumentCompletionUtils.extractVariableInfos(
                    variableName = it,
                    document = document,
                    project = project,
                    functionPlacement = functionPlacement,
                    getCompletionsForOffset = getCompletionsForOffset
                ) ?: throw RuntimeException("No completion could be extract for variable $it")
            )
        }

        /** Clear the function params and ask for help there to list all expected arguments **/

        val callParams = DocumentCompletionUtils.extractAllFunctionCallParams(
            file = virtualFile,
            document = document,
            project = project,
            functionPlacement = functionPlacement,
            getCompletionsForOffset = getCompletionsForOffset
        ) ?: throw RuntimeException("No call parameters could be extracted.")

        /** Match the params with each other, setting up a priority **/
        val callCompletionData = CallCompletionData(
            variables = variables,
            arguments = callParams
        )

        val selection = selectFieldsWithDialog(project, callCompletionData)
        // No problem, nothing is selected
            ?: return

        val text = selection.toFunctionInnerText()

        project.runWriteAction(groupId = SPREAD_ACTION_GROUP_ID) {
            // If there is calling values, delete it
            // - the action probably could not have been triggered
            document.deleteString(functionPlacement.parametersTextRange)
            document.insertString(functionPlacement.parametersTextRange.first, text)
        }
    }

    private fun selectFieldsWithDialog(
        project: Project,
        callCompletionData: CallCompletionData
    ): SelectionResult? {
        val dialog = SpreadDialog(project, callCompletionData)
        dialog.show()

        return if (!dialog.isOK)
            null
        else
            dialog.getDialogResult()
    }

    override fun actionPerformed(event: AnActionEvent) {
        val actionData = tryCreateActionData(event) ?: return

        try {
            performActionPrivate(actionData)
        } catch (e: Exception) {
            e.printStackTrace()

            Notifications.Bus.notify(
                Notification(
                    Constants.NOTIFICATION_ID,
                    "Internal error",
                    "Error occurred: ${e.message}",
                    NotificationType.ERROR
                )
            )

        }

        println("Performing action")
    }

    companion object {

        /**
         * To collect all temporary changes into the same group for a single undo
         */
        val SPREAD_ACTION_GROUP_ID = "andrasferenczi.spread_action"

    }
}
