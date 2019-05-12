package andrasferenczi.action.utils

import andrasferenczi.ext.psi.findNormalFormalParameters
import andrasferenczi.ext.psi.isNamedParameter
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

enum class DartConstructorType {
    NormalParameterOnly,
    NamedParameterOnly,
    Mixed,
    NoParams;
}

/**
 * No named constructors
 */
class DartConstructorInfo(
    val psi: DartMethodDeclaration,
    val constructorType: DartConstructorType
)

fun List<DartConstructorInfo>.deleteAllPsiElements() = forEach { it.psi.delete() }

private fun DartMethodDeclaration.determineConstructorType(): DartConstructorType {
    val parameters = findNormalFormalParameters().toList()

    if (parameters.isEmpty()) {
        return DartConstructorType.NoParams
    }

    val namedParameterCount = parameters.count { it.isNamedParameter }

    return when (namedParameterCount) {
        parameters.size -> DartConstructorType.NamedParameterOnly
        0 -> DartConstructorType.NormalParameterOnly
        else -> DartConstructorType.Mixed
    }
}

fun DartClass.extractMethodConstructorInfos(): List<DartConstructorInfo> {
    return constructors
        .filterIsInstance<DartMethodDeclaration>()
        .map {
            DartConstructorInfo(
                it, it.determineConstructorType()
            )
        }
}


