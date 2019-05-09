package andrasferenczi.generator

object DartDataCodeGenerator {

    fun generateConstructor(
        className: String,
        variableNames: List<String>
    ): String {
        return buildString {
            append(className)
            append("({")

            variableNames.forEach {
                append("this.$it,")
                append(System.lineSeparator())
            }

            append("})")
            append(System.lineSeparator())
        }
    }

}