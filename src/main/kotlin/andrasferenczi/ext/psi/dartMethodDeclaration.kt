package andrasferenczi.ext.psi

import com.jetbrains.lang.dart.psi.DartDefaultFormalNamedParameter
import com.jetbrains.lang.dart.psi.DartMethodDeclaration
import com.jetbrains.lang.dart.psi.DartNormalFormalParameter

fun DartMethodDeclaration.findNormalFormalParameters(): Sequence<DartNormalFormalParameter> = findChildrenByType()

val DartNormalFormalParameter.isNamedParameter: Boolean
    get() = parent is DartDefaultFormalNamedParameter
