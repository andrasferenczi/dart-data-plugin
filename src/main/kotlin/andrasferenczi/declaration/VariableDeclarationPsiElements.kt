package andrasferenczi.declaration

import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.jetbrains.lang.dart.psi.DartComponentName
import com.jetbrains.lang.dart.psi.DartType
import com.jetbrains.lang.dart.psi.DartVarAccessDeclaration
import com.jetbrains.lang.dart.psi.DartVarInit

// DartVarAccessDeclaration can not be null
// DartType can be null
class VariableDeclarationPsiElements(
    val modifiers: List<LeafPsiElement>,
    // DartType is null if initialized and has a final or const modifier
    val dartType: DartType?,
    val name: DartComponentName,
    val initializer: DartVarInit?
)
