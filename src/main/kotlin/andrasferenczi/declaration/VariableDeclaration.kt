package andrasferenczi.declaration

data class VariableDeclaration(
    val modifiers: Set<DeclarationModifier>,
    val variableName: String,
    // The type could be
    val hasInitializer: Boolean
    // val variableType: String
)

fun VariableDeclaration.hasModifier(modifier: DeclarationModifier): Boolean {
    return modifier in modifiers
}
