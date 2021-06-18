package andrasferenczi.dialog.spread

import andrasferenczi.intention.utils.CallCompletionData
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import javax.swing.SwingUtilities

class SpreadDialog(
    project: Project,
    callCompletionData: CallCompletionData
) : DialogWrapper(project) {

    private val manager = SpreadDialogManagerImplementation(
        initialData = callCompletionData.toSpreadDialogUiData()
    )

    private val dialog: JComponent

    init {
        title = "Select variables for assignment"

        val (dialog, ui) = SpreadDialogUtils.createPanel(this.manager)

        manager.onUpdateListener = {
//            SwingUtilities.invokeAndWait {
                ui.setFields(it)
//            }
        }

        this.dialog = dialog

        // Called after entire ui is ready
        init()

        ui.setFields(manager.data)

    }

    override fun createCenterPanel(): JComponent? {
      return this.dialog
    }

    fun getDialogResult(): SelectionResult =
        manager.data.toSelectionResult()
}
