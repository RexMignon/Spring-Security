package com.mignon.springsecurity.util

sealed class Result<out T> {

    /**
     * 表示操作成功的状态。
     *
     * @param data 成功时返回的数据。
     */
    data class Success<out T>(val data: T) : Result<T>()

    /**
     * 表示操作失败的状态。
     *
     * @param exception 导致失败的异常。
     * @param message 失败的描述信息，如果异常为null则可使用。
     */
    data class Error(val exception: Throwable? = null, val message: String? = null) : Result<Nothing>()

    /**
     * 辅助函数：如果结果是 Success，则返回其数据；否则返回 null。
     */
    fun getOrNull(): T? {
        return if (this is Success) this.data else null
    }

    /**
     * 辅助函数：如果结果是 Error，则返回其异常；否则返回 null。
     */
    fun exceptionOrNull(): Throwable? {
        return if (this is Error) this.exception else null
    }

    /**
     * 辅助函数：如果结果是 Error，则返回其错误信息；否则返回 null。
     */
    fun messageOrNull(): String? {
        return if (this is Error) this.message else null
    }

    /**
     * 对 Result 进行模式匹配，并执行相应的操作。
     *
     * @param onSuccess 当结果是 Success 时执行的 lambda 表达式。
     * @param onError 当结果是 Error 时执行的 lambda 表达式。
     * @return 返回 onSuccess 或 onError lambda 的结果。
     */
    inline fun <R> fold(onSuccess: (T) -> R, onError: (Throwable?, String?) -> R): R {
        return when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(exception, message)
        }
    }
}