package andrasferenczi.dialog.spread

import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JLabel
import javax.swing.JTextField

class SpreadDialogVariableAssignment(
    val generateCheckbox: JCheckBox,
    val namedArgumentLabel: JLabel,
    val assignmentTextField: JTextField,
    val settableCheckbox: JCheckBox
) {

    fun updateFromFields(data: SpreadDialogVariableData): SpreadDialogVariableData {
        return data.copy(
            isGenerated = data.isGenerated,
            // named argument does not change
            assignedValue = assignmentTextField.text,
            isSettable = settableCheckbox.isSelected
        )
    }

    fun setFields(data: SpreadDialogVariableData) {
        // Stopping endless loops with ifs
        if (generateCheckbox.isSelected != data.isGenerated) {
            generateCheckbox.isSelected = data.isGenerated
        }
        if (namedArgumentLabel.text != data.functionNamedArgumentName) {
            namedArgumentLabel.text = data.functionNamedArgumentName
        }
        if (assignmentTextField.text != data.assignedValue) {
            assignmentTextField.text = data.assignedValue
        }
        if (settableCheckbox.isSelected != data.isSettable) {
            settableCheckbox.isSelected = data.isSettable
        }
    }

}

class SpreadDialogUIElements(
    val variableAssignments: List<SpreadDialogVariableAssignment>,
    val argumentButtons: List<JButton>
) {

    fun updateFromFields(
        data: SpreadDialogUIData
    ): SpreadDialogUIData {
        assert(data.variables.size == variableAssignments.size)
        assert(data.buttons.size == argumentButtons.size)

        val newVariables = data.variables.mapIndexed { index, value ->
            val assignment = variableAssignments[index]
            assignment.updateFromFields(value)
        }

        return data.copy(
            variables = newVariables
        )
    }

    fun setFields(
        data: SpreadDialogUIData
    ) {
        assert(data.variables.size == variableAssignments.size)
        assert(data.buttons.size == argumentButtons.size)

        variableAssignments.forEachIndexed { index, assignment ->
            val value = data.variables[index]
            assignment.setFields(value)
        }
    }

}
