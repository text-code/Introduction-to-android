package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val like: Int = 0,
    val share: Int = 998,
    val likedByMe: Boolean = false,
    val shareByMe: Boolean = false
)
