package com.example.firstkmmproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform