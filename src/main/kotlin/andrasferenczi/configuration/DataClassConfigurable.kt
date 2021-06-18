package andrasferenczi.configuration

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.*


class DataClassConfigurable(
    private val project: Project
) : Configurable {

    private var uiElements: ConfigurationUIElements? = null

    private var lastSavedConfigurationData = ConfigurationDataManager.retrieveData(project)

    private val currentConfigurationData: ConfigurationData?
        get() = uiElements?.extractCurrentConfigurationData()

    override fun isModified(): Boolean {
        return currentConfigurationData != lastSavedConfigurationData
    }

    override fun getDisplayName(): String = "Dart Data Class Plugin"

    override fun apply() {
        val dataToSave = currentConfigurationData ?: throw RuntimeException("No data to save is available")

        ConfigurationDataManager.saveData(project, dataToSave)
        lastSavedConfigurationData = dataToSave.copy()
    }

    override fun createComponent(): JComponent? {
        // This data will be replaced in the reset() call with the right values
        val ui = createConfigurationUI(ConfigurationData.TEST_DATA)
        this.uiElements = ui

        return ui.jComponent
    }

    override fun disposeUIResources() {
        super.disposeUIResources()

        this.uiElements = null
    }

    override fun reset() {
        super.reset()

        this.uiElements?.setFields(lastSavedConfigurationData)
    }
}