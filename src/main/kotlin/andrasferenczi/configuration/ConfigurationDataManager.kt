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

        configuration = configuration.copy(
            useConstForConstructor = properties.getBoolean(
                Keys.USE_CONST_KEYWORD_FOR_CONSTRUCTOR,
                configuration.useConstForConstructor
            )
        )

        configuration = configuration.copy(
            optimizeConstCopy = properties.getBoolean(
                Keys.OPTIMIZE_CONSTANT_COPY,
                configuration.optimizeConstCopy
            )
        )

        configuration = configuration.copy(
            addKeyMapperForMap = properties.getBoolean(
                Keys.ADD_KEY_MAPPER_FOR_MAP,
                configuration.addKeyMapperForMap
            )
        )

        configuration = configuration.copy(
            noImplicitCasts = properties.getBoolean(
                Keys.NO_IMPLICIT_CASTS,
                configuration.noImplicitCasts
            )
        )

        configuration = configuration.copy(
            nullSafety = properties.getBoolean(
                Keys.NULL_SAFETY,
                configuration.nullSafety
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
        properties.setValue(
            Keys.USE_CONST_KEYWORD_FOR_CONSTRUCTOR,
            configurationData.useConstForConstructor,
            ConfigurationData.DEFAULT_DATA.useConstForConstructor
        )

        properties.setValue(
            Keys.OPTIMIZE_CONSTANT_COPY,
            configurationData.optimizeConstCopy,
            ConfigurationData.DEFAULT_DATA.optimizeConstCopy
        )

        properties.setValue(
            Keys.ADD_KEY_MAPPER_FOR_MAP,
            configurationData.addKeyMapperForMap,
            ConfigurationData.DEFAULT_DATA.addKeyMapperForMap
        )

        properties.setValue(
            Keys.NO_IMPLICIT_CASTS,
            configurationData.noImplicitCasts,
            ConfigurationData.DEFAULT_DATA.noImplicitCasts
        )

        properties.setValue(
            Keys.NULL_SAFETY,
            configurationData.nullSafety,
            ConfigurationData.DEFAULT_DATA.noImplicitCasts
        )
    }

    private object Keys {
        const val COPY_WITH_METHOD_NAME = "dart-data-class.copy-with-method-name"
        const val USE_REQUIRED_ANNOTATION = "dart-data-class.use-required-annotation"
        const val USE_NEW_KEYWORD = "dart-data-class.use-new-keyword"
        const val USE_CONST_KEYWORD_FOR_CONSTRUCTOR = "dart-data-class.use-const-keyword-for-constructor"
        const val OPTIMIZE_CONSTANT_COPY = "dart-data-class.optimize-constant-copy"
        const val ADD_KEY_MAPPER_FOR_MAP = "dart-data-class.add-key-mapper-for-map"
        const val NO_IMPLICIT_CASTS = "data-data-class.no-implicit-casts"
        const val NULL_SAFETY = "data-data-class.null-safety"
    }
}