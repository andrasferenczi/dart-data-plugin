package andrasferenczi.configuration

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

// Insets
private const val TOP_INSET = 10
private const val BOTTOM_INSET = 0
private const val RIGHT_INSET = 10

// The texts should be aligned
private const val CHECKBOX_CHECK_AREA_WIDTH = 22
private const val CHECKBOX_LEFT_INSET = 15
private const val NO_CHECKBOX_LEFT_INSET = CHECKBOX_CHECK_AREA_WIDTH + CHECKBOX_LEFT_INSET


fun createConfigurationUI(input: ConfigurationData): ConfigurationUIElements {

    val pane = JPanel(GridBagLayout())

    // ROW 1
    pane.add(
        JLabel("name of the copyWith() method"),
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 0
            insets = Insets(TOP_INSET, NO_CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    val copyWithNameTextField = JTextField(input.copyWithMethodName)
    pane.add(
        copyWithNameTextField,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            weightx = 0.6
            gridx = 1
            gridy = 0
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    val useRequiredAnnotationCheckBox = JCheckBox(
        "add @required to constructor parameters",
        input.useRequiredAnnotation
    )
    // ROW 2
    pane.add(
        useRequiredAnnotationCheckBox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 1
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    val useNewKeywordCheckbox = JCheckBox(
        "use the 'new' keyword",
        input.useNewKeyword
    )
    // ROW 3
    pane.add(
        useNewKeywordCheckbox,
        GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            gridx = 0
            gridy = 2
            insets = Insets(TOP_INSET, CHECKBOX_LEFT_INSET, BOTTOM_INSET, RIGHT_INSET)
        }
    )

    // Remaining space
    pane.add(
        JPanel(),
        GridBagConstraints().apply {
            weighty = 1.0   // request any extra vertical space
            anchor = GridBagConstraints.PAGE_END // bottom of space
            gridy = 3
            gridwidth = 2
        }
    )

    return ConfigurationUIElements(
        pane,
        copyWithNameTextField,
        useRequiredAnnotationCheckBox,
        useNewKeywordCheckbox
    )
}