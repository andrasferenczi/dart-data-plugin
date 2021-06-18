package andrasferenczi.dialog

import andrasferenczi.declaration.VariableDeclaration
import andrasferenczi.declaration.canBeAssignedFromConstructor
import com.intellij.ide.util.DefaultPsiElementCellRenderer
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.psi.PsiElement
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import javax.swing.JComponent
import javax.swing.JPanel

class GenerateDialog(
    project: Project,
    private val declarations: List<VariableDeclaration>
) : DialogWrapper(project) {

    private val fieldsCollection: CollectionListModel<PsiElement>
    private val fieldsComponent: LabeledComponent<JPanel>

    private val fieldList: JBList<PsiElement>

    init {
        title = "Select Fields - Dart Dataclass Generation"

        fieldsCollection = CollectionListModel(
            declarations.map { it.name }
        )


        fieldList = JBList(fieldsCollection).apply {
            cellRenderer = DefaultPsiElementCellRenderer()
            // Preselect the elements
            selectedIndices = declarations
                .withIndex()
                .filter { it.value.canBeAssignedFromConstructor }
                .map { it.index }
                .toIntArray()
        }

        val decorator = ToolbarDecorator.createDecorator(fieldList)
            .disableAddAction()
        val panel = decorator.createPanel()

        fieldsComponent = LabeledComponent.create(
            panel,
            "Fields to include (default selected fields can only be assigned from constructor)"
        )

        // Called after entire ui is ready
        init()
    }

    private fun extractSelectedIndices(): Set<Int> {
        return this.fieldList.selectedIndices.toSet()
    }

    override fun createCenterPanel(): JComponent? {
        return fieldsComponent
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return fieldList
    }

    fun getSelectedFields(): List<VariableDeclaration> {
        val selectedIndices = extractSelectedIndices()
        return this.declarations.filterIndexed { index, _ -> index in selectedIndices }
    }
}
