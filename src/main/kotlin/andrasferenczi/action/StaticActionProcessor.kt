package andrasferenczi.action

import andrasferenczi.action.data.GenerationData
import andrasferenczi.action.data.PerformAction

interface StaticActionProcessor {

    fun processAction(generationData: GenerationData): PerformAction?

}
