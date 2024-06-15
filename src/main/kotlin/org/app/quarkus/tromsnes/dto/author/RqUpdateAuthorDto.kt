package org.app.quarkus.tromsnes.dto.author

import java.time.LocalDate
import kotlinx.serialization.Serializable
import org.app.quarkus.tromsnes.serializer.LocalDateSerializer

@Serializable
data class RqUpdateAuthorDto(

    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val bornPlace: String? = null,
    @Serializable(LocalDateSerializer::class)
    val bornDate: LocalDate? = null,
    @Serializable(LocalDateSerializer::class)
    val deadDate: LocalDate? = null

)