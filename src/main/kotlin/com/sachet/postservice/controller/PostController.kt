package com.sachet.postservice.controller

import com.sachet.postservice.model.Posts
import com.sachet.postservice.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/post")
class PostController(
    val postService: PostService,

) {

    @PostMapping("/save")
    fun savePost(@RequestBody post: Mono<Posts>):Mono<ResponseEntity<Posts>>{
        return postService
            .addPost(post)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
            .log()
    }

    @GetMapping("/{userId}")
    fun getPostByUserId(
        @PathVariable userId:String,
        @RequestParam(value = "page", defaultValue = "0")page: Long,
        @RequestParam(value = "size", defaultValue = "3")size: Long
    ):Mono<ResponseEntity<List<Posts>>>{
        return postService
            .getPostByUserId(userId, page, size)
            .collectList()
            .map {
                if (it.isEmpty()){
                    return@map ResponseEntity(HttpStatus.NOT_FOUND)
                }
                ResponseEntity(it, HttpStatus.OK)
            }
            .log()
    }

    @GetMapping("")
    fun getAllPosts():Mono<ResponseEntity<List<Posts>>>{
        return postService
            .getAllPost()
            .collectList()
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
            .log()
    }

    @PutMapping("/{postId}")
    fun updatePost(@PathVariable postId: String, @RequestBody post: Mono<Posts>):Mono<ResponseEntity<Posts>>{
        return postService
            .updatePost(postId, post)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
            .switchIfEmpty(
                Mono.just(ResponseEntity(HttpStatus.NOT_FOUND))
            )
            .log()
    }

    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: String):Mono<ResponseEntity<Void>>{
        return postService
            .deletePosts(postId)
            .map {
                ResponseEntity<Void>(HttpStatus.OK)
            }
            .log()
    }

}























