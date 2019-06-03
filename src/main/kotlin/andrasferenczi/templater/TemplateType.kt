package andrasferenczi.templater

enum class TemplateType(
    val templateKey: String
) {
    NamedParameterConstructor("named_parameter_constructor"),
    CopyWithMethod("copy_with__method"),
    MapTemplate("to_map__from_map"),
    Combined("combined"),

    ToString("to_string"),
    Equals("equals"),
    HashCode("hashcode"),

    Comment("comment")
}