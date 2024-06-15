package org.app.quarkus.tromsnes.dao

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "book")
class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id", nullable = false)
    var id: UUID? = null
    var description: String? = null
    var publicationDate: LocalDate? = null
    var title: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    var authors: MutableList<AuthorEntity> = mutableListOf()

    @CreationTimestamp
    var createdAt: ZonedDateTime? = ZonedDateTime.now()

    @UpdateTimestamp
    var updatedAt: ZonedDateTime? = ZonedDateTime.now()

}