package domain.repository

interface TransactionManager {
    fun <T>performInTransaction(block:()->T):T
}