package andrasferenczi.declaration

import andrasferenczi.DartFileNotWellFormattedException
import andrasferenczi.ext.psi.findChildrenByType
import andrasferenczi.ext.psi.findFirstChildByType
import andrasferenczi.ext.psi.iterateDFS
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
    // This DFS iteration behaves slightly different from the common one...
    // Not all returned truthy values will add the element to the list
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
    val declarations = extractDeclarations().toList()

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
    return findFirstChildByType()
}

/**
 * Todo: This is a slow function, since all elements are checked. Should check only around function declarations.
 */
private fun DartVarAccessDeclaration.extractModifiers(): Sequence<LeafPsiElement> {
    return findChildrenByType<LeafPsiElement>()
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

    val modifiers = accessDeclaration.extractModifiers().toList()
    val type = accessDeclaration.extractType()

    val declarations = extractDeclarationNameAndInitializer()

    return declarations.map { (name, initializer) ->
        VariableDeclarationPsiElementsImpl(
            modifiers,
            type,
            name,
            initializer
        )
    }
}

private val privateToUniquePublicVariableNameTransformations = listOf<(variableName: String) -> String>(
    // Remove starting underscore and make it variable like
    { it.substring(1).decapitalize() },
    // Private
    { "p" + it.substring(1).capitalize() },
    // mVariable for the Android <3
    { "m" + it.substring(1).capitalize() },
    // Unique
    { "u" + it.substring(1).capitalize() },
    { "p_" + it.substring(1).capitalize() },
    { "m_" + it.substring(1).capitalize() },
    { "u_" + it.substring(1).capitalize() }
)

// Function keeps name if not private
// If 2 same-named variables exist, there will be errors anyway in the user's code
private fun createUniquePublicVariableName(variableName: String, existingNames: Set<String>): String {
    if (!isVariableNamePrivate(variableName)) {
        return variableName
    }

    if (variableName.isBlank() || variableName == "_") {
        throw RuntimeException("Variable name $variableName is not a valid name")
    }

    val newVarName = privateToUniquePublicVariableNameTransformations.asSequence()
        .map { it.invoke(variableName) }
        .filter { it !in existingNames }
        .firstOrNull()
        ?: variableName.substring(1)

    // There could have been multiple underscores
    return if (isVariableNamePrivate(newVarName))
        "p$newVarName"
    else
        newVarName
}

object DeclarationExtractor {

    fun extractDeclarationsFromClass(dartClass: DartClassDefinition): List<VariableDeclaration> {
        // Todo: Probably some type of max depth would be useful
        val varDeclarations = dartClass.findChildrenByType<DartVarDeclarationList>().toList()
        val declarations = varDeclarations.flatMap { it.extractEntireDeclarations() }

        val existingNames = declarations.map { it.variableName }.toSet()
        return declarations.map {
            val newName = createUniquePublicVariableName(
                it.variableName,
                existingNames
            )

            return@map VariableDeclaration(
                it,
                PublicNamedVariableImpl(newName)
            )
        }
    }

}
