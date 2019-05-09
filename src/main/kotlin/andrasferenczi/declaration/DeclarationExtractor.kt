package andrasferenczi.declaration

import andrasferenczi.DartFileNotWellFormattedException
import andrasferenczi.utils.allChildren
import andrasferenczi.utils.findChildrenByType
import andrasferenczi.utils.iterateDFS
import andrasferenczi.utils.split
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.jetbrains.lang.dart.psi.*
import java.util.*

// ";" is probably not going to occur, it is on another level
private val VARIABLE_DECLARATION_SEPARATORS = setOf(",", ";")

/**
 * Collects the types in order for each declared variable.
 */
private fun DartVarDeclarationList.extractDeclarations(): List<PsiElement> {

    val values: MutableList<PsiElement> = LinkedList()

    iterateDFS {
        // Do not evaluate the type declaration
        // A comma might come earlier (which belongs to the type) than the first name
        // e.g.:
        // Map<String, int> value;
        if (it is DartType) {
            return@iterateDFS false
        }

        val isDeclarationPart =
            it is DartComponentName
                    || it is DartVarInit
                    || (it is LeafPsiElement && it.text in VARIABLE_DECLARATION_SEPARATORS)

        if (isDeclarationPart) {
            values += it
        }

        return@iterateDFS !isDeclarationPart
    }

    return values
}

private fun DartVarDeclarationList.extractDeclarationNameAndInitializer(): List<Pair<DartComponentName, DartVarInit?>> {
    val declarations = extractDeclarations()

    val parts = declarations.split { it is LeafPsiElement && it.text == "," }

    return parts.map { part ->
        val componentName = part.find { it is DartComponentName } as DartComponentName?
            ?: throw DartFileNotWellFormattedException("A variable has no name defined.")

        val varInit = part.find { it is DartVarInit } as DartVarInit?

        return@map componentName to varInit
    }
}

private fun DartVarDeclarationList.extractVarAccessDeclaration(): DartVarAccessDeclaration? {
    // DartVarAccessDeclaration has the type and if the variable is final
    val types = findChildrenByType<DartVarAccessDeclaration>()

    if (types.isEmpty()) {
        return null
    }

    val (type) = types
    return type
}

private fun DartVarAccessDeclaration.extractModifiers(): List<LeafPsiElement> {
    return allChildren()
        .filterIsInstance<LeafPsiElement>()
        .filter { it.text in DeclarationModifier.textSet }
}

private fun DartVarAccessDeclaration.extractType(): DartType? {
    // Can be null, if declared in the following form:
    // final implicitInt = 2;
    return findChildrenByType<DartType>().firstOrNull()
}

private fun DartVarDeclarationList.extractEntireDeclarations(): List<VariableDeclarationPsiElements> {
    val accessDeclaration = extractVarAccessDeclaration()
    // A variable declaration has to exist
    // The declaration contains the modifiers (final / static / const)
    // and without them it can be assumed that the document is not well-formatted
        ?: throw DartFileNotWellFormattedException(
            "No access declaration could be found for a variable. Is the document well-formatted?"
        )

    val modifiers = accessDeclaration.extractModifiers()
    val type = accessDeclaration.extractType()

    val declarations = extractDeclarationNameAndInitializer()

    return declarations.map { (name, initializer) ->
        VariableDeclarationPsiElements(
            modifiers,
            type,
            name,
            initializer
        )
    }
}

object DeclarationExtractor {

    fun extractDeclarationsFromClass(dartClass: DartClassDefinition): List<VariableDeclarationPsiElements> {
        val declarations = dartClass.findChildrenByType<DartVarDeclarationList>()

        return declarations.flatMap { it.extractEntireDeclarations() }
    }

}
