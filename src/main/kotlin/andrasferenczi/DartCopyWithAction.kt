package andrasferenczi

import andrasferenczi.declaration.DeclarationExtractor
import andrasferenczi.declaration.canBeAssignedFromConstructor
import andrasferenczi.declaration.variableName
import andrasferenczi.generator.DartDataCodeGenerator
import andrasferenczi.utils.*
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiFileFactory
import com.jetbrains.lang.dart.DartFileTypeFactory
import com.jetbrains.lang.dart.DartLanguage
import com.jetbrains.lang.dart.DartParser
import com.jetbrains.lang.dart.psi.DartClassDefinition
import com.jetbrains.lang.dart.psi.DartComponentName
import com.jetbrains.lang.dart.psi.DartFile
import com.jetbrains.lang.dart.psi.DartVarDeclarationList
import com.jetbrains.lang.dart.util.DartElementGenerator
import groovyjarjarantlr.CodeGenerator
import org.jetbrains.java.generate.element.ElementFactory

class DartCopyWithAction : AnAction("Dart Data") {

    override fun actionPerformed(event: AnActionEvent) {
        // Can return from here, error messages are handled
        val (project, editor, dartFile, caret) = createActionData(event) ?: return

        val currentElement = dartFile.findElementAt(caret.caretModel.offset)

        if (currentElement == null) {
            Messages.showErrorDialog(
                "No element was found at the caret.",
                "No element found"
            )

            return
        }

        val dartClassBody = currentElement.findParentClassDefinition()

        if (dartClassBody == null) {
            Messages.showErrorDialog(
                "The caret has to be placed inside the class in which the code generator should run",
                "Caret is not inside the class"
            )

            return
        }

        val dartClassName = dartClassBody.extractClassName()
        val declarations = DeclarationExtractor
            .extractDeclarationsFromClass(dartClassBody)

        val variableNames = declarations
            .filter { it.canBeAssignedFromConstructor }
            .map { it.variableName }

        val generatedConstructor = DartDataCodeGenerator.generateConstructor(
            dartClassName,
            variableNames
        )

        Messages.showMessageDialog(project, "Everything is awesome", "Yeah", Messages.getInformationIcon())
    }

}