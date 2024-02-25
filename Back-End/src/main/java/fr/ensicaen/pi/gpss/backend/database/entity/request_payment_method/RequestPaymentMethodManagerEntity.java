package fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "RequestPaymentMethodManager")
@Table(name = "`RequestPaymentMethodManager`", schema = "`RequestPaymentMethod`")
@Getter
@Setter
public class RequestPaymentMethodManagerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`idRequestPaymentMethodManager`", nullable = false)
    private Integer idRequestPaymentMethodManager;
    @ManyToOne
    @JoinColumn(name = "`idBankAccount`", nullable = false)
    private BankAccountEntity bankAccount;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idRequestPaymentCard`")
    private RequestCardEntity requestCard;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idRequestPaymentChequeBook`")
    private RequestChequeBookEntity requestChequeBook;

    public RequestPaymentMethodManagerEntity(
            BankAccountEntity bankAccount,
            RequestCardEntity requestCard,
            RequestChequeBookEntity requestChequeBook
    ) {
        this.bankAccount = bankAccount;
        this.requestCard = requestCard;
        this.requestChequeBook = requestChequeBook;
    }

    public RequestPaymentMethodManagerEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestPaymentMethodManagerEntity that = (RequestPaymentMethodManagerEntity) o;
        return Objects.equals(idRequestPaymentMethodManager, that.idRequestPaymentMethodManager) &&
                Objects.equals(bankAccount, that.bankAccount) &&
                Objects.equals(requestCard, that.requestCard) &&
                Objects.equals(requestChequeBook, that.requestChequeBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idRequestPaymentMethodManager, bankAccount, requestCard, requestChequeBook
        );
    }
}
