package com.sachet.postservice.error_handler

import org.springframework.http.HttpStatus

class ErrorResponse(
    val error: String?,
    val code: HttpStatus
){
    override fun toString(): String {
        return "{error=$error, code=$code}"
    }
}