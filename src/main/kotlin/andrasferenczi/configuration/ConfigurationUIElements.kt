package andrasferenczi.configuration

import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JTextField

// Output
class ConfigurationUIElements(
    val jComponent: JComponent,

    val copyWithNameTextField: JTextField,
    val useRequiredAnnotationCheckBox: JCheckBox,
    val useNewKeywordCheckbox: JCheckBox,
    val useConstKeywordForConstructorCheckbox: JCheckBox,
    val optimizeConstCopyCheckbox: JCheckBox,
    val addKeyMapperForMapCheckbox: JCheckBox
) {


    fun extractCurrentConfigurationData() : ConfigurationData {
        return ConfigurationData(
            copyWithMethodName = copyWithNameTextField.text,
            useRequiredAnnotation = useRequiredAnnotationCheckBox.isSelected,
            useNewKeyword = useNewKeywordCheckbox.isSelected,
            useConstForConstructor = useConstKeywordForConstructorCheckbox.isSelected,
            optimizeConstCopy = optimizeConstCopyCheckbox.isSelected,
            addKeyMapperForMap = addKeyMapperForMapCheckbox.isSelected
        )
    }

    fun setFields(configurationData: ConfigurationData) {
        copyWithNameTextField.text = configurationData.copyWithMethodName
        useRequiredAnnotationCheckBox.isSelected = configurationData.useRequiredAnnotation
        useNewKeywordCheckbox.isSelected = configurationData.useNewKeyword
        useConstKeywordForConstructorCheckbox.isSelected = configurationData.useConstForConstructor
        optimizeConstCopyCheckbox.isSelected = configurationData.optimizeConstCopy
        addKeyMapperForMapCheckbox.isSelected = configurationData.addKeyMapperForMap
    }

}