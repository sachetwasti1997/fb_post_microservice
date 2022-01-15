package com.sachet.postservice.custom_error

class PostNotFoundException(message: String): RuntimeException(message) {
}