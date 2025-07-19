package com.mignon.springsecurity.model.enum

enum class SystemCodeEnum(val code: Int, val msg: String) {
    /** 成功的响应结果 */
    SUCCESS(200, "成功的响应结果"),

    /** 未经授权的访问 */
    UNAUTHORIZED(401, "未经授权的访问"),

    /** 服务器内部错误 */
    SYSTEM_ERROR(500, "服务器内部错误");

}