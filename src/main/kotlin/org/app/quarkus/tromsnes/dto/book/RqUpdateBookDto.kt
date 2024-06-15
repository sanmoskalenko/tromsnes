package org.app.quarkus.tromsnes.dto.book

import java.time.LocalDate
import java.util.UUID
import kotlinx.serialization.Serializable
import org.app.quarkus.tromsnes.serializer.LocalDateSerializer
import org.app.quarkus.tromsnes.serializer.UUIDSerializer

@Serializable
data class RqUpdateBookDto(

    val title: String? = null,
    val authors: List<@Serializable(UUIDSerializer::class) UUID>? = mutableListOf(),
    val description: String? = null,
    val publicationDate: @Serializable(LocalDateSerializer::class) LocalDate? = null

)
