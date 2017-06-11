package cz.vojta.unidarchitecture.tracks

data class Model(val loading: Boolean = false,
                 val refreshing: Boolean = false,
                 val error: Throwable? = null,
                 val data: List<Track>? = null) {
    fun with(partialResult: LoadResult): Model {
        return when (partialResult) {
            is Loading -> this.copy(loading = true, refreshing = partialResult.refreshing, error = null)
            is Error -> this.copy(loading = false, refreshing = false, error = partialResult.throwable)
            is Data -> this.copy(loading = false, refreshing = false, error = null, data = partialResult.tracks)
        }
    }
}