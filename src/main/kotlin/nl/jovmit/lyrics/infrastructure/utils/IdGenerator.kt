package nl.jovmit.lyrics.infrastructure.utils

import java.util.*

class IdGenerator {

    fun next(): String {
        return UUID.randomUUID().toString()
    }
}
