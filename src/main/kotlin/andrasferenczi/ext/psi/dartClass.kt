package andrasferenczi.ext.psi

import com.jetbrains.lang.dart.psi.*
import com.jetbrains.lang.dart.util.DartResolveUtil

val DartClass.body: DartClassMembers?
    get() {
        val body = when (this) {
            is DartClassDefinition -> classBody
            is DartMixinDeclaration -> classBody
            else -> null
        }

        return body?.classMembers
    }

fun DartClass.collectClassesAndInterfaces(): Pair<List<DartClass>, List<DartClass>> {
    val superClasses: MutableList<DartClass> = ArrayList()
    val superInterfaces: MutableList<DartClass> = ArrayList()

    DartResolveUtil.collectSupers(superClasses, superInterfaces, this)

    return superClasses to superInterfaces
}
