package andrasferenczi.action.utils

import andrasferenczi.utils.mergeCalls
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.jetbrains.lang.dart.psi.DartClass

fun createConstructorDeleteCallWithUserPrompt(
    project: Project,
    dartClass: DartClass
): (() -> Unit)? {
    val additionalCalls: MutableList<() -> Unit> = ArrayList()

    val existingInfos = dartClass.extractMethodConstructorInfos()
    if (existingInfos.isNotEmpty()) {
        val (namedConstructorInfos, otherConstructorInfos) = existingInfos.partition { it.constructorType == DartConstructorType.NamedParameterOnly }

        additionalCalls += { namedConstructorInfos.deleteAllPsiElements() }

        if (otherConstructorInfos.isNotEmpty()) {
            val shouldDeleteInt = Messages.showOkCancelDialog(
                project,
                "Constructors already exist. Do you want to the delete the existing constructors to prevent conflicts?",
                "Conflicting Constructors",
                "Delete",
                "Keep",
                null
            )

            val wantsToDelete = shouldDeleteInt == Messages.OK

            if (wantsToDelete) {
                additionalCalls += { existingInfos.deleteAllPsiElements() }
            }
        }
    }

    return additionalCalls.mergeCalls()
}