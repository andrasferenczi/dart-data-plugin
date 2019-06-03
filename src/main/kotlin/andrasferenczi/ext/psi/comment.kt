package andrasferenczi.ext.psi

import com.intellij.psi.PsiComment
import com.jetbrains.lang.dart.psi.DartClassBody
import com.jetbrains.lang.dart.psi.DartClassMembers


val PsiComment.commentContent
    get() = text.substringAfter("//")

val PsiComment.isTopClassLevel
    get() = parent is DartClassBody || parent is DartClassMembers