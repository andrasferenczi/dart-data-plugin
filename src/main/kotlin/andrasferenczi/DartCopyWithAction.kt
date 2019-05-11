package andrasferenczi

import andrasferenczi.declaration.DeclarationExtractor
import andrasferenczi.declaration.canBeAssignedFromConstructor
import andrasferenczi.declaration.variableName
import andrasferenczi.ext.evalAnchorInClass
import andrasferenczi.ext.psi.extractClassName
import andrasferenczi.ext.psi.findParentClassDefinition
import andrasferenczi.ext.runWriteAction
import andrasferenczi.ext.setCaretSafe
import andrasferenczi.generator.DartDataCodeGenerator
import andrasferenczi.templater.ConstructorTemplateParams
import andrasferenczi.templater.createConstructorTemplate
import andrasferenczi.utils.*
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages

class DartCopyWithAction : AnAction("Dart Data") {

    override fun update(e: AnActionEvent) {
        super.update(e)
    }

    override fun actionPerformed(event: AnActionEvent) {
        // TemplateManager.getInstance(event.project).createTemplate()

//        val value: PsiElement? = null

//        MemberChooser()

        // Useful:
        // Use IncorrectOperationException

        // Before invoke to check if class already implements the given functions

        // Before writing the file
        // FileModificationService.getInstance().prepareFileForWrite()

        // Getting the location of the item placement
        // psiElement?.textRange.startOffset

        // Psi Tree Util ...
        // PsiTreeUtil.getParentOfType()

        //   Template      template!!.addVariable()


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


        val templateManager = TemplateManager.getInstance(project)

        val template = createConstructorTemplate(
            templateManager,
            ConstructorTemplateParams(
                className = dartClassName,
                publicVariableNames = variableNames
            )
        )

        val anchor = editor.evalAnchorInClass(dartClassBody)

        project.runWriteAction {
            editor.setCaretSafe(dartClassBody, anchor.textRange.endOffset)
            templateManager.startTemplate(editor, template)
        }

//        PsiFileFactory.getInstance(project)
//            .createFileFromText(Da)

//        WriteCommandAction.runWriteCommandAction(project) {
//            val existingConstructor = dartClassBody.findMethodsByName(dartClassName).firstOrNull()
//
//            // Todo: Not really working
//            val offset = existingConstructor?.calculateGlobalOffset()
//                ?: editor.caretModel.currentCaret.offset
//
//            existingConstructor?.delete()
//
//            editor.document.insertString(offset, generatedConstructor)
//
////            CodeStyleManager.getInstance(project)
////                .reformatText(editor.document.)
////
////            val documentManager = PsiDocumentManager.getInstance(project)
//
//            PsiDocumentManager.getInstance(project)
//                .doPostponedOperationsAndUnblockDocument(editor.document)
//        }

//        PsiElementFactory.SERVICE.getInstance(project).createMethodFromText()

//        Messages.showMessageDialog(project, "Everything is awesome", "Yeah", Messages.getInformationIcon())
    }


}