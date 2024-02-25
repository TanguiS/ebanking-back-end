package fr.ensicaen.pi.gpss.backend.service.manage_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.BankAccountType;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestChequeBookMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestPaymentMethodManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.request_payment_method.RequestPaymentMethodManagerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ManageRequestChequeBookService {
    private final RequestChequeBookMapper requestChequeBookMapper;
    private final RequestPaymentMethodManagerMapper requestPaymentMethodManagerMapper;
    private final RequestPaymentMethodManagerRepository requestPaymentMethodManagerRepository;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;

    public ManageRequestChequeBookService(
            RequestChequeBookMapper requestChequeBookMapper,
            RequestPaymentMethodManagerMapper requestPaymentMethodManagerMapper,
            RequestPaymentMethodManagerRepository requestPaymentMethodManagerRepository,
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository
    ) {
        this.requestChequeBookMapper = requestChequeBookMapper;
        this.requestPaymentMethodManagerMapper = requestPaymentMethodManagerMapper;
        this.requestPaymentMethodManagerRepository = requestPaymentMethodManagerRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
    }

    public boolean doesBankAccountAlreadyHaveARequest(@Valid @NotNull BankAccountDTO bankAccount) {
        return bankAccountRepository.countNotOrderedRequestChequeBookByBankAccount(
                bankAccountMapper.toEntity(bankAccount)
        ) != 0;
    }

    public boolean doesBankAccountIsNotACurrentOne(@Valid @NotNull BankAccountDTO bankAccount) {
        return bankAccount.getBankAccountType() != BankAccountType.CURRENT_ACCOUNT;
    }

    public RequestChequeBookDTO addRequest(@Valid @NotNull BankAccountDTO bankAccount) {
        requestPaymentMethodManagerRepository.save(
                requestPaymentMethodManagerMapper.toEntity(
                        requestPaymentMethodManagerMapper.toNewChequeBookRequestManager(
                                requestChequeBookMapper.toNew(), bankAccount
                        )
                )
        );
        return requestChequeBookMapper.toDTO(
                bankAccountRepository.findLatestChequeBookRequestByBankAccount(
                        bankAccountMapper.toEntity(
                                bankAccount
                        )
                )
        );
    }
}
