package com.davidnardya.dvsocial.utils

import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserPost

object Constants {

    val userI: User = User(
        "123",
        "MyNameIsPaul",
        listOf(
            UserPost(
                "123123",
                "",
                "https://images.dog.ceo/breeds/saluki/n02091831_1730.jpg",
                "Look at my doggo! His name is Goddo"
            ),
            UserPost(
                "1231234",
                "",
                "https://images.dog.ceo/breeds/hound-blood/n02088466_8812.jpg",
                "Look at my doggo! His name is Rex"
            ),
            UserPost(
                "12312341",
                "",
                "https://images.dog.ceo/breeds/komondor/n02105505_2134.jpg",
                "Look at my doggo! His name is Ben"
            )
        )
    )

    val userII: User = User(
        "1234",
        "ThisIsAUserName",
        listOf(
            UserPost(
                "123123213",
                "",
                "https://images.dog.ceo/breeds/airedale/n02096051_7772.jpg",
                "Look at my doggo! His name is Loopy"
            ),
            UserPost(
                "1231234214",
                "",
                "https://images.dog.ceo/breeds/frise-bichon/jh-ezio-3.jpg",
                "Look at my doggo! His name is Tie"
            )
        )
    )

    val userIII: User = User(
        "123412311111",
        "FeckinHeckin",
        listOf(
            UserPost(
                "1231232133333331",
                "",
                "https://images.dog.ceo/breeds/ridgeback-rhodesian/n02087394_5552.jpg",
                "Look at my doggo! His name is Loopy"
            )
        )
    )
}