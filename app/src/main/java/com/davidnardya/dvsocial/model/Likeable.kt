package com.davidnardya.dvsocial.model

interface Likeable {
    val likes: List<String>?
    fun isLiked(): Boolean
}