package org.app.quarkus.tromsnes.dto.author

import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.serialization.Serializable
import org.app.quarkus.tromsnes.serializer.LocalDateSerializer
import org.app.quarkus.tromsnes.serializer.UUIDSerializer
import org.app.quarkus.tromsnes.serializer.ZonedDateTimeSerializer

@Serializable
data class RsAuthorDto(

    @Serializable(UUIDSerializer::class)
    val id: UUID?,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val bornPlace: String? = null,
    @Serializable(LocalDateSerializer::class)
    val bornDate: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val deadDate: LocalDate? = null,
    @Serializable(ZonedDateTimeSerializer::class)
    val createdAt: ZonedDateTime? = null,
    @Serializable(ZonedDateTimeSerializer::class)
    val updatedAt: ZonedDateTime? = null

)
