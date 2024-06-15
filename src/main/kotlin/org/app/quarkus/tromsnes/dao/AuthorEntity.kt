package org.app.quarkus.tromsnes.dao

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "author")
class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null

    lateinit var firstName: String
    lateinit var middleName: String
    lateinit var lastName: String
    lateinit var bornDate: LocalDate
    var bornPlace: String? = null
    var deadDate: LocalDate? = null

    @CreationTimestamp
    var createdAt: ZonedDateTime? = ZonedDateTime.now()

    @UpdateTimestamp
    var updatedAt: ZonedDateTime? = ZonedDateTime.now()

}