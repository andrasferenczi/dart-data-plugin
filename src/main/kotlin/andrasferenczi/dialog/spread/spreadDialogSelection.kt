package andrasferenczi.dialog.spread

data class Selection(
    val functionParameterName: String,
    val assignedText: String
)

data class SelectionResult(
    val selections: List<Selection>
) : List<Selection> by selections

fun SpreadDialogUIData.toSelectionResult(): SelectionResult {
    return SelectionResult(
        selections = this.variables
            .filter { it.isGenerated }
            .map {
                Selection(
                    it.functionNamedArgumentName,
                    it.assignedValue
                )
            }
    )
}

fun SelectionResult.toFunctionInnerText(): String {
    return selections.joinToString(
        prefix = "\n",
        separator = ",\n",
        postfix = ",\n"
    ) { it.functionParameterName + ": " + it.assignedText }
}
