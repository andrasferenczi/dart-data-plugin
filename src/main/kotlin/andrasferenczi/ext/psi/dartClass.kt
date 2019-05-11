package andrasferenczi.ext.psi

import com.jetbrains.lang.dart.psi.*

val DartClass.body: DartClassMembers?
    get() {
        val body = when (this) {
            is DartClassDefinition -> classBody
            is DartMixinDeclaration -> classBody
            else -> null
        }

        return body?.classMembers
    }

fun DartClass.hasMethodWithName(
    methodName: String
): Boolean {
    if (methodName.isBlank()) {
        return false
    }

    TODO("Check what comes in what order in the class hierarchy")

//    val dartClassDefinition = findParentClassDefinition()
//
//    val declarations = dartClassDefinition?.findChildrenByType<DartMethodDeclaration>()
//    if (declarations.isEmpty()) {
//        return false
//    }
//
//    return declarations.find { it.name == methodName } !== null
}