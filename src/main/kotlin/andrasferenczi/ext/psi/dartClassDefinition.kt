package andrasferenczi.ext.psi

import andrasferenczi.DartFileNotWellFormattedException
import com.intellij.psi.PsiElement
import com.jetbrains.lang.dart.psi.DartClassDefinition
import com.jetbrains.lang.dart.psi.DartComponentName
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

fun PsiElement.findParentClassDefinition(): DartClassDefinition? =
    findFirstParentOfType()

fun DartClassDefinition.extractClassName(): String =
    allChildren()
        .filterIsInstance<DartComponentName>()
        .firstOrNull()
        ?.text
        ?: throw DartFileNotWellFormattedException("Dart class definition does not have a class name")

fun DartClassDefinition.listMethods(): Sequence<DartMethodDeclaration> = findChildrenByType()

// Only this can find the equals method with the ==
// This also looks for operators
fun DartClassDefinition.findMethodsByName(name: String): Sequence<DartMethodDeclaration> =
    listMethods().filter { it.name == name }
