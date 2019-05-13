package andrasferenczi.configuration_ui

import andrasferenczi.configuration.ConfigurationData
import andrasferenczi.configuration.createConfigurationUI
import javax.swing.JFrame

/**
 * For faster preview compared to starting the entire IDEA anew
 */
fun main() {
    val component = createConfigurationUI(ConfigurationData.TEST_DATA).jComponent

    // create a basic JFrame
    JFrame.setDefaultLookAndFeelDecorated(true)
    val frame = JFrame("Fast JComponent preview")
    frame.setSize(700, 500)
    frame.setLocation(450, 350)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    // add the JComponent to main frame
    frame.add(component)

    frame.isVisible = true
}