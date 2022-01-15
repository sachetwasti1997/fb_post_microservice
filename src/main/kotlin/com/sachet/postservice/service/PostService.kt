package com.sachet.postservice.service

import com.sachet.postservice.model.Posts
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PostService {
    fun addPost(posts: Mono<Posts>):Mono<Posts>
    fun getAllPost(): Flux<Posts>
    fun getPostByUserId(userId:String):Flux<Posts>
    fun updatePost(postId:String, posts: Mono<Posts>): Mono<Posts>
    fun deletePosts(postId: String):Mono<Void>
}