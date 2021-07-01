package andrasferenczi.ext

import com.intellij.openapi.editor.Document

fun Document.deleteString(offset: IntRange) {
    // Is probably inclusive
    this.deleteString(offset.first, offset.last)
}
