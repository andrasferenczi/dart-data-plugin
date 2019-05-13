package andrasferenczi.configuration

import andrasferenczi.templater.TemplateConstants

// Input
// Default values
data class ConfigurationData(
    val copyWithMethodName: String,
    val useRequiredAnnotation: Boolean,
    val useNewKeyword: Boolean
) {
    companion object {
        val DEFAULT_DATA = ConfigurationData(
            copyWithMethodName = TemplateConstants.COPYWITH_DEFAULT_METHOD_NAME,
            useRequiredAnnotation = true,
            useNewKeyword = true
        )

        val TEST_DATA = ConfigurationData(
            copyWithMethodName = "testData",
            useRequiredAnnotation = true,
            useNewKeyword = false
        )

    }
}