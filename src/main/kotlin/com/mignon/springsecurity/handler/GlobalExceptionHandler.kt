package com.mignon.springsecurity.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

/**
 * 全局异常处理器。
 * 使用 @ControllerAdvice 注解，使得这个类可以处理所有Controller抛出的异常。
 */
@ControllerAdvice
class GlobalExceptionHandler {
    private val securityExceptionsToReThrow = setOf(
        BadCredentialsException::class.java,
        AccountExpiredException::class.java,
        LockedException::class.java,
        DisabledException::class.java,
        CredentialsExpiredException::class.java,
        UsernameNotFoundException::class.java,
        AccessDeniedException::class.java,
        AuthorizationDeniedException::class.java
    )

    companion object{
        val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    /**
     * 处理所有未捕获的 Exception 异常。
     * @param ex 捕获到的异常对象
     * @return 包含错误信息的 ResponseEntity
     */
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        if (securityExceptionsToReThrow.any { it.isInstance(ex) }) {
            throw ex
        }
        logger.error("[ERROR] ${ex.message}")
        ex.printStackTrace()
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "服务器内部错误，请稍后再试。",
            path = ""
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * 处理 IllegalArgumentException 异常。
     * 当方法接收到非法或不适当的参数时抛出。
     * @param ex 捕获到的 IllegalArgumentException 异常对象
     * @return 包含错误信息的 ResponseEntity
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = ex.message ?: "请求参数无效。",
            path = ""
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    /**
     * 统一的错误响应体结构。
     * 建议在实际项目中将其定义在一个单独的数据类中。
     */
    data class ErrorResponse(
        val timestamp: LocalDateTime,
        val status: Int,
        val error: String,
        val message: String,
        val path: String
    )
}