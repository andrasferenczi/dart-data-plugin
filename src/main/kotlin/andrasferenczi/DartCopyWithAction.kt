package andrasferenczi

import andrasferenczi.declaration.DeclarationExtractor
import andrasferenczi.utils.findChildrenByType
import andrasferenczi.utils.findFirstParentOfType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement
import com.jetbrains.lang.dart.psi.*

class DartCopyWithAction : AnAction("Dart Data") {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT)
        val dartFile = event.getData(CommonDataKeys.PSI_FILE)

        if (dartFile !is DartFile) {
            Messages.showErrorDialog(
                "Could not cast the given file type to the DartFile class, aborting action.",
                "Dart file not recognized"
            )
            return
        }

        val caret = event.getData(CommonDataKeys.CARET)

        if (caret == null) {
            Messages.showErrorDialog(
                "The caret was not provided by the environment.",
                "No caret detected"
            )
            return
        }

        val currentElement = dartFile.findElementAt(caret.caretModel.offset)
        val dartClassBody = currentElement?.findFirstParentOfType<DartClassDefinition>()
        val declarationsLists = dartClassBody?.findChildrenByType<DartVarDeclarationList>()

//        val values = declarationsLists?.map {
//            it.findChildrenByType<DartTypeImpl>() to it.findChildrenByType<DartComponentName>()
//        }

//        val infos = declarationsLists?.map { extractDeclarationInfo(it) }

        val declarations = DeclarationExtractor.extractDeclarationsFromClass(dartClassBody!!)

        Messages.showMessageDialog(project, "Everything is awesome", "Yeah", Messages.getInformationIcon())
    }

}