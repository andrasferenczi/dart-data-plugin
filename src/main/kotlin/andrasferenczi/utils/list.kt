package andrasferenczi.utils

/**
 * Takes the first item where the lambda first returns true
 * and the rest of the list.
 *
 * Returns empty if no matching items were found.
 */
fun <T> List<T>.takeFrom(take: (T) -> Boolean): List<T> {
    val firstToTake = this.indexOfFirst(take)

    if (firstToTake < 0) {
        return emptyList()
    }

    return this.takeLast(size - firstToTake)
}

/**
 * Splits at the predicate
 * Does not put the split item into the resulting collection
 */
fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
    return foldIndexed<T, MutableList<MutableList<T>>>(
        mutableListOf(mutableListOf())
    ) { index, acc, t ->
        val isNewGroup = predicate(t)

        if (isNewGroup) {
            // No useless empty groups
            if (acc.last().isNotEmpty() && index != this.lastIndex) {
                acc.add(mutableListOf())
            }
        } else {
            acc.last() += t
        }

        acc

    }
}