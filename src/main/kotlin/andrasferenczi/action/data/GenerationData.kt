package andrasferenczi.action.data

import andrasferenczi.action.init.ActionData
import andrasferenczi.declaration.VariableDeclaration
import com.jetbrains.lang.dart.psi.DartClassDefinition

data class GenerationData(
    val actionData: ActionData,
    val dartClass: DartClassDefinition,
    val declarations: List<VariableDeclaration>
)