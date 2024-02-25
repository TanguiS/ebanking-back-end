package fr.ensicaen.pi.gpss.backend.service.add;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.DirectDebitDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.direct_debit.DirectDebitToBankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.direct_debit.DirectDebitToBankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.direct_debit.DirectDebitToBankAccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddDirectDebitService {
    private final DirectDebitToBankAccountMapper directDebitToBankAccountMapper;
    private final DirectDebitToBankAccountRepository directDebitToBankAccountRepository;

    public AddDirectDebitService(
            DirectDebitToBankAccountMapper directDebitToBankAccountMapper,
            DirectDebitToBankAccountRepository directDebitToBankAccountRepository
    ) {
        this.directDebitToBankAccountMapper = directDebitToBankAccountMapper;
        this.directDebitToBankAccountRepository = directDebitToBankAccountRepository;
    }

    public DirectDebitDTO addNew(
            @Valid @NotNull DirectDebitDTO preFilledDirectDebit,
            @Valid @NotNull BankAccountDTO bankAccount
    ) {
        DirectDebitToBankAccountDTO newDTO = directDebitToBankAccountMapper.toNew(preFilledDirectDebit, bankAccount);
        DirectDebitDTO output = newDTO.getDirectDebit();
        directDebitToBankAccountRepository.save(
                directDebitToBankAccountMapper.toEntity(newDTO)
        );
        return output;
    }
}
