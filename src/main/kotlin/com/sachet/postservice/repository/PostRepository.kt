package com.sachet.postservice.repository

import com.sachet.postservice.model.Posts
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface PostRepository: ReactiveMongoRepository<Posts, String>{
    fun getPostsByUserId(userId: String):Flux<Posts>
}