package fr.ensicaen.pi.gpss.backend.database.repository.payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.CardEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<CardEntity, Integer> {
    boolean existsByIdCardAndCardStatus(
            @Valid @NotNull @PositiveOrZero @Param("idCard") Integer idCard,
            @Valid @NotNull @Param("cardStatus") CardStatus cardStatus
    );

    @Query(
            "SELECT bankAccount FROM BankAccount bankAccount " +
            "JOIN PaymentMethodManager manager ON manager.bankAccount = bankAccount " +
            "JOIN Card card ON manager.card = card " +
            "WHERE card = :card"
    )
    BankAccountEntity findBankAccountByCard(
            @Valid @NotNull @Param("card") CardEntity card
    );

    @Query(
            "SELECT cast(date_part('year', age(" +
                            "cardInformation.expirationCardDate, " +
                            "requestCard.requestPaymentMethodStatusEntity.bankRequestPaymentMethodDate" +
                            ")" +
            ") as INTEGER) " +
            "FROM RequestCard requestCard " +
            "JOIN CardInformation cardInformation ON requestCard.cardInformation = cardInformation " +
            "JOIN Card card ON card.cardInformation = cardInformation " +
            "WHERE card = :card"
    )
    Integer getExpirationDateInYear(
            @Valid @NotNull @Param("card") CardEntity card
    );
}
