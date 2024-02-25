package fr.ensicaen.pi.gpss.backend.database.dto.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.beneficiary.BeneficiaryToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.DirectDebitToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.PaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestPaymentMethodManagerDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.transaction.TransactionToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
@Valid
public class BankAccountDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idBankAccount;
    private String iban;
    private String rib;
    @PositiveOrZero
    private Integer amount;
    private BankAccountType bankAccountType;
    private List<TransactionToBankAccountDTO> transactions;
    private List<RequestPaymentMethodManagerDTO> paymentMethodRequests;
    private List<PaymentMethodManagerDTO> paymentMethods;
    private List<BeneficiaryToBankAccountDTO> beneficiaries;
    private List<DirectDebitToBankAccountDTO> directDebits;
}
