package andrasferenczi.templater

interface NamedVariableTemplateParam {
    // By the name it can be accessed with this.__name__
    val variableName: String
    val isNullable: Boolean
}

class NamedVariableTemplateParamImpl(
    override val variableName: String,
    override val isNullable: Boolean
) : NamedVariableTemplateParam

interface TypedVariableTemplateParam : NamedVariableTemplateParam {
    // Full type name, like Map<String, int>
    // (no imports needed, since this should be already in the class)
    val type: String
}

interface PrivateNamedVariableTemplateParam {
    // The name of the variable that can be used for constructor parameter
    // (unique among all parameter names and has no starting underscore sign)
    val publicVariableName: String
}

// The usages:
interface PublicVariableTemplateParam : NamedVariableTemplateParam

interface AliasedVariableTemplateParam : TypedVariableTemplateParam, PrivateNamedVariableTemplateParam

// The name the named constructor parameter has - needed when called from a copy/fromJson method
val AliasedVariableTemplateParam.namedConstructorParamName: String
    get() = publicVariableName

// To make the reading and writing from the map consistent
val AliasedVariableTemplateParam.mapKeyString: String
    get() = variableName

// Classes
data class PublicVariableTemplateParamImpl(
    override val variableName: String,
    override val isNullable: Boolean
) : PublicVariableTemplateParam

data class AliasedVariableTemplateParamImpl(
    override val variableName: String,
    override val type: String,
    override val publicVariableName: String,
    override val isNullable: Boolean
) : AliasedVariableTemplateParam