package andrasferenczi.intention.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService
import org.dartlang.analysis.server.protocol.CompletionSuggestion
import org.dartlang.analysis.server.protocol.IncludedSuggestionRelevanceTag
import org.dartlang.analysis.server.protocol.IncludedSuggestionSet

typealias CompletionData = Pair<List<CompletionSuggestionData>, List<CompletionLibraryRefData>>

object DartServerCompletionUtils {

    enum class SuggestionKinds(val kind: String) {
        NamedArgument("NAMED_ARGUMENT"),
        KeyWord("KEYWORD"),
        Invocation("INVOCATION")
    }

    enum class SuggestionElementKind(val kind: String) {
        Constructor("CONSTRUCTOR"),
        LocalVariable("LOCAL_VARIABLE"),
        Parameter("PARAMETER"),
        Field("FIELD"),
        Class("CLASS");


        companion object {
            val textToKind = values().associateBy { it.kind }
        }
    }

    fun getCompletions(
        project: Project,
        file: VirtualFile,
        offset: Int
    ): CompletionData? {
        val das = DartAnalysisServerService.getInstance(project)
        das.updateFilesContent()

        val suggestionsId = das.completion_getSuggestions(file, offset) ?: return null

        val suggestionResult: MutableList<CompletionSuggestionData> = ArrayList()
        val completionRefResult: MutableList<CompletionLibraryRefData> = ArrayList()

        das.addCompletions(file, suggestionsId,
            { replacementOffset: Int,
              replacementLength: Int,
              suggestion: CompletionSuggestion ->
                suggestionResult += CompletionSuggestionData(
                    replacementOffset = replacementOffset,
                    replacementLength = replacementLength,
                    suggestion = suggestion
                )
            },
            { includedSet: IncludedSuggestionSet,
              includedKinds: MutableSet<String>,
              includedRelevanceTags: MutableMap<String, IncludedSuggestionRelevanceTag> ->
                completionRefResult += CompletionLibraryRefData(
                    includedSet = includedSet,
                    includedKinds = includedKinds,
                    includedRelevanceTags = includedRelevanceTags
                )
            }
        )

        return suggestionResult to completionRefResult
    }

}
