package fr.ensicaen.pi.gpss.backend.database.entity.account;

import fr.ensicaen.pi.gpss.backend.database.entity.beneficiary.BeneficiaryToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.direct_debit.DirectDebitToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.payment_method.PaymentMethodManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.request_payment_method.RequestPaymentMethodManagerEntity;
import fr.ensicaen.pi.gpss.backend.database.entity.transaction.TransactionToBankAccountEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "BankAccount")
@Table(name = "`BankAccount`", schema = "`Account`")
@Getter
@Setter
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "`idBankAccount`")
    private Integer idBankAccount;
    @Column(name = "`IBAN`")
    private String iban;
    @Column(name = "`RIB`")
    private String rib;
    @Basic
    @Column(name = "`Amount`")
    private Integer amount;
    @Basic
    @Column(name = "`BankAccountType`")
    @Enumerated(EnumType.STRING)
    @Type(PostgreSQLEnumType.class)
    private BankAccountType bankAccountType;
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<TransactionToBankAccountEntity> transactions = new ArrayList<>();
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<RequestPaymentMethodManagerEntity> paymentMethodRequests = new ArrayList<>();
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<PaymentMethodManagerEntity> paymentMethods = new ArrayList<>();
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<BeneficiaryToBankAccountEntity> beneficiaries = new ArrayList<>();
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<DirectDebitToBankAccountEntity> directDebits = new ArrayList<>();

    public BankAccountEntity(
            String iban,
            String rib,
            Integer amount,
            BankAccountType bankAccountType,
            List<TransactionToBankAccountEntity> transactions,
            List<RequestPaymentMethodManagerEntity> paymentMethodRequests,
            List<PaymentMethodManagerEntity> paymentMethods,
            List<BeneficiaryToBankAccountEntity> beneficiaries,
            List<DirectDebitToBankAccountEntity> directDebits
    ) {
        this.iban = iban;
        this.rib = rib;
        this.amount = amount;
        this.bankAccountType = bankAccountType;
        this.transactions = transactions;
        this.paymentMethodRequests = paymentMethodRequests;
        this.paymentMethods = paymentMethods;
        this.beneficiaries = beneficiaries;
        this.directDebits = directDebits;
    }

    public BankAccountEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountEntity that = (BankAccountEntity) o;
        return Objects.equals(idBankAccount, that.idBankAccount) &&
                Objects.equals(iban, that.iban) &&
                Objects.equals(rib, that.rib) &&
                Objects.equals(amount, that.amount) &&
                bankAccountType == that.bankAccountType &&
                Objects.equals(transactions, that.transactions) &&
                Objects.equals(paymentMethodRequests, that.paymentMethodRequests) &&
                Objects.equals(paymentMethods, that.paymentMethods) &&
                Objects.equals(beneficiaries, that.beneficiaries) &&
                Objects.equals(directDebits, that.directDebits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                idBankAccount,
                iban,
                rib,
                amount,
                bankAccountType,
                transactions,
                paymentMethodRequests,
                paymentMethods,
                beneficiaries,
                directDebits
        );
    }
}
