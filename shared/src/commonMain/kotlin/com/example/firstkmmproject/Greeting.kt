package com.example.firstkmmproject

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name} from pod lib!"
    }
}