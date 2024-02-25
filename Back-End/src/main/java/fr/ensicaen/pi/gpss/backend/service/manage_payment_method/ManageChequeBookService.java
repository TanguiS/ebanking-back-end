package fr.ensicaen.pi.gpss.backend.service.manage_payment_method;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.payment_method.ChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.request_payment_method.RequestChequeBookDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.payment_method.ChequeBookMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.payment_method.PaymentMethodManagerMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.request_payment_method.RequestChequeBookMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.payment_method.PaymentMethodManagerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ManageChequeBookService {
    private final ChequeBookMapper chequeBookMapper;
    private final RequestChequeBookMapper requestChequeBookMapper;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentMethodManagerMapper paymentMethodManagerMapper;
    private final PaymentMethodManagerRepository paymentMethodManagerRepository;

    public ManageChequeBookService(
            ChequeBookMapper chequeBookMapper,
            RequestChequeBookMapper requestChequeBookMapper,
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            PaymentMethodManagerMapper paymentMethodManagerMapper,
            PaymentMethodManagerRepository paymentMethodManagerRepository
    ) {
        this.chequeBookMapper = chequeBookMapper;
        this.requestChequeBookMapper = requestChequeBookMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.paymentMethodManagerMapper = paymentMethodManagerMapper;
        this.paymentMethodManagerRepository = paymentMethodManagerRepository;
    }

    public ChequeBookDTO addNewOnBankReceived(@Valid @NotNull RequestChequeBookDTO requestChequeBook) {
        BankAccountDTO bankAccount = bankAccountMapper.toDTO(
                bankAccountRepository.findByRequestChequeBook(
                        requestChequeBookMapper.toEntity(
                                requestChequeBook
                        )
                )
        );
        ChequeBookDTO chequeBook = chequeBookMapper.toNew();
        paymentMethodManagerRepository.save(
                paymentMethodManagerMapper.toEntity(
                        paymentMethodManagerMapper.toNewChequeBookManager(
                                chequeBook, bankAccount
                        )
                )
        );
        return chequeBook;
    }

    public List<ChequeBookDTO> addNewsOnBankReceived(
            @Valid @NotEmpty List<RequestChequeBookDTO> requestsChequeBook
    ) {
        return requestsChequeBook.stream().map(this::addNewOnBankReceived).toList();
    }
}
