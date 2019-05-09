package andrasferenczi.declaration

enum class DeclarationModifier(val text: String) {
    Final("final"),
    Private("private"),
    Const("const"),
    Static("static");

    companion object {
        val textSet: Set<String> = DeclarationModifier
            .values()
            .map { it.text }
            .toSet()


    }
}