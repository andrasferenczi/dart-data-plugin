package andrasferenczi.action.utils

import andrasferenczi.templater.TemplateConstants
import com.jetbrains.lang.dart.psi.DartClass

fun createCopyWithDeleteCall(
    dartClass: DartClass
): (() -> Unit)? {

    // TODO: Prompt user for creating the constructor if it does not exist yet

    val copyWithMethod = dartClass.findMethodByName(TemplateConstants.COPYWITH_METHOD_NAME)

    copyWithMethod?.let {
        return { it.delete() }
    }

    return null
}