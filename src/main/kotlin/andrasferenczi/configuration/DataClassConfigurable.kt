package andrasferenczi.configuration

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent
import com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH
import javax.swing.JPanel
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
import com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST
import com.sun.javafx.scene.control.behavior.CellBehaviorBase.setAnchor
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import javax.swing.JTextField
import com.sun.javafx.scene.CameraHelper.project
import java.awt.Insets
import javax.swing.JLabel
import com.intellij.ide.util.PropertiesComponent


class DataClassConfigurable(
    private val project: Project
) : Configurable {

    lateinit var pathTextField: JTextField

    override fun isModified(): Boolean = false

    override fun getDisplayName(): String = "This is a custom test componetn"

    override fun apply() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createComponent(): JComponent? {
        pathTextField = JTextField()

        val fieldWithButton = TextFieldWithBrowseButton(pathTextField)
        fieldWithButton.addBrowseFolderListener(
            "Select Hook Script", "", null,
            FileChooserDescriptorFactory.createSingleFileDescriptor()
        )

        val container = JPanel(GridLayoutManager(2, 2, Insets(0, 0, 0, 0), 12, 12))

        val pathLabelConstraint = GridConstraints()
        pathLabelConstraint.row = 0
        pathLabelConstraint.column = 0
        pathLabelConstraint.fill = GridConstraints.FILL_HORIZONTAL
        pathLabelConstraint.vSizePolicy = GridConstraints.SIZEPOLICY_CAN_SHRINK
        container.add(JLabel("Script path"), pathLabelConstraint)

        val pathFieldConstraint = GridConstraints()
        pathFieldConstraint.hSizePolicy = GridConstraints.SIZEPOLICY_WANT_GROW
        pathFieldConstraint.fill = GridConstraints.FILL_HORIZONTAL
        pathFieldConstraint.anchor = GridConstraints.ANCHOR_WEST
        pathFieldConstraint.row = 0
        pathFieldConstraint.column = 1
        pathFieldConstraint.vSizePolicy = GridConstraints.SIZEPOLICY_CAN_SHRINK
        container.add(fieldWithButton, pathFieldConstraint)

        val spacer = JPanel()
        val spacerConstraints = GridConstraints()
        spacerConstraints.row = 1
        spacerConstraints.fill = GridConstraints.FILL_BOTH
        container.add(spacer, spacerConstraints)

        return container
    }

    override fun disposeUIResources() {
        super.disposeUIResources()
    }

    override fun reset() {
        super.reset()
    }

//    @NotNull
//    fun getScriptPath(project: Project): String? {
//        val properties = PropertiesComponent.getInstance(project)
//        var path = properties.getValue(PATH_KEY)
//
//        if (path == null || path.trim { it <= ' ' }.isEmpty()) {
//            path = DEFAULT_FILE
//        } else {
//            path = path.trim { it <= ' ' }
//        }
//
//        return path
//    }
}