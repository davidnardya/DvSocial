package com.davidnardya.dvsocial.utils

import com.davidnardya.dvsocial.model.User
import com.davidnardya.dvsocial.model.UserImage
import com.davidnardya.dvsocial.model.UserPost


object Constants {

    val userNameList = listOf(
        "MyNameIsPaul",
        "ThisIsAUserName",
        "FeckinHeckin",
        "BabyYoda",
        "Shakalaka"
    )

    val userImageCaptionList = listOf(
        "Look at my doggo! His name is Goddo",
        "Look at my doggo! His name is Rex",
        "Look at my doggo! His name is Ben",
        "Look at my doggo! His name is Loopy",
        "Look at my doggo! His name is Tie",
        "Look at my doggo! His name is Do-go-go"
    )

//    val userI: User = User(
//        "123",
//        "1122",
//        "MyNameIsPaul",
//        listOf(
//            UserPost(
//                "123123",
//                "",
//                "https://images.dog.ceo/breeds/saluki/n02091831_1730.jpg",
//                "Look at my doggo! His name is Goddo"
//            ),
//            UserPost(
//                "1231234",
//                "",
//                "https://images.dog.ceo/breeds/hound-blood/n02088466_8812.jpg",
//                "Look at my doggo! His name is Rex"
//            ),
//            UserPost(
//                "12312341",
//                "",
//                "https://images.dog.ceo/breeds/komondor/n02105505_2134.jpg",
//                "Look at my doggo! His name is Ben"
//            )
//        )
//    )
//
//    val userII: User = User(
//        "1234",
//        "1122",
//        "ThisIsAUserName",
//        listOf(
//            UserPost(
//                "123123213",
//                "",
//                "https://images.dog.ceo/breeds/airedale/n02096051_7772.jpg",
//                "Look at my doggo! His name is Loopy"
//            ),
//            UserPost(
//                "1231234214",
//                "",
//                "https://images.dog.ceo/breeds/frise-bichon/jh-ezio-3.jpg",
//                "Look at my doggo! His name is Tie"
//            )
//        )
//    )
//
//    val userIII: User = User(
//        "123412311111",
//        "1122",
//        "FeckinHeckin",
//        listOf(
//            UserPost(
//                "1231232133333331",
//                "",
//                "https://images.dog.ceo/breeds/ridgeback-rhodesian/n02087394_5552.jpg",
//                "Look at my doggo! His name is Loopy"
//            )
//        )
//    )
//
    val emptyUser: User = User(
        "",
        "",
        "",
        emptyList()
    )

    const val BASE_URL = "https://dog.ceo/api/"
    const val USER_NAME = "username"
    const val PASSWORD = "password"
}