package com.gwabs.getpayedpos.domain.usecase
import com.gwabs.getpayedpos.data.repository.AccountRepository
import com.gwabs.getpayedpos.data.repository.TransferRepository
import com.gwabs.getpayedpos.domain.model.PaymentEvent
import kotlin.random.Random

class SimulateIncomingPaymentUseCase(
    private val accountRepo: AccountRepository,
    private val transferRepo: TransferRepository
) {
    private fun randomAmount(): Double {
        // 500 – 50,000 NGN with 2 dp
        val kobo = Random.nextInt(50_000, 5_000_000) // in kobo (x100)
        return kobo / 100.0
    }

    private fun randomMaskedAccount(): String {
        val digits = (1..10).joinToString("") { Random.nextInt(0, 10).toString() }
        return digits.replaceRange(3, 7, "****") // e.g., 123****890
    }

    private fun randomAccountName(): String {
        val firstNames = listOf(
            "Ade", "Bola", "Chidi", "Dike", "Efe", "Femi", "Gozie", "Hassan",
            "Ifeanyi", "Jide", "Kola", "Lekan", "Musa", "Ngozi", "Ola", "Pius",
            "Rotimi", "Sade", "Tayo", "Uche", "Victor", "Wale", "Yemi", "Zainab"
        )
        val lastNames = listOf(
            "Abubakar", "Bello", "Chukwu", "Danjuma", "Eze", "Falola", "Gowon",
            "Haruna", "Ibrahim", "Jega", "Kalu", "Lawal", "Mohammed", "Nwachukwu",
            "Okafor", "Popoola", "Quadri", "Rufa'i", "Sani", "Tafawa-Balewa",
            "Umar", "Williams", "Yakubu", "Zaria"
        )
        return "${firstNames.random()} ${lastNames.random()}"
    }

    private fun randomBankName(): String {
        val banks = listOf(
            "Access Bank", "Citibank", "Ecobank", "Fidelity Bank", "First Bank",
            "FCMB", "GTBank", "Heritage Bank", "Keystone Bank", "Kuda Bank",
            "Opay", "Palmpay", "Polaris Bank", "Stanbic IBTC Bank", "Standard Chartered Bank",
            "Sterling Bank", "Suntrust Bank", "Union Bank", "UBA", "Unity Bank",
            "Wema Bank", "Zenith Bank"
        )
        return banks.random()
    }

    suspend operator fun invoke(
        amountOverride: Double? = null,
        fromAccountOverride: String? = null,
        fromAccountName: String? = null,
        fromBankName: String? = null
    ): PaymentEvent {
        val amount = amountOverride ?: randomAmount()
        val fromAccount = fromAccountOverride ?: randomMaskedAccount()
        val fromName = fromAccountName ?: randomAccountName()
        val fromBankName = fromBankName?: randomBankName()
        val ts = System.currentTimeMillis()

        // Update balance and log
        accountRepo.incrementBalance(amount)
        transferRepo.logIncoming(amount, fromAccountNumber = fromAccount,fromAccName = fromName,fromBankName = fromBankName, ts = ts)

        return PaymentEvent(amount = amount, fromAccountNumber = fromAccount, fromAccName = fromName, fromBankName = fromBankName, timestampMillis = ts)
    }
}
