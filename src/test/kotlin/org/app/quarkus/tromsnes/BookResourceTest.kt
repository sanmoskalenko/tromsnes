package org.app.quarkus.tromsnes

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.core.MediaType
import java.time.LocalDate
import java.util.UUID
import net.javacrumbs.jsonunit.assertj.JsonAssertion
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.anyVararg
import org.app.quarkus.tromsnes.dao.AuthorEntity
import org.app.quarkus.tromsnes.dao.BookEntity
import org.app.quarkus.tromsnes.exception.ResourceNotFoundException
import org.app.quarkus.tromsnes.repository.AuthorRepository
import org.app.quarkus.tromsnes.repository.BookRepository

@QuarkusTest
class BookResourceTest {

    @InjectMock
    lateinit var bookRepository: BookRepository

    @InjectMock
    lateinit var authorRepository: AuthorRepository

    private final val authorId: UUID = UUID.randomUUID()
    private final val bookId: UUID = UUID.randomUUID()
    val createBookRq: String = String.format(TestUtils.useFixture("createBook.json"), authorId)

    val authorEntity = AuthorEntity().apply {
        id = authorId
        firstName = "Foo"
        middleName = "Bar"
        lastName = "Baz"
        bornPlace = "FooBar"
        bornDate = LocalDate.of(1800, 1, 1)
        deadDate = LocalDate.of(1900, 1, 1)
    }

    val bookEntity = BookEntity().apply {
        id = bookId
        authors = listOf(authorEntity).toMutableList()
        description = "testDescription"
        title = "testTitle"
        publicationDate = LocalDate.of(2023, 1, 1)

    }

    @Test
    fun `should create a book and return status 201`() {
        Mockito.`when`(bookRepository.persist(anyVararg(BookEntity::class)))
            .thenReturn(Uni.createFrom().item(bookEntity))
        Mockito.`when`(authorRepository.findByIds(listOf(authorId)))
            .thenReturn(Uni.createFrom().item(listOf(authorEntity)))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .body(createBookRq)
            .post("api/v1/books").then()
            .assertThat().statusCode(HttpStatus.SC_CREATED)
            .assertThat().header("Content-Type", "application/json;charset=UTF-8")
            .extract().asPrettyString()

        assertThatJson(response).and(
            JsonAssertion { v ->
                v.node("id").isEqualTo(bookId)
                v.node("title").isEqualTo("testTitle")
                v.node("description").isEqualTo("testDescription")
                v.node("publicationDate").isEqualTo("2023-01-01")
                v.node("createdAt").isNotNull
                v.node("updatedAt").isNotNull
                v.node("authors").isArray.element(0).and(
                    JsonAssertion { i ->
                        i.node("firstName").isEqualTo("Foo")
                        i.node("middleName").isEqualTo("Bar")
                        i.node("lastName").isEqualTo("Baz")
                        i.node("bornPlace").isEqualTo("FooBar")
                        i.node("bornDate").isEqualTo("1800-01-01")
                        i.node("deadDate").isEqualTo("1900-01-01")
                        i.node("id").isNotNull
                        i.node("createdAt").isNotNull
                        i.node("updatedAt").isNotNull
                    })
            })
    }

    @Test
    fun `should return list of books with status 200`() {
        Mockito.`when`(bookRepository.listAll())
            .thenReturn(Uni.createFrom().item(listOf(bookEntity)))
        Mockito.`when`(authorRepository.findByIds(listOf(authorId)))
            .thenReturn(Uni.createFrom().item(listOf(authorEntity)))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .get("api/v1/books/list")
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .assertThat().header("Content-Type", "application/json;charset=UTF-8")
            .extract().asPrettyString()

        assertThatJson(response).and(
            JsonAssertion { v ->
                v.isArray.element(0).and(
                    JsonAssertion { i ->
                        i.node("id").isEqualTo(bookId)
                        i.node("title").isEqualTo("testTitle")
                        i.node("description").isEqualTo("testDescription")
                        i.node("publicationDate").isEqualTo("2023-01-01")
                        i.node("createdAt").isNotNull
                        i.node("updatedAt").isNotNull
                        i.node("authors").isArray.element(0).and(
                            JsonAssertion { t ->
                                t.node("firstName").isEqualTo("Foo")
                                t.node("middleName").isEqualTo("Bar")
                                t.node("lastName").isEqualTo("Baz")
                                t.node("bornPlace").isEqualTo("FooBar")
                                t.node("bornDate").isEqualTo("1800-01-01")
                                t.node("deadDate").isEqualTo("1900-01-01")
                                t.node("id").isNotNull
                                t.node("createdAt").isNotNull
                                t.node("updatedAt").isNotNull
                            })
                    })
            })
    }

    @Test
    fun `should return book with status 200`() {
        Mockito.`when`(bookRepository.findById(bookId))
            .thenReturn(Uni.createFrom().item(bookEntity))
        Mockito.`when`(authorRepository.findByIds(listOf(authorId)))
            .thenReturn(Uni.createFrom().item(listOf(authorEntity)))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .get("/api/v1/books/${bookId}")
            .then()
            .assertThat().statusCode(HttpStatus.SC_OK)
            .assertThat().header("Content-Type", "application/json;charset=UTF-8")
            .extract().asPrettyString()

        assertThatJson(response).and(
            JsonAssertion { v ->
                v.node("id").isEqualTo(bookId)
                v.node("title").isEqualTo("testTitle")
                v.node("description").isEqualTo("testDescription")
                v.node("publicationDate").isEqualTo("2023-01-01")
                v.node("createdAt").isNotNull
                v.node("updatedAt").isNotNull
                v.node("authors").isArray.element(0).and(
                    JsonAssertion { i ->
                        i.node("firstName").isEqualTo("Foo")
                        i.node("middleName").isEqualTo("Bar")
                        i.node("lastName").isEqualTo("Baz")
                        i.node("bornPlace").isEqualTo("FooBar")
                        i.node("bornDate").isEqualTo("1800-01-01")
                        i.node("deadDate").isEqualTo("1900-01-01")
                        i.node("id").isNotNull
                        i.node("createdAt").isNotNull
                        i.node("updatedAt").isNotNull
                    })
            })
    }

    @Test
    fun `should return 404 when book is not found`() {
        Mockito.`when`(bookRepository.findById(bookId))
            .thenThrow(ResourceNotFoundException("Record with ID $bookId not found"))

        val response = given().`when`()
            .header("Content-Type", MediaType.APPLICATION_JSON)
            .get("/api/v1/books/${bookId}")
            .then()
            .assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
            .assertThat().header("Content-Type", "application/json;charset=UTF-8")
            .extract().asPrettyString()

        assertThatJson(response).and(
            JsonAssertion { v ->
                v.node("details").isEqualTo("Record with ID $bookId not found")
            })
    }

}
