package com.sachet.postservice.error_handler

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val error: String,
    val code: HttpStatus
)