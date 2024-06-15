package org.app.quarkus.tromsnes.dto.author

import java.time.LocalDate
import kotlinx.serialization.Serializable
import org.app.quarkus.tromsnes.serializer.LocalDateSerializer

@Serializable
data class RqCreateAuthorDto(

    val firstName: String,
    val middleName: String,
    val lastName: String,
    @Serializable(LocalDateSerializer::class)
    val bornDate: LocalDate,
    @Serializable(LocalDateSerializer::class)
    val deadDate: LocalDate? = null,
    val bornPlace: String? = null

)