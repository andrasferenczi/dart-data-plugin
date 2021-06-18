package andrasferenczi.ext.swing

import java.awt.event.ItemEvent
import javax.swing.JCheckBox

fun JCheckBox.addIsSelectedChangeListener(
    listener: (isSelected: Boolean) -> Unit
) {
    addItemListener {
        val isSelected = it.stateChange == ItemEvent.SELECTED
        listener(isSelected)
    }
}
