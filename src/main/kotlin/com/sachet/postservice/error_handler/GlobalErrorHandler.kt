package com.sachet.postservice.error_handler
import com.sachet.postservice.custom_error.PostDataException
import com.sachet.postservice.custom_error.PostNotFoundException
import org.bson.json.JsonObject
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalErrorHandler:ErrorWebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val dataFactory = exchange.response.bufferFactory()
        val errorResponse : ErrorResponse
        if (ex is PostNotFoundException){
            exchange.response.statusCode = HttpStatus.NOT_FOUND
             errorResponse = ErrorResponse(ex.message, HttpStatus.NOT_FOUND)
        }
        else if (ex is PostDataException){
            exchange.response.statusCode = HttpStatus.BAD_REQUEST
            errorResponse = ErrorResponse(ex.message, HttpStatus.BAD_REQUEST)
        }else{
            exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            errorResponse = ErrorResponse(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
        }
        val errorMessage = dataFactory.wrap(errorResponse.toString().toByteArray())
        return exchange.response.writeWith (Mono.just(errorMessage))
    }

}