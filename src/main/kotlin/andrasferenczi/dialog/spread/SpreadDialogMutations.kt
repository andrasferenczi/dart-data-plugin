package andrasferenczi.dialog.spread

/**
 * 'Clicks' all buttons to set initial values for the fields,
 * so that the fields won't be empty.
 *
 * Last to First
 */
fun SpreadDialogUIData.applyAllFieldsOrdered(): SpreadDialogUIData {
    return this.buttons
        .reversed()
        .fold(this) { data, button ->
        data.setSelectedValue(button)
    }
}

// ---

fun SpreadDialogUIData.setSelectedValue(buttonData: SpreadDialogButtonData): SpreadDialogUIData {
    return copy(
        variables = this.variables.setSelectedValues(buttonData)
    )
}

fun SpreadDialogUIData.setVariableAssignedValue(index: Int, assignedValue: String): SpreadDialogUIData {
    return copyWithVariable(index) { it.copy(assignedValue = assignedValue) }
}

fun SpreadDialogUIData.setIsGenerated(index: Int, isGenerated: Boolean): SpreadDialogUIData {
    return copyWithVariable(index) { it.copy(isGenerated = isGenerated) }
}

fun SpreadDialogUIData.setIsSettable(index: Int, isSettable: Boolean): SpreadDialogUIData {
    return copyWithVariable(index) { it.copy(isSettable = isSettable) }
}

// ---

private fun SpreadDialogUIData.copyWithVariable(
    index: Int,
    map: (data: SpreadDialogVariableData) -> SpreadDialogVariableData
): SpreadDialogUIData {
    return copy(
        variables = variables.toMutableList().also { it[index] = map(it[index]) }
    )
}

private fun List<SpreadDialogVariableData>.setSelectedValues(
    buttonData: SpreadDialogButtonData
): List<SpreadDialogVariableData> {
    return map { it.setSelectedValue(buttonData) }
}

// Later this will be regex
private fun areNamesEqual(namedArgumentName: String, variableName: String): Boolean {
    return namedArgumentName == variableName
}

private fun SpreadDialogVariableData.setSelectedValue(buttonData: SpreadDialogButtonData): SpreadDialogVariableData {
    if (!isSettable) {
        return this
    }

    val variable = buttonData.variable

    val variableHint = variable.variableHint
    if (areNamesEqual(this.functionNamedArgumentName, variableHint.name)) {
        return copy(
            assignedValue = variableHint.name
        )
    }

    val fieldHints = buttonData.variable.fieldHints
    fieldHints
        .firstOrNull { areNamesEqual(this.functionNamedArgumentName, it.name) }
        ?.let {
            return copy(
                assignedValue = variableHint.name + "." + it.name
            )
        }
        ?: return this
}
