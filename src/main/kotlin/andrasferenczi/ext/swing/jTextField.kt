package andrasferenczi.ext.swing

import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

fun JTextField.addTextChangeListener(onTextChanged: (text: String) -> Unit) {
    this.document.addDocumentListener(
        object: DocumentListener {

            override fun changedUpdate(e: DocumentEvent?) {
                update()
            }

            override fun insertUpdate(e: DocumentEvent?) {
                update()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                update()
            }

            fun update() {
                val text = this@addTextChangeListener.text
                onTextChanged(text)
            }
        }
    )
}
