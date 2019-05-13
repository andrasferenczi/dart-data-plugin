package andrasferenczi.configuration

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project

object ConfigurationDataManager {

    fun retrieveData(project: Project): ConfigurationData {
        val properties = PropertiesComponent.getInstance(project)

        var configuration = ConfigurationData.DEFAULT_DATA.copy()

        properties.getValue(Keys.COPY_WITH_METHOD_NAME)?.let {
            configuration = configuration.copy(copyWithMethodName = it)
        }

        configuration = configuration.copy(
            useRequiredAnnotation = properties.getBoolean(
                Keys.USE_REQUIRED_ANNOTATION,
                configuration.useRequiredAnnotation
            )
        )

        configuration = configuration.copy(
            useNewKeyword = properties.getBoolean(
                Keys.USE_NEW_KEYWORD,
                configuration.useNewKeyword
            )
        )

        return configuration
    }

    fun saveData(project: Project, configurationData: ConfigurationData) {
        val properties = PropertiesComponent.getInstance(project)

        // Note to self: This api is a joke with the default data - wasted an hour why boolean is not set

        properties.setValue(
            Keys.COPY_WITH_METHOD_NAME,
            configurationData.copyWithMethodName,
            ConfigurationData.DEFAULT_DATA.copyWithMethodName
        )
        properties.setValue(
            Keys.USE_REQUIRED_ANNOTATION,
            configurationData.useRequiredAnnotation,
            ConfigurationData.DEFAULT_DATA.useRequiredAnnotation
        )
        properties.setValue(
            Keys.USE_NEW_KEYWORD,
            configurationData.useNewKeyword,
            ConfigurationData.DEFAULT_DATA.useNewKeyword
        )
    }

    private object Keys {
        const val COPY_WITH_METHOD_NAME = "dart-data-class.copy-with-method-name"
        const val USE_REQUIRED_ANNOTATION = "dart-data-class.use-required-annotation"
        const val USE_NEW_KEYWORD = "dart-data-class.use-new-keyword"
    }
}