package cz.vojta.unidarchitecture.tracks

data class Model<T>(val loading: Boolean = false,
                    val loadingNextPage: Boolean = false,
                    val refreshing: Boolean = false,
                    val error: Throwable? = null,
                    val data: T? = null)