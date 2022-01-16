package com.sachet.postservice.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotNull

@Document(value = "posts")
class Posts(
    @Id
    var postId: String?= null,
    @field: NotNull(message = "User Id cannot be null!")
    var userId: String ?= null,
    @field: NotNull(message = "Post title cannot be null!")
    var title: String ?= null,
    var description: String ?= null,
){
    override fun toString(): String {
        return "Posts(postId=$postId, userId=$userId, title=$title, description=$description)"
    }
}