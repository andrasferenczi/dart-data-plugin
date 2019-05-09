package andrasferenczi.declaration

//import java.lang.RuntimeException
//
//data class VariableDeclaration(
//    val modifiers: Set<DeclarationModifier>,
//    val variableName: String,
//    // The type could be specified, but it not important for this purpose now
//    // If a variable is initialized, it should have either type or const/final
//    // in the latter case the value can not be modified
//    val hasInitializer: Boolean
//    // val variableType: String
//) {
//    companion object
//}
//
//fun VariableDeclaration.hasModifier(modifier: DeclarationModifier): Boolean {
//    return modifier in modifiers
//}
//
//val VariableDeclaration.canBeAssignedFromConstructor: Boolean
//    get() {
//        val isStatic = DeclarationModifier.Static in modifiers
//        val isFinal = DeclarationModifier.Final in modifiers
//
//        return !isStatic && ((isFinal && !hasInitializer) || !isFinal )
//    }
//
//fun VariableDeclaration.Companion.fromPsi(elements: VariableDeclarationPsiElements): VariableDeclaration {
//    return VariableDeclaration(
//        modifiers = elements.modifiers
//            .map { DeclarationModifier.textMap[it.text] }
//            .filterNotNull()
//            .toSet(),
//        variableName = elements.name.name ?: throw RuntimeException("Element has no name " + elements.name),
//        hasInitializer = elements.initializer !== null
//    )
//}