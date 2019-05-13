package andrasferenczi.configuration

import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JTextField

// Output
class ConfigurationUIElements(
    val jComponent: JComponent,

    val copyWithNameTextField: JTextField,
    val useRequiredAnnotationCheckBox: JCheckBox,
    val useNewKeywordCheckbox: JCheckBox
) {


    fun extractCurrentConfigurationData() : ConfigurationData {
        return ConfigurationData(
            copyWithMethodName = copyWithNameTextField.text,
            useRequiredAnnotation = useRequiredAnnotationCheckBox.isSelected,
            useNewKeyword = useNewKeywordCheckbox.isSelected
        )
    }

    fun setFields(configurationData: ConfigurationData) {
        copyWithNameTextField.text = configurationData.copyWithMethodName
        useRequiredAnnotationCheckBox.isSelected = configurationData.useRequiredAnnotation
        useNewKeywordCheckbox.isSelected = configurationData.useNewKeyword
    }

}