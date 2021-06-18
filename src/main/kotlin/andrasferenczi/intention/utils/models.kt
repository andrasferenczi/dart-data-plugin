package andrasferenczi.intention.utils

import org.dartlang.analysis.server.protocol.IncludedSuggestionRelevanceTag
import org.dartlang.analysis.server.protocol.IncludedSuggestionSet
import java.lang.RuntimeException

// Todo: Field types as well
enum class VariableKind(val visibleName: String) {
    Field("Field"),
    Parameter("Parameter"),
    LocalVariable("Local Variable");

    companion object {

        fun fromElementKind(kind: DartServerCompletionUtils.SuggestionElementKind): VariableKind? =
            tryFromElementKind(kind) ?: throw RuntimeException("Could not convert $kind to variable type.")

        fun tryFromElementKind(kind: DartServerCompletionUtils.SuggestionElementKind): VariableKind? {
            return when (kind) {
                DartServerCompletionUtils.SuggestionElementKind.Field -> Field
                DartServerCompletionUtils.SuggestionElementKind.Parameter -> Parameter
                DartServerCompletionUtils.SuggestionElementKind.LocalVariable -> LocalVariable
                else -> null
            }
        }
    }
}

data class CallCompletionData(
    val variables: List<HierarchicalVariableHint>,
    val arguments: List<NamedArgumentHint>
)

data class HierarchicalVariableHint(
    val variableHint: VariableHint,
    val fieldHints: List<FieldHint>
)

data class VariableHint(
    val name: String,
    val orderIndex: Int,
    val returnType: String,
    val kind: VariableKind
)

data class FieldHint(
    val name: String,
    val returnType: String
)

data class NamedArgumentHint(
    val name: String,
    val returnType: String
)

/**
 * All values are calculated in offsets
 */
data class FunctionPlacementData(
    val functionNameStart: Int,
    val openBracketStart: Int,
    val closingBracketStart: Int,
    // For being able to restore the content if deleted
    // String subsequence uses different indexing from the offset
    val functionParametersContent: String
) {
    val closingBracketEnd = closingBracketStart + 1

    val parametersTextRange = openBracketStart + 1..closingBracketStart
}

data class CompletionLibraryRefData(
    val includedSet: IncludedSuggestionSet,
    val includedKinds: Set<String>,
    val includedRelevanceTags: Map<String, IncludedSuggestionRelevanceTag>
)
