package org.app.quarkus.tromsnes.mapper

import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import java.util.UUID
import org.app.quarkus.tromsnes.dao.AuthorEntity
import org.app.quarkus.tromsnes.dao.BookEntity
import org.app.quarkus.tromsnes.dto.book.RqCreateBookDto
import org.app.quarkus.tromsnes.dto.book.RqUpdateBookDto
import org.app.quarkus.tromsnes.dto.book.RsBookDto
import org.app.quarkus.tromsnes.repository.AuthorRepository

@ApplicationScoped
class BookMapper {

    @Inject
    lateinit var authorMapper: AuthorMapper

    @Inject
    lateinit var authorRepository: AuthorRepository

    fun toCreateEntity(rq: RqCreateBookDto): Uni<BookEntity>? {
        return authorRepository.findByIds(rq.authors)
            .onItem().transform { authors ->
                if (authors.size != rq.authors.size) {
                    throw RuntimeException("One or more authors not found!")
                }
                BookEntity().apply {
                    title = rq.title
                    description = rq.description
                    publicationDate = rq.publicationDate
                    this.authors = authors.toMutableList()
                }
            }
    }

    fun toUpdateEntity(entity: BookEntity, rq: RqUpdateBookDto): BookEntity {
        return BookEntity().apply {
            rq.title?.let { entity.title = it }
            rq.description?.let { entity.description = it }
            rq.publicationDate?.let { entity.publicationDate = it }
            rq.authors?.let { resolveAuthorsByIds(it, entity) }
        }
    }

    fun toDto(bookEntity: BookEntity): RsBookDto {
        return RsBookDto(
            id = bookEntity.id,
            title = bookEntity.title,
            description = bookEntity.description,
            publicationDate = bookEntity.publicationDate,
            authors = bookEntity.authors.map(authorMapper::toDto),
            createdAt = bookEntity.createdAt,
            updatedAt = bookEntity.updatedAt
        )
    }

    fun toListDto(list: List<BookEntity>): List<RsBookDto> = list.map { toDto(it) }

    fun resolveAuthorsByIds(authorIds: List<UUID>, entity: BookEntity): Uni<MutableList<AuthorEntity>>? {
        if (authorIds.isEmpty()) {
            return Uni.createFrom().item(mutableListOf())
        }
        return authorRepository.findByIds(authorIds)
            .onItem().ifNotNull().transform { item ->
                if (item.size != authorIds.size) {
                    throw RuntimeException("One or more authors not found!")
                }
                return@transform item.toMutableList()
            }
            .onItem().ifNull().failWith { RuntimeException("Records not found!") }
    }

}
