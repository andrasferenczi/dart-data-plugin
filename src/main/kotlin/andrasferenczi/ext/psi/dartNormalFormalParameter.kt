package andrasferenczi.ext.psi

import com.jetbrains.lang.dart.psi.DartDefaultFormalNamedParameter
import com.jetbrains.lang.dart.psi.DartNormalFormalParameter

val DartNormalFormalParameter.isNamedParameter: Boolean
    get() = parent is DartDefaultFormalNamedParameter