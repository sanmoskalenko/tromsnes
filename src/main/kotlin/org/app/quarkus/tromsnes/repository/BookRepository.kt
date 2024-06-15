package org.app.quarkus.tromsnes.repository

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID
import org.app.quarkus.tromsnes.dao.BookEntity

@ApplicationScoped
class BookRepository : PanacheRepositoryBase<BookEntity, UUID>
