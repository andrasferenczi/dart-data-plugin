package andrasferenczi.configuration_ui

import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import javax.swing.JFrame
import java.awt.Insets
import javax.swing.JPanel
import javax.swing.JButton
import java.awt.GridBagConstraints
import java.awt.GridBagLayout


fun showGridBagLayout(shouldFill: Boolean, shouldWeightX: Boolean) {

    val pane = JPanel(GridBagLayout())
//    val c = GridBagConstraints()

//For each component to be added to this container:
//...Create the component...
//...Set instance variables in the GridBagConstraints instance...
//    pane.add(theComponent, c)

    var button: JButton

    pane.setLayout(GridBagLayout())
    val c = GridBagConstraints()
    if (shouldFill) {
        //natural height, maximum width
        c.fill = GridBagConstraints.HORIZONTAL
    }

    button = JButton("Button 1")
    if (shouldWeightX) {
        c.weightx = 0.5
    }
    c.fill = GridBagConstraints.HORIZONTAL
    c.gridx = 0
    c.gridy = 0
    pane.add(button, c)

    button = JButton("Button 2")
    c.fill = GridBagConstraints.HORIZONTAL
    c.weightx = 0.5
    c.gridx = 1
    c.gridy = 0
    pane.add(button, c)

    button = JButton("Button 3")
    c.fill = GridBagConstraints.HORIZONTAL
    c.weightx = 0.5
    c.gridx = 2
    c.gridy = 0
    pane.add(button, c)

    button = JButton("Long-Named Button 4")
    c.fill = GridBagConstraints.HORIZONTAL
    c.ipady = 40      //make this component tall
    c.weightx = 0.0
    c.gridwidth = 3
    c.gridx = 0
    c.gridy = 1
    pane.add(button, c)

    button = JButton("5")
    c.fill = GridBagConstraints.HORIZONTAL
    c.ipady = 0       //reset to default
    c.weighty = 1.0   //request any extra vertical space
    c.anchor = GridBagConstraints.PAGE_END //bottom of space
    c.insets = Insets(10, 0, 0, 0)  //top padding
    c.gridx = 1       //aligned with button 2
    c.gridwidth = 2   //2 columns wide
    c.gridy = 2       //third row
    pane.add(button, c)


    JFrame.setDefaultLookAndFeelDecorated(true)
    val frame = JFrame("Fast JComponent preview")
    frame.setSize(700, 500)
    frame.setLocation(450, 350)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    // add the JComponent to main frame
    frame.add(pane)
    frame.isVisible = true
}

/**
 * For faster preview compared to starting the entire IDEA anew
 */
fun main() {
//    val component = createConfigurationUI(ConfigurationUIInput.TEST_DATA).jComponent
//
//    // create a basic JFrame
//    JFrame.setDefaultLookAndFeelDecorated(true)
//    val frame = JFrame("Fast JComponent preview")
//    frame.setSize(700, 500)
//    frame.setLocation(450, 350)
//    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//
//    // add the JComponent to main frame
//    frame.add(component)
//
//    frame.isVisible = true

    showGridBagLayout(true, true)

//    val frame = JFrame("GridLayout Demo")
//    // create grid layout with 3 rows , 2 columns with horizontal
//    // and vertical gap set to 10
//    val panel = JPanel(
//        GridLayoutManager(
//            6,
//            1,
//            Insets(0, 0, 0, 0), 12, 12
//        )
//    )
//    // add buttons to the panel
//    (0 until 5).forEach {
//        panel.add(
//            JButton("Button $it"),
//            GridConstraints().apply {
//                row = it
//                column = 0
//            }
//        )
//    }
//
//    panel.add(
//        JPanel(),
//        GridConstraints().apply {
//            row = 5
//            fill = GridConstraints.FILL_BOTH
//        }
//    )
//
//    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//    frame.setSize(300, 150)
//    frame.contentPane.add(panel)
//    frame.isVisible = true

}