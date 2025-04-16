package infrastructure.persistence.dao

import domain.repository.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class ExposedTransactionManager:TransactionManager {
    override fun <T> performInTransaction(block: () -> T): T {
        return transaction {
            block()
        }
    }
}