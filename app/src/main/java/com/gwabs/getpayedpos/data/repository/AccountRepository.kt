package com.gwabs.getpayedpos.data.repository

import com.gwabs.getpayedpos.data.local.AccountEntity
import com.gwabs.getpayedpos.data.local.dao.AccountDao
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun account(): Flow<AccountEntity?>
    suspend fun ensureAccount(accountNumber: String)
    suspend fun incrementBalance(delta: Double)
    suspend fun getSnapshot(): AccountEntity
}

class AccountRepositoryImpl(
    private val dao: AccountDao
) : AccountRepository {
    override fun account(): Flow<AccountEntity?> = dao.observeAccount()

    override suspend fun ensureAccount(accountNumber: String) {
        val current = dao.getAccount()
        if (current == null) {
            dao.upsert(AccountEntity(accountNumber = "9030863146", accountName = "AA Gwabare", bankName = "GetPayed", balance = 0.0))
        } else if (current.accountNumber != accountNumber) {
            dao.upsert(current.copy(accountNumber = accountNumber))
        }
    }

    override suspend fun incrementBalance(delta: Double) = dao.incrementBalance(delta)

    override suspend fun getSnapshot(): AccountEntity =
        dao.getAccount() ?: AccountEntity(accountNumber = "9030863146", accountName = "AA Gwabare", bankName = "GetPayed", balance = 0.0).also {
            dao.upsert(it)
        }
}
