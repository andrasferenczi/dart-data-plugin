package andrasferenczi.dialog.spread

import andrasferenczi.ext.swing.addIsSelectedChangeListener
import andrasferenczi.ext.swing.addTextChangeListener
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*


object SpreadDialogUtils {

    private fun createGridBagConstraints(gridX: Int, gridY: Int): GridBagConstraints {

        val inset = 5

        return GridBagConstraints().apply {
            this.gridx = gridX
            this.gridy = gridY
            insets = Insets(inset, inset, inset, inset)
        }
    }

    private fun createVariableGrid(manager: SpreadDialogDataManager): Pair<JComponent, List<SpreadDialogVariableAssignment>> {
        val pane = JPanel(GridBagLayout().also {
            it.columnWeights = doubleArrayOf(0.4, 1.0, 4.0, 0.4)
        })

        // Columns
        pane.add(
            JLabel("Generate"),
            createGridBagConstraints(gridX = 0, gridY = 0)
        )

        pane.add(
            JLabel("Argument"),
            createGridBagConstraints(gridX = 1, gridY = 0)
        )

        pane.add(
            JLabel("Value"),
            createGridBagConstraints(gridX = 2, gridY = 0)
        )

        pane.add(
            JLabel("Settable"),
            createGridBagConstraints(gridX = 3, gridY = 0)
        )

        // Rows
        val assignments = manager.data.variables.mapIndexed { rowIndex, variable ->

            val gridY = rowIndex + 1

            val isGeneratedCheckbox = JBCheckBox().also {
                it.addIsSelectedChangeListener { isSelected ->
                    manager.update { setIsGenerated(rowIndex, isSelected) }
                }
            }

            pane.add(
                isGeneratedCheckbox,
                createGridBagConstraints(gridX = 0, gridY = gridY).also {
                    it.anchor = GridBagConstraints.LINE_END
                }
            )

            val functionArgumentNameLabel = JBLabel(variable.functionNamedArgumentName)

            pane.add(
                functionArgumentNameLabel,
                createGridBagConstraints(gridX = 1, gridY = gridY).also {
                    it.anchor = GridBagConstraints.CENTER
                }
            )

            val assignmentTextField = JBTextField(variable.assignedValue).also {
                it.addTextChangeListener { text ->
                    manager.update { setVariableAssignedValue(rowIndex, text) }
                }
            }

            pane.add(
                assignmentTextField,
                createGridBagConstraints(gridX = 2, gridY = gridY).also {
                    it.fill = GridBagConstraints.HORIZONTAL
                }
            )

            val isSelectableCheckbox = JBCheckBox().also {
                it.addIsSelectedChangeListener { isSelected ->
                    manager.update { setIsSettable(rowIndex, isSelected) }
                }
            }

            pane.add(
                isSelectableCheckbox,
                createGridBagConstraints(gridX = 3, gridY = gridY).also {
                    it.anchor = GridBagConstraints.LINE_START
                }
            )

            return@mapIndexed SpreadDialogVariableAssignment(
                generateCheckbox = isGeneratedCheckbox,
                namedArgumentLabel = functionArgumentNameLabel,
                assignmentTextField = assignmentTextField,
                settableCheckbox = isSelectableCheckbox
            )
        }

        return JBScrollPane(pane) to assignments
    }

    private fun createButtonsArray(manager: SpreadDialogDataManager): Pair<JScrollPane, List<JButton>> {
        val panel = JPanel()

        val buttons = manager.data.buttons.mapIndexed { index, originalButtonData ->
            val originalName = originalButtonData.variable.variableHint.name

            JButton(originalName).also {
                it.addActionListener {
                    manager.update { setSelectedValue(this@update.buttons[index]) }
                }
                panel.add(it)
            }
        }

        return JBScrollPane(panel) to buttons
    }

    fun createPanel(manager: SpreadDialogDataManager): Pair<JComponent, SpreadDialogUIElements> {
        val full = JPanel().also {
            it.layout = BoxLayout(it, BoxLayout.Y_AXIS)
        }

        val grid = createVariableGrid(manager)
        full.add(grid.first)

        val buttons = createButtonsArray(manager)
        full.add(buttons.first)

        return full to SpreadDialogUIElements(
            variableAssignments = grid.second,
            argumentButtons = buttons.second
        )
    }

}
