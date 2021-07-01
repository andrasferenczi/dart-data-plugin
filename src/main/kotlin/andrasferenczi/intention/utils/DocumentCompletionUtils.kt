package andrasferenczi.intention.utils

import andrasferenczi.ext.deleteString
import andrasferenczi.ext.runWriteAction
import andrasferenczi.intention.SpreadAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Changes document in order to get data
 */
object DocumentCompletionUtils {

    /**
     * Creates a dummy text in the code to check the variable fields.
     *
     *  - Writes the name of the variable before the function
     *  - Places a dot after
     *  - Asks the Dart Analysis Server for suggestions
     *
     *  Only works if the written code is not inside another function call (IT'S OK!)
     */
    fun extractVariableInfos(
        variableName: VariableHint,
        document: Document,
        project: Project,
        functionPlacement: FunctionPlacementData,
        getCompletionsForOffset: (offset: Int) -> CompletionData?
    ): List<FieldHint>? {
        val innerVariableCodeHint = variableName.name + "."

        project.runWriteAction(groupId = SpreadAction.SPREAD_ACTION_GROUP_ID) {
            document.insertString(functionPlacement.functionNameStart, innerVariableCodeHint)
        }

        val fieldsCompletions =
            getCompletionsForOffset(functionPlacement.functionNameStart + innerVariableCodeHint.length)

        val fields = fieldsCompletions?.first?.map { it.suggestion }?.extractFieldHints()

        project.runWriteAction(groupId = SpreadAction.SPREAD_ACTION_GROUP_ID) {
            document.deleteString(
                functionPlacement.functionNameStart..
                        (functionPlacement.functionNameStart + innerVariableCodeHint.length)
            )
        }

        return fields
    }


    /**
     * Removes the parameters of the function call and then calls the Dart Analysis Server for hints.
     *
     * ATTENTION!
     * DAS only returns the list of the named arguments, if the function has no positional arguments,
     * or all positional arguments are filled out on the call site.
     */
    fun extractAllFunctionCallParams(
        file: VirtualFile,
        document: Document,
        project: Project,
        functionPlacement: FunctionPlacementData,
        getCompletionsForOffset: (offset: Int) -> CompletionData?
    ): List<NamedArgumentHint>? {
        val deleteRange = functionPlacement.parametersTextRange

        project.runWriteAction(groupId = SpreadAction.SPREAD_ACTION_GROUP_ID) {
            document.deleteString(deleteRange)
        }

        val parametersStart = functionPlacement.parametersTextRange.first
        val paramCompletions = getCompletionsForOffset(parametersStart)

        val namedArgumentsHints = paramCompletions
            ?.first
            ?.map { it.suggestion }
            ?.extractNamedArguments()

        val originalCode = functionPlacement.functionParametersContent
        project.runWriteAction(groupId = SpreadAction.SPREAD_ACTION_GROUP_ID) {
            document.insertString(parametersStart, originalCode)
        }

        return namedArgumentsHints
    }
}
