package org.app.quarkus.tromsnes

import java.nio.charset.StandardCharsets

object TestUtils {

    fun useFixture(fileName: String): String {
        val classLoader = this::class.java.classLoader
        val inputStream = classLoader.getResourceAsStream("fixtures/$fileName")
            ?: throw IllegalArgumentException("Resource not found: $fileName")

        inputStream.bufferedReader(StandardCharsets.UTF_8).use { reader ->
            return reader.readText()
        }
    }

}
