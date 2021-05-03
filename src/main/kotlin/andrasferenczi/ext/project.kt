package andrasferenczi.ext

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project

const val WRITE_ACTION_NAME = "andrasferenczi.writeaction"

fun Project.runWriteAction(
    action: () -> Unit
) {
    val runnable = Runnable { ApplicationManager.getApplication().runWriteAction(action) }

    val processor = CommandProcessor.getInstance()
    if (processor.currentCommand === null) {
        processor.executeCommand(this, runnable, WRITE_ACTION_NAME, null)
    } else {
        runnable.run()
    }
}