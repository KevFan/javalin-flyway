import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import io.javalin.Javalin
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun main() {
    // Set up the database connection details
    val pg = EmbeddedPostgres.start()

    val dbUrl = pg.getJdbcUrl("postgres", "postgres")
    val dbUser = "postgres"
    val dbPassword = "postgres"

    // Run Flyway to migrate the database schema
    val flyway = Flyway.configure()
        .dataSource(dbUrl, dbUser, dbPassword)
        .locations("filesystem:src/main/resources/flyway/migrations")
        .load()
    flyway.migrate()

    // Connect to the database
    Database.connect(dbUrl, "org.postgresql.Driver", dbUser, dbPassword)

    // Set up the Javalin application
    val app = Javalin.create().start(9000)

    // Define your Javalin routes and handlers here
    app.get("/") { ctx ->
        ctx.result("Hello, world!")
    }
}