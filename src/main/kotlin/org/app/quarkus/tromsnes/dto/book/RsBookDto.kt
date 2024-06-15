package org.app.quarkus.tromsnes.dto.book

import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.serialization.Serializable
import org.app.quarkus.tromsnes.dto.author.RsAuthorDto
import org.app.quarkus.tromsnes.serializer.LocalDateSerializer
import org.app.quarkus.tromsnes.serializer.UUIDSerializer
import org.app.quarkus.tromsnes.serializer.ZonedDateTimeSerializer

@Serializable
data class RsBookDto(

    @Serializable(UUIDSerializer::class)
    val id: UUID? = null,
    val title: String? = null,
    val authors: List<RsAuthorDto>? = null,
    val description: String? = null,
    @Serializable(LocalDateSerializer::class)
    val publicationDate: LocalDate? = null,
    @Serializable(ZonedDateTimeSerializer::class)
    val createdAt: ZonedDateTime? = null,
    @Serializable(ZonedDateTimeSerializer::class)
    val updatedAt: ZonedDateTime? = null

)
