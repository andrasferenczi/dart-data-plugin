package andrasferenczi.configuration

import andrasferenczi.templater.TemplateConstants

// Input
// Default values
data class ConfigurationData constructor(
    val copyWithMethodName: String,
    val useRequiredAnnotation: Boolean,
    val useNewKeyword: Boolean,
    val useConstForConstructor: Boolean,
    val optimizeConstCopy: Boolean,
    val addKeyMapperForMap: Boolean,
    val noImplicitCasts: Boolean,
    val nullSafety: Boolean
) {
    companion object {
        val DEFAULT_DATA = ConfigurationData(
            copyWithMethodName = TemplateConstants.COPYWITH_DEFAULT_METHOD_NAME,
            useRequiredAnnotation = true,
            useNewKeyword = false,
            useConstForConstructor = true,
            optimizeConstCopy = false,
            addKeyMapperForMap = false,
            noImplicitCasts = true,
            nullSafety = true
        )

        val TEST_DATA = DEFAULT_DATA.copy(
            copyWithMethodName = "testData",
            useConstForConstructor = false
        )

    }
}