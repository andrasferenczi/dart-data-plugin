package andrasferenczi.configuration

import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import java.awt.Insets
import javax.swing.*

data class ConfigurationUIInput(
    val isChecked: Boolean = false
) {
    companion object {
        val TEST_DATA = ConfigurationUIInput()
    }
}

class ConfigurationUIOutput(
    val jComponent: JComponent,

    val copyWithNameTextField: JTextField
)


fun createConfigurationUI(input: ConfigurationUIInput): ConfigurationUIOutput {
    val container = JPanel(
        GridLayoutManager(
            4,
            2,
            Insets(0, 0, 0, 0), 12, 12
        )
    )

    container.add(
        JLabel("copyWith() function name"),
        GridConstraints().apply {
            row = 0
            column = 0
//            fill = GridConstraints.SIZEPOLICY_CAN_GROW
//            vSizePolicy = GridConstraints.SIZEPOLICY_CAN_SHRINK
        }
    )

    val copyWithNameTextField = JTextField("copyWith") // Todo: Get this from input
    container.add(
        copyWithNameTextField,
        GridConstraints().apply {
            //            hSizePolicy = GridConstraints.SIZEPOLICY_WANT_GROW
            fill = GridConstraints.FILL_HORIZONTAL
            anchor = GridConstraints.ANCHOR_WEST
            row = 0
            column = 1
//            vSizePolicy = GridConstraints.SIZEPOLICY_CAN_SHRINK
        }
    )

    val addRequiredToConstructor = JCheckBox("add @required to constructor parameters", true) // Todo: Use input
    container.add(
        addRequiredToConstructor,
        GridConstraints().apply {
            //            hSizePolicy = GridConstraints.SIZEPOLICY_WANT_GROW
            fill = GridConstraints.FILL_HORIZONTAL
            anchor = GridConstraints.ANCHOR_WEST
            row = 1
            column = 0
//            vSizePolicy = GridConstraints.SIZEPOLICY_CAN_SHRINK
        }
    )

    val useNewKeywordConstructorCall = JCheckBox("use the 'new' keyword in constructor calls", true) // Todo: Use input
    container.add(
        useNewKeywordConstructorCall,
        GridConstraints().apply {
            //            hSizePolicy = GridConstraints.SIZEPOLICY_WANT_GROW
            fill = GridConstraints.FILL_HORIZONTAL
            anchor = GridConstraints.ANCHOR_WEST
            row = 2
            column = 0
//            vSizePolicy = GridConstraints.SIZEPOLICY_CAN_SHRINK
        }
    )


    val spacer = JPanel()
    val spacerConstraints = GridConstraints()
    spacerConstraints.row = 3
    spacerConstraints.fill = GridConstraints.FILL_BOTH
    container.add(spacer, spacerConstraints)

    return ConfigurationUIOutput(
        container,
        copyWithNameTextField
    )
}