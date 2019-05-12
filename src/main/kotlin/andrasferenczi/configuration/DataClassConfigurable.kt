package andrasferenczi.configuration

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import java.awt.Insets
import javax.swing.*


class DataClassConfigurable(
    // private val project: Project
) : Configurable {

    var copyWithNameTextField: JTextField? = null

    // TODO
    override fun isModified(): Boolean = false

    override fun getDisplayName(): String = "Dart Dataclass Plugin"

    override fun apply() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createComponent(): JComponent? {
        val ui = createConfigurationUI(ConfigurationUIInput.TEST_DATA)

        return ui.jComponent
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