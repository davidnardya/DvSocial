package com.davidnardya.dvsocial.model

interface Likeable {
    val id: String?
    var isLiked: Boolean?
    var likes: Int?
}