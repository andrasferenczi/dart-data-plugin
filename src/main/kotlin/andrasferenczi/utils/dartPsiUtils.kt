package andrasferenczi.utils

import andrasferenczi.DartFileNotWellFormattedException
import com.intellij.psi.PsiElement
import com.jetbrains.lang.dart.psi.DartClassDefinition
import com.jetbrains.lang.dart.psi.DartComponentName
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

fun PsiElement.findParentClassDefinition() =
    findFirstParentOfType<DartClassDefinition>()

fun DartClassDefinition.extractClassName(): String =
    allChildren()
        .filterIsInstance<DartComponentName>()
        .firstOrNull()
        ?.text
        ?: throw DartFileNotWellFormattedException("Dart class definition does not have a class name")

fun DartClassDefinition.listMethods(): List<DartMethodDeclaration> = findChildrenByType()

fun DartClassDefinition.findMethodsByName(name: String): List<DartMethodDeclaration> =
    listMethods().filter { it.name == name }
