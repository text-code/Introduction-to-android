package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var like: Int = 0,
    var share: Int = 998,
    var likedByMe: Boolean = false
)
