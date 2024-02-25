package fr.ensicaen.pi.gpss.backend.database.repository.account;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.CardEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestCardEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestChequeBookEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Integer> {
    @Query(value =
        "SELECT bankAccount.iban " +
        "FROM BankAccount bankAccount " +
        "WHERE bankAccount.iban IS NOT NULL"
    )
    List<String> findAllAvailableIban();

    @Query(value =
            "SELECT card.cardInformation.pan FROM BankAccount bankAccount " +
            "INNER JOIN bankAccount.paymentMethods paymentMethod " +
            "INNER JOIN paymentMethod.card card " +
            "WHERE SIZE(bankAccount.paymentMethods) > 0 " +
            "AND card.cardInformation.pan IS NOT NULL " +
            "AND card.cardStatus = fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus.ACTIVATED"
    )
    List<String> findAllAvailablePan();


    @Query(value =
            "SELECT bankAccount FROM BankAccount bankAccount " +
            "WHERE bankAccount IN " +
                    "(SELECT manager.clientAccount.bankAccount FROM AccountManager manager " +
                    "WHERE manager.usersInformation = :userInformation)"
    )
    List<BankAccountEntity> findAllByUserInformation(
            @Valid @NotNull @Param("userInformation") UserInformationEntity userInformation
    );

    @Query(
            "SELECT count(bankAccount) FROM BankAccount bankAccount " +
            "JOIN ClientAccount clientAccount ON bankAccount = clientAccount.bankAccount " +
            "JOIN AccountManager accountManager ON clientAccount.accountManager = accountManager " +
            "JOIN UserInformation user ON accountManager.usersInformation = user " +
            "WHERE bankAccount.bankAccountType = :bankAccountType " +
            "AND user = :clientUserInformation"
    )
    int countAllByTypeAccountAndUserInformation(
            @Valid @NotNull @Param("bankAccountType") BankAccountType bankAccountType,
            @Valid @NotNull @Param("clientUserInformation") UserInformationEntity clientUserInformation
    );

    @Query(
            "SELECT count(bankAccount) FROM BankAccount bankAccount " +
            "WHERE bankAccount.bankAccountType = :bankAccountType"
    )
    int countAllByBankAccountType(
            @Valid @NotNull @Param("bankAccountType") BankAccountType bankAccountType
    );

    @Query(
            "SELECT bankAccount FROM BankAccount bankAccount " +
            "JOIN RequestPaymentMethodManager manager ON manager.bankAccount =  bankAccount " +
            "JOIN RequestCard request ON manager.requestCard = request " +
            "WHERE request = :requestCard"
    )
    BankAccountEntity findByRequestCard(
            @Valid @NotNull @Param("requestCard") RequestCardEntity requestCard
    );

    @Query(
            "SELECT bankAccount FROM BankAccount bankAccount " +
                    "JOIN RequestPaymentMethodManager manager ON manager.bankAccount =  bankAccount " +
                    "JOIN RequestChequeBook request ON manager.requestChequeBook = request " +
                    "WHERE request = :requestChequeBook"
    )
    BankAccountEntity findByRequestChequeBook(
            @Valid @NotNull @Param("requestChequeBook") RequestChequeBookEntity requestChequeBook
    );

    @Query(
            "SELECT requestCard FROM RequestCard requestCard " +
            "JOIN RequestPaymentMethodManager manager ON manager.requestCard = requestCard " +
            "JOIN BankAccount bankAccount ON manager.bankAccount = bankAccount " +
            "WHERE bankAccount = :bankAccount " +
            "AND requestCard.requestPaymentMethodStatusEntity.orderStatus = " +
                    "fr.ensicaen.pi.gpss.backend.database.enumerate.OrderStatus.NOT_ORDERED " +
            "ORDER BY requestCard.requestPaymentMethodStatusEntity.userRequestPaymentMethodDate DESC " +
            "LIMIT 1"
    )
    RequestCardEntity findLatestUserCardRequestByBankAccount(
            @Valid @NotNull @Param("bankAccount") BankAccountEntity bankAccount
    );

    @Query(
            "SELECT requestChequeBook FROM RequestChequeBook requestChequeBook " +
            "JOIN RequestPaymentMethodManager manager ON manager.requestChequeBook = requestChequeBook " +
            "JOIN BankAccount bankAccount ON manager.bankAccount = bankAccount " +
            "WHERE bankAccount = :bankAccount " +
            "AND requestChequeBook.requestPaymentMethodStatusEntity.orderStatus = " +
                    "fr.ensicaen.pi.gpss.backend.database.enumerate.OrderStatus.NOT_ORDERED " +
            "ORDER BY requestChequeBook.requestPaymentMethodStatusEntity.userRequestPaymentMethodDate DESC " +
            "LIMIT 1"
    )
    RequestChequeBookEntity findLatestChequeBookRequestByBankAccount(
            @Valid @NotNull @Param("bankAccount") BankAccountEntity bankAccount
    );

    @Query(
            "SELECT card FROM Card card " +
            "JOIN CardInformation cardInformation ON card.cardInformation = cardInformation " +
            "JOIN RequestCard requestCard ON requestCard.cardInformation = cardInformation " +
            "JOIN RequestPaymentMethodManager manager ON manager.requestCard = requestCard " +
            "JOIN BankAccount bankAccount ON manager.bankAccount = bankAccount " +
            "WHERE bankAccount =:bankAccount " +
            "ORDER BY requestCard.requestPaymentMethodStatusEntity.bankReceivedPaymentMethodDate DESC " +
            "LIMIT 1"
    )
    CardEntity findLatestCardByBankAccount(
            @Valid @NotNull @Param("bankAccount") BankAccountEntity bankAccount
    );

    @Query(
           "SELECT count(card) FROM Card card " +
           "JOIN PaymentMethodManager manager ON manager.card = card " +
           "JOIN BankAccount bankAccount ON manager.bankAccount = bankAccount " +
           "WHERE card.cardStatus = fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus.ACTIVATED " +
           "AND bankAccount = :bankAccount"
    )
    int countAvailableCardByBankAccount(
            @Valid @NotNull @Param("bankAccount") BankAccountEntity bankAccount
    );

    @Query(
            "SELECT card FROM Card card " +
            "JOIN PaymentMethodManager manager ON manager.card = card " +
            "JOIN BankAccount bankAccount ON manager.bankAccount = bankAccount " +
            "WHERE card.cardStatus = fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus.ACTIVATED " +
            "AND bankAccount = :bankAccount"
    )
    List<CardEntity> findAllAvailableCardByBankAccount(
            @Valid @NotNull @Param("bankAccount") BankAccountEntity bankAccount
    );

    @Query(
            "SELECT count(request.requestCard) FROM RequestPaymentMethodManager request " +
            "JOIN BankAccount bankAccount ON bankAccount = request.bankAccount " +
            "WHERE bankAccount = :bankAccount " +
            "AND request.requestCard.requestPaymentMethodStatusEntity.orderStatus = " +
                    "fr.ensicaen.pi.gpss.backend.database.enumerate.OrderStatus.NOT_ORDERED"
    )
    int countNotOrderedRequestCardByBankAccount(
            @Valid @NotNull @Param("bankAccount") BankAccountEntity bankAccount
    );

    @Query(
            "SELECT count(request.requestChequeBook) FROM RequestPaymentMethodManager request " +
                    "JOIN BankAccount bankAccount ON bankAccount = request.bankAccount " +
                    "WHERE bankAccount = :bankAccount " +
                    "AND request.requestChequeBook.requestPaymentMethodStatusEntity.orderStatus = " +
                    "fr.ensicaen.pi.gpss.backend.database.enumerate.OrderStatus.NOT_ORDERED"
    )
    int countNotOrderedRequestChequeBookByBankAccount(
            @Valid @NotNull @Param("bankAccount") BankAccountEntity bankAccount
    );
}
