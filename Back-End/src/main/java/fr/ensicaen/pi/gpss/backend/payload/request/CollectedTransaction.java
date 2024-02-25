package fr.ensicaen.pi.gpss.backend.payload.request;

import fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption.IbanOrPan;
import fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Currency;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.lang.NonNull;

import java.util.Objects;

public record CollectedTransaction (
        @Valid @NotNull @PositiveOrZero Integer id,
        @Valid @NotNull AccountingDirection accountingDirection,
        @Valid @NotNull TransactionType transactionType,
        @Valid @Positive Integer amount,
        @Valid @NotNull Currency currency,
        @Valid @NotBlank @IbanOrPan String transactionActor,
        @Valid @NotBlank String transactionDate
) {

    public boolean isAccountingOpposite(@NonNull CollectedTransaction oppositeTransaction) {
        if (this.equals(oppositeTransaction)) {
            return false;
        }
        if (!amount.equals(oppositeTransaction.amount())) {
            return false;
        }
        if (!transactionType.equals(oppositeTransaction.transactionType())) {
            return false;
        }
        if (!currency.equals(oppositeTransaction.currency())) {
            return false;
        }
        if (!transactionDate.equals(oppositeTransaction.transactionDate())) {
            return false;
        }
        boolean isThisDebitAndOppositeIsCredit = accountingDirection == AccountingDirection.DEBIT &&
                oppositeTransaction.accountingDirection() == AccountingDirection.CREDIT;
        boolean isThisCreditAndOppositeIsDebit = accountingDirection == AccountingDirection.CREDIT &&
                oppositeTransaction.accountingDirection() == AccountingDirection.DEBIT;
        return isThisDebitAndOppositeIsCredit || isThisCreditAndOppositeIsDebit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectedTransaction that = (CollectedTransaction) o;
        return Objects.equals(id, that.id) &&
                accountingDirection == that.accountingDirection &&
                transactionType == that.transactionType &&
                Objects.equals(amount, that.amount) &&
                currency == that.currency &&
                Objects.equals(transactionActor, that.transactionActor) &&
                Objects.equals(transactionDate, that.transactionDate);
    }

    @Override
    public String toString() {
        return "CollectedTransaction{" +
                "_id=" + id +
                ", _accountingDirection=" + accountingDirection +
                ", _transactionType=" + transactionType +
                ", _amount=" + amount +
                ", _currency=" + currency +
                ", _transactionActor='" + transactionActor + '\'' +
                ", _transactionDate='" + transactionDate + '\'' +
                '}';
    }
}
