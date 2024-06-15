package org.app.quarkus.tromsnes.dto.book

import java.time.LocalDate
import java.util.UUID
import kotlinx.serialization.Serializable
import org.app.quarkus.tromsnes.serializer.LocalDateSerializer
import org.app.quarkus.tromsnes.serializer.UUIDSerializer

@Serializable
data class RqCreateBookDto(

    val title: String,
    val authors: List<@Serializable(UUIDSerializer::class) UUID>,
    val description: String? = null,
    @Serializable(LocalDateSerializer::class)
    val publicationDate: LocalDate

)
