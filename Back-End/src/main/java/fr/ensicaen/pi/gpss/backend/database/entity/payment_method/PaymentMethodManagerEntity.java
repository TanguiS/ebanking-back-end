package fr.ensicaen.pi.gpss.backend.database.entity.payment_method;

import fr.ensicaen.pi.gpss.backend.database.entity.account.BankAccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity(name = "PaymentMethodManager")
@Table(name = "`PaymentMethodManager`", schema = "`PaymentMethod`")
@Getter
@Setter
public class PaymentMethodManagerEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idPaymentMethodManager`", nullable = false)
    private Integer idPaymentMethodManager;
    @ManyToOne
    @JoinColumn(name = "`idBankAccount`", nullable = false)
    private BankAccountEntity bankAccount;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idCheque`")
    private ChequeBookEntity chequeBook;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "`idCard`")
    private CardEntity card;

    public PaymentMethodManagerEntity(
            BankAccountEntity bankAccount,
            ChequeBookEntity chequeBook,
            CardEntity card
    ) {
        this.bankAccount = bankAccount;
        this.chequeBook = chequeBook;
        this.card = card;
    }

    public PaymentMethodManagerEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethodManagerEntity that = (PaymentMethodManagerEntity) o;
        return Objects.equals(idPaymentMethodManager, that.idPaymentMethodManager) &&
                Objects.equals(bankAccount, that.bankAccount) &&
                Objects.equals(chequeBook, that.chequeBook) &&
                Objects.equals(card, that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPaymentMethodManager, bankAccount, chequeBook, card);
    }
}
