package andrasferenczi.generator

object DartDataCodeGenerator {

    fun generateConstructor(
        className: String,
        variableNames: List<String>
    ): String {
        return buildString {
            append("\n")
            append(className)
            append("({")
            append("\n")

            variableNames.forEach {
                append("this.$it,")
                append("\n")
            }

            append("});")
            append("\n")
        }
    }

}