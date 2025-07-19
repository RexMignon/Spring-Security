package com.mignon.springsecurity.util



import com.mignon.springsecurity.model.enum.SystemCodeEnum
import io.swagger.v3.oas.annotations.media.Schema
import java.util.Objects

/**
 * 接口响应结果类
 * @param <T> 响应数据的泛型类型
 */
@Schema(description = "接口响应结果")
data class Result<T>(
    @Schema(description = "接口响应结果描述", required = true, example = "OK")
    var msg: String? = null,

    @Schema(description = "系统响应码", required = true, example = "200")
    var code: Int? = null,

    @Schema(description = "接口的响应数据", required = true)
    var data: T? = null
) {
    companion object {
        private const val FAILED_MSG = "FAIL"
        private const val SUCCESS_MSG = "OK"

        /**
         * 创建一个成功的 Result 对象，带数据。
         * @param data 响应数据
         * @return 成功的 Result 对象
         */
        @JvmStatic
        fun <T> success(data: T?): Result<T> {
            return Result(
                msg = SystemCodeEnum.SUCCESS.msg,
                code = SystemCodeEnum.SUCCESS.code,
                data = data
            )
        }

        /**
         * 创建一个成功的 Result 对象，带数据和自定义消息。
         * @param data 响应数据
         * @param message 自定义消息
         * @return 成功的 Result 对象
         */
        @JvmStatic
        fun <T> success(data: T?, message: String): Result<T> {
            return Result(
                msg = message,
                code = SystemCodeEnum.SUCCESS.code,
                data = data
            )
        }

        /**
         * 创建一个成功的 Result 对象，不带数据。
         * @return 成功的 Result 对象
         */
        @JvmStatic
        fun <T> success(): Result<T> {
            return success(null)
        }

        /**
         * 创建一个失败的 Result 对象，使用默认系统错误码和消息。
         * @return 失败的 Result 对象
         */
        @JvmStatic
        fun <T> failed(): Result<T> {
            return Result(
                msg = SystemCodeEnum.SYSTEM_ERROR.msg,
                code = SystemCodeEnum.SYSTEM_ERROR.code,
                data = null
            )
        }

        /**
         * 创建一个失败的 Result 对象，使用指定的系统错误码枚举。
         * @param codeEnum 系统错误码枚举
         * @return 失败的 Result 对象
         */
        @JvmStatic
        fun <T> failed(codeEnum: SystemCodeEnum): Result<T> {
            return Result(
                msg = codeEnum.msg,
                code = codeEnum.code,
                data = null
            )
        }

        /**
         * 创建一个失败的 Result 对象，使用自定义消息和默认系统错误码。
         * @param msg 自定义消息
         * @return 失败的 Result 对象
         */
        @JvmStatic
        fun <T> failed(msg: String): Result<T> {
            return Result(
                msg = msg,
                code = SystemCodeEnum.SYSTEM_ERROR.code,
                data = null
            )
        }

        /**
         * 创建一个失败的 Result 对象，使用自定义消息和错误码。
         * @param message 自定义消息
         * @param errorCode 错误码
         * @return 失败的 Result 对象
         */
        @JvmStatic
        fun <T> failed(message: String, errorCode: Int): Result<T> {
            return Result(
                msg = message,
                code = errorCode,
                data = null
            )
        }

        /**
         * 创建 ResultBuilder 实例。
         * @return ResultBuilder 实例
         */
        @JvmStatic
        fun <T> builder(): ResultBuilder<T> {
            return ResultBuilder()
        }
    }

    /**
     * Result 的建造者类。
     * @param <T> 响应数据的泛型类型
     */
    class ResultBuilder<T> internal constructor() {
        private var msg: String? = null
        private var code: Int? = null
        private var data: T? = null

        fun msg(msg: String?): ResultBuilder<T> {
            this.msg = msg
            return this
        }

        fun code(code: Int?): ResultBuilder<T> {
            this.code = code
            return this
        }

        fun data(data: T?): ResultBuilder<T> {
            this.data = data
            return this
        }

        fun build(): Result<T> {
            return Result(this.msg, this.code, this.data)
        }

        override fun toString(): String {
            return "Result.ResultBuilder(msg=$msg, code=$code, data=$data)"
        }
    }
}