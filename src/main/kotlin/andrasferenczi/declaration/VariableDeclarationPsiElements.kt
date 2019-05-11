package andrasferenczi.declaration

import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.jetbrains.lang.dart.psi.DartComponentName
import com.jetbrains.lang.dart.psi.DartType
import com.jetbrains.lang.dart.psi.DartVarAccessDeclaration
import com.jetbrains.lang.dart.psi.DartVarInit
import java.lang.RuntimeException

// DartVarAccessDeclaration can not be null
// DartType can be null
class VariableDeclarationPsiElements(
    val modifiers: List<LeafPsiElement>,
    // DartType is null if initialized and has a final or const modifier
    val dartType: DartType?,
    val name: DartComponentName,
    val initializer: DartVarInit?
)

fun VariableDeclarationPsiElements.hasModifier(modifier: DeclarationModifier): Boolean {
    return modifiers.find { it.text == modifier.text } !== null
}

val VariableDeclarationPsiElements.hasInitializer: Boolean
    get() = initializer !== null

val VariableDeclarationPsiElements.variableName: String
    get() = name.name ?: throw RuntimeException("Encountered a variable which does not have a name.")

val VariableDeclarationPsiElements.isPrivate: Boolean
    get() = variableName.startsWith("_")

val VariableDeclarationPsiElements.canBeAssignedFromConstructor: Boolean
    get() {
        val isStatic = hasModifier(DeclarationModifier.Static)
        val isFinal = hasModifier(DeclarationModifier.Final)
        val isPrivate = isPrivate

        return !isPrivate && !isStatic && ((isFinal && !hasInitializer) || !isFinal)
    }
