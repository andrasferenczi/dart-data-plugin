package andrasferenczi.action.utils

import com.jetbrains.lang.dart.psi.DartClass

fun createCopyWithDeleteCall(
    dartClass: DartClass,
    copyWithMethodName: String
): (() -> Unit)? {

    // TODO: Prompt user for creating the constructor if it does not exist yet

    val copyWithMethod = dartClass.findMethodByName(copyWithMethodName)

    copyWithMethod?.let {
        return { it.delete() }
    }

    return null
}