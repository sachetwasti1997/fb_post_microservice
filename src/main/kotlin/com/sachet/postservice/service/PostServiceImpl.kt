package com.sachet.postservice.service

import com.sachet.postservice.custom_error.PostDataException
import com.sachet.postservice.custom_error.PostNotFoundException
import com.sachet.postservice.model.Posts
import com.sachet.postservice.repository.PostRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors
import javax.validation.Validator

@Service
class PostServiceImpl
    (
        val postRepository: PostRepository,
        val validator: Validator
    )
    : PostService {

    override fun addPost(posts: Mono<Posts>): Mono<Posts> {
        return posts
            .doOnNext {
                validateRequsest(it)
            }
            .flatMap {
                postRepository
                    .save(it)
            }
    }

    override fun getAllPost(): Flux<Posts> {
        return postRepository.findAll()
    }

    override fun getPostByUserId(userId: String, page:Long, size:Long): Flux<Posts> {
        return postRepository
            .getPostsByUserId(userId)
            .skip(page * size)
            .take(size)
    }

    override fun updatePost(postId: String, posts: Mono<Posts>): Mono<Posts> {
        return posts
            .doOnNext {
                validateRequsest(it)
            }
            .flatMap {postReceived ->
                postRepository
                    .findById(postId)
                    .switchIfEmpty(
                        Mono.error(PostNotFoundException("No Post Found"))
                    )
                    .flatMap { postSaved ->
                        postSaved.description = postReceived.description
                        postSaved.title = postReceived.title
                        postRepository.save(postSaved)
                    }
            }
    }

    override fun deletePosts(postId: String): Mono<Void> {
        return postRepository
            .findById(postId)
            .switchIfEmpty(Mono.error(PostNotFoundException("Post not found")))
            .flatMap {
                postRepository.delete(it)
            }
    }

    private fun validateRequsest(review: Posts?) {
        val constraintVoilations = validator.validate(review)
        if (constraintVoilations.size > 0) {
            val consStr = constraintVoilations
                .stream()
                .map {
                    it.message
                }
                .collect(Collectors.joining(","))
            throw PostDataException(consStr)
        }
    }
}

















