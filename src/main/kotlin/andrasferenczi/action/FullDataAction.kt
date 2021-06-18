package andrasferenczi.action

import andrasferenczi.action.init.ActionData
import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction
import andrasferenczi.action.data.combineAll
import andrasferenczi.action.my.MyEqualsAction
import andrasferenczi.action.my.MyHashCodeAction
import andrasferenczi.action.my.MyToStringAction
import andrasferenczi.action.utils.*
import andrasferenczi.ext.addNewLine
import andrasferenczi.ext.addSpace
import andrasferenczi.ext.createDartTemplate
import andrasferenczi.ext.psi.commentContent
import andrasferenczi.ext.psi.findChildrenByType
import andrasferenczi.ext.psi.isTopClassLevel
import andrasferenczi.templater.TemplateConstants
import andrasferenczi.templater.TemplateType
import andrasferenczi.traversal.TraversalType
import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiComment
import com.jetbrains.lang.dart.psi.DartClassDefinition

class FullDataAction : BaseAnAction() {

    override fun processAction(
        event: AnActionEvent,
        actionData: ActionData,
        dartClass: DartClassDefinition
    ): PerformAction? {
        val project = actionData.project
        val declarations = selectFieldsWithDialog(project, dartClass)
            ?: return null

        val generationData = GenerationData(actionData, dartClass, declarations)

        val templateManager = TemplateManager.getInstance(project)

        val processActions = listOf(
            NamedArgumentConstructorAction.Companion,
            MyEqualsAction.Companion,
            MyHashCodeAction.Companion,
            MyToStringAction.Companion,
            DartCopyWithAction.Companion,
            MapAction.Companion
        )
            .map { it.processAction(generationData) }

        return listOf(
            // TODO
            processCommentBeginAction(dartClass, templateManager),
            *processActions.toTypedArray(),
            processCommentEndAction(templateManager)
        )
            .combineAll()

        // transactionguard
    }

    companion object {

        private fun processCommentBeginAction(
            dartClass: DartClassDefinition,
            templateManager: TemplateManager
        ): PerformAction {
            return PerformAction(
                createDeleteCall(dartClass),
                templateManager.createCommentBeginTemplate()
            )
        }

        private fun processCommentEndAction(
            templateManager: TemplateManager
        ): PerformAction {
            return PerformAction(
                // The begin has already deleted the items
                null,
                templateManager.createCommentEndTemplate()
            )
        }

        private fun createDeleteCall(dartClass: DartClassDefinition): (() -> Unit)? {
            val comments = extractSectionComments(
                dartClass,
                createCommentBeginContent(),
                createCommentEndContent()
            )

            if (comments.isEmpty()) {
                return null
            }

            return { comments.forEach { it.delete() } }
        }

        /**
         * Tries to extract the matching comments and in case of missing comments it returns empty
         */
        private fun extractSectionComments(
            dartClass: DartClassDefinition,
            commentBeginContent: String,
            commentEndContent: String
        ): List<PsiComment> {

            // Breadth does not give the correct order - the end comment might be higher level in the hierarchy
            val comments = dartClass.findChildrenByType<PsiComment>(traversalType = TraversalType.Depth)
                // Top level only
                .filter { it.isTopClassLevel }
                .toList()

            val beginCommentIndex = comments.indexOfFirst { it.commentContent == commentBeginContent }

            val beginComment = comments.getOrNull(beginCommentIndex) ?: return emptyList()

            // End comment will be searched after the first one (the retrieval of these children are in order)
            val endComment = comments
                .subList(beginCommentIndex, comments.size)
                .firstOrNull { it.commentContent == commentEndContent }
                ?: return emptyList()

            return listOf(beginComment, endComment)
        }

        private fun TemplateManager.createCommentBeginTemplate(): Template {
            return createDartTemplate(TemplateType.Comment)
                .apply {
                    addNewLine()
                    addSpace()
                    addSpace()
                    addTextSegment("//")
                    addTextSegment(createCommentBeginContent())
                    addNewLine()
                }
        }

        private fun TemplateManager.createCommentEndTemplate(): Template {
            return createDartTemplate(TemplateType.Comment)
                .apply {
                    addNewLine()
                    addSpace()
                    addSpace()
                    addTextSegment("//")
                    addTextSegment(createCommentEndContent())
                    addNewLine()
                }
        }

        private fun createCommentBeginContent(): String =
            "<editor-fold desc=\"${TemplateConstants.EDITOR_FOLD_DATA_CLASS_NAME}\">"

        private fun createCommentEndContent(): String = "</editor-fold>"

    }
}