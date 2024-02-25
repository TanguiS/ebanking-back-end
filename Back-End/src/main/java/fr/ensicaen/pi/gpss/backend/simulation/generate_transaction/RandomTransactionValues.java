package fr.ensicaen.pi.gpss.backend.simulation.generate_transaction;

import fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Currency;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionType;
import fr.ensicaen.pi.gpss.backend.payload.request.CollectedTransaction;
import jakarta.validation.constraints.NotEmpty;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomTransactionValues {
    private final Random randomizer;
    private final RandomPrimaryAccountNumberComponent panRandomizer;
    private final RandomBankAccountIdentificationComponent ibanRandomizer;
    private int id;
    private static final Currency[] CURRENCIES_POOL = Currency.values();
    private static final TransactionType[] TRANSACTION_TYPES_POOL = TransactionType.values();

    public RandomTransactionValues(
            Random randomizer,
            RandomPrimaryAccountNumberComponent panRandomizer,
            RandomBankAccountIdentificationComponent ibanRandomizer
    ) {
        this.randomizer = randomizer;
        this.panRandomizer = panRandomizer;
        this.ibanRandomizer = ibanRandomizer;
    }

    private int shuffleAmount() {
        return randomizer.nextInt(100, 25000);
    }

    private Currency shuffleCurrency() {
        return CURRENCIES_POOL[randomizer.nextInt(0, CURRENCIES_POOL.length)];
    }

    private TransactionType shuffleType() {
        return TRANSACTION_TYPES_POOL[randomizer.nextInt(0, TRANSACTION_TYPES_POOL.length)];
    }

    private String shuffleTransactionActorFromRandomGeneration() {
        if (randomizer.nextBoolean()) {
            return ibanRandomizer.shuffle();
        }
        return panRandomizer.shuffle();
    }

    private String shuffleTransactionActorFromDB(List<String> nonEncryptedIban, List<String> nonEncryptedPan) {
        if (randomizer.nextBoolean()) {
            return nonEncryptedIban.get(randomizer.nextInt(0, nonEncryptedIban.size()));
        }
        return nonEncryptedPan.get(randomizer.nextInt(0, nonEncryptedPan.size()));
    }

    private String shuffleTransactionActor(
            List<String> nonEncryptedIban, List<String> nonEncryptedPan, boolean isForceUseOfDBAsPool
    ) {
        String transaction = shuffleTransactionActorFromRandomGeneration();
        if (randomizer.nextBoolean() || isForceUseOfDBAsPool) {
            transaction = shuffleTransactionActorFromDB(nonEncryptedIban, nonEncryptedPan);
        }
        return transaction;
    }

    private CollectedTransaction shuffle(
            List<String> nonEncryptedIban,
            List<String> nonEncryptedPan,
            AccountingDirection accountingDirection,
            TransactionType transactionType,
            int amount,
            Currency currency,
            Timestamp transactionDate,
            boolean isForceUseOfDBAsPool
    ) {
        String transaction = shuffleTransactionActor(nonEncryptedIban, nonEncryptedPan, isForceUseOfDBAsPool);
        id++;
        return new CollectedTransaction(
                id, accountingDirection, transactionType, amount, currency, transaction, transactionDate.toString()
        );
    }

    public CollectedTransactionTuple shuffleCollectedTransaction(
            @NotEmpty(message = "nonEncryptedIban list can not be empty") List<String> nonEncryptedIban,
            @NotEmpty(message = "nonEncryptedPan list can not be empty") List<String> nonEncryptedPan
    ) {
        int amount = shuffleAmount();
        Currency currency = shuffleCurrency();
        TransactionType type = shuffleType();
        Timestamp transactionDate = new Timestamp(new Date().getTime());

        CollectedTransaction debit = shuffle(
                nonEncryptedIban, nonEncryptedPan, AccountingDirection.DEBIT,
                type, amount, currency, transactionDate, false
        );
        CollectedTransaction credit = shuffle(
                nonEncryptedIban, nonEncryptedPan, AccountingDirection.CREDIT,
                type, amount, currency, transactionDate, true
        );

        return new CollectedTransactionTuple(debit, credit);
    }
}
