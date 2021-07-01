package andrasferenczi.spread_ui

import andrasferenczi.dialog.spread.SpreadDialogDataManager
import andrasferenczi.dialog.spread.SpreadDialogManagerImplementation
import andrasferenczi.dialog.spread.SpreadDialogUIData
import andrasferenczi.dialog.spread.SpreadDialogUtils
import javax.swing.JFrame


private class TestManager : SpreadDialogManagerImplementation(
    SpreadDialogUIData.TEST_DATA
) {

    override fun update(updater: SpreadDialogUIData.() -> SpreadDialogUIData) {
        val newData = super.update(updater)

        println("--- Setting")
        println("From: $data")
        println("To: $data")

        return newData
    }

}

/**
 * For faster preview compared to starting the entire IDEA anew
 */
fun main() {
    val manager = TestManager()

    val (component, ui) = SpreadDialogUtils.createPanel(manager)

    manager.onUpdateListener = { ui.setFields(it) }

    // create a basic JFrame
    JFrame.setDefaultLookAndFeelDecorated(true)
    val frame = JFrame("Fast JComponent preview")
    frame.setSize(700, 500)
    frame.setLocation(450, 350)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    // add the JComponent to main frame
    frame.add(component)

    frame.isVisible = true

    ui.setFields(manager.data)
}
