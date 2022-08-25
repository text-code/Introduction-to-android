package ru.netology.nmedia.db

import ru.netology.nmedia.dto.Post

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    content = content,
    published = published,
    like = like,
    share = share,
    likedByMe = likedByMe
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    content = content,
    published = published,
    like = like,
    share = share,
    likedByMe = likedByMe
)