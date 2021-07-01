package andrasferenczi.dialog.spread

interface SpreadDialogDataManager {

    // Getter and setter for always getting fresh data
    var data: SpreadDialogUIData


    fun update(updater: SpreadDialogUIData.() -> SpreadDialogUIData) {
        this.data = this.data.updater()
    }
}

open class SpreadDialogManagerImplementation(
    initialData: SpreadDialogUIData,
    var onUpdateListener: ((data: SpreadDialogUIData) -> Unit)? = null
) : SpreadDialogDataManager {

    override var data: SpreadDialogUIData = initialData
        set(value) {
            field = value
            onUpdateListener?.invoke(value)
        }
}
