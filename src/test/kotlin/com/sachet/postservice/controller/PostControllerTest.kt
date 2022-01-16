package com.sachet.postservice.controller

import com.sachet.postservice.model.Posts
import com.sachet.postservice.repository.PostRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
internal class PostControllerTest
    @Autowired
    constructor(
        val postRepository: PostRepository,
        val webTestClient: WebTestClient
    ){

    private val POST_BASE_URI = "/api/v1/post"
    private var id : String?= UUID.randomUUID().toString()

    @BeforeEach
    fun setUp() {
        val postList = listOf(
            Posts(
                postId = UUID.randomUUID().toString(),
                title = "Tour Diaries",
                userId = id,
                description = "Wonderful Tour to Sikkim"
            ),
            Posts(
                postId = UUID.randomUUID().toString(),
                title = "Tiger",
                userId = UUID.randomUUID().toString(),
                description = "My Love"
            ),
            Posts(
                postId = UUID.randomUUID().toString(),
                title = "Mejistic View",
                userId = UUID.randomUUID().toString(),
                description = "The Untouched Kanchanjunga."
            ),
            Posts(
                postId = UUID.randomUUID().toString(),
                title = "BMW",
                userId = id,
                description = "Happy to have a BMW, splendid car with splendid performance!"
            ),
            Posts(
                postId = id,
                title = "Good Morning",
                userId = id,
                description = ""
            ),
            Posts(
                postId = UUID.randomUUID().toString(),
                title = "Random Post",
                userId = id,
                description = "Random post for pagination!"
            ),
        )
        postRepository.saveAll(postList).blockLast();
    }

    @AfterEach
    fun tearDown() {
        postRepository.deleteAll().block()
    }

    @Test
    fun savePost() {
        val post = Posts(
            postId = UUID.randomUUID().toString(),
            title = "Darjeeling",
            userId = UUID.randomUUID().toString(),
            description = "The Queen of hills!"
        )
        webTestClient
            .post()
            .uri("$POST_BASE_URI/save")
            .bodyValue(post)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Posts::class.java)
            .consumeWith {
                val savedPost = it.responseBody
                assertNotNull(savedPost)
                assertNotNull(savedPost?.postId)
            }
    }

    @Test
    fun getPost() {
        webTestClient
            .get()
            .uri(POST_BASE_URI)
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(Posts::class.java)
            .hasSize(5);
    }

    @Test
    fun getPostByUserId(){
        val page = 0
        val size = 3
        webTestClient
            .get()
            .uri{
                it
                    .path("$POST_BASE_URI/$id")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .exchange()
            .expectStatus()
            .isOk
            .expectBodyList(Posts::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Posts>> {
                println(it.responseBody)
                assertTrue(it.responseBody?.size == 3)
            }
    }

    @Test
    fun getPostByUserIdPageNumberMoreThenDataPresent(){
        val page = 2
        val size = 3
        webTestClient
            .get()
            .uri{
                it
                    .path("$POST_BASE_URI/$id")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .exchange()
            .expectStatus()
            .isNotFound

    }

    @Test
    fun updatePost(){
        val post = Posts(
            title = "Good Morning",
            userId = id,
            description = "Coffee with love!"
        )
        webTestClient
            .put()
            .uri("$POST_BASE_URI/$id")
            .bodyValue(post)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(Posts::class.java)
            .consumeWith {
                val postBody = it.responseBody
                assertNotNull(postBody)
                assertEquals("Coffee with love!", postBody?.description)
            }
    }

    @Test
    fun deletePost(){
        webTestClient
            .delete()
            .uri("$POST_BASE_URI/$id")
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun getPostByUserIdNotFound(){
        webTestClient
            .get()
            .uri("$POST_BASE_URI/${UUID.randomUUID()}")
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun updatePostNotFound(){
        val post = Posts(
            title = "Good Morning",
            userId = id,
            description = "Coffee with love!"
        )
        webTestClient
            .put()
            .uri("$POST_BASE_URI/${UUID.randomUUID()}")
            .bodyValue(post)
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun deletePostNotFound(){
        webTestClient
            .delete()
            .uri("$POST_BASE_URI/${UUID.randomUUID()}")
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody(String::class.java)
            .consumeWith {
                val jsonResponse = it.responseBody
                assertTrue(jsonResponse?.contains("errorCode")!!)
                assertTrue(jsonResponse.contains("404 NOT_FOUND"))
            }
    }

    @Test
    fun savePostUserIdAndTitleNull() {
        val post = Posts(
            postId = UUID.randomUUID().toString(),
            description = "The Queen of hills!"
        )
        webTestClient
            .post()
            .uri("$POST_BASE_URI/save")
            .bodyValue(post)
            .exchange()
            .expectStatus()
            .is4xxClientError

    }

}

















