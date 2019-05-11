package andrasferenczi.ext.psi

import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartClassDefinition
import com.jetbrains.lang.dart.psi.DartClassMembers
import com.jetbrains.lang.dart.psi.DartMixinDeclaration

val DartClass.body: DartClassMembers?
    get() {
        val body = when (this) {
            is DartClassDefinition -> classBody
            is DartMixinDeclaration -> classBody
            else -> null
        }

        return body?.classMembers
    }