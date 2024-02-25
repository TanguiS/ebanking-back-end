package fr.ensicaen.pi.gpss.backend.database.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption.Pan;
import fr.ensicaen.pi.gpss.backend.database.dto.TemplateDTO;
import fr.ensicaen.pi.gpss.backend.database.enumerate.AccountingDirection;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Currency;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionStatus;
import fr.ensicaen.pi.gpss.backend.database.enumerate.TransactionType;
import fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption.Iban;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Valid
public class TransactionDTO extends TemplateDTO {
    @PositiveOrZero
    private Integer idTransaction;
    @PositiveOrZero
    private Integer amount;
    private Currency currency;
    private Timestamp transactionDate;
    private TransactionType transactionType;
    private AccountingDirection accountingDirection;
    private Timestamp clearedDate;
    @Pan
    private String pan;
    @Iban
    private String iban;
    private TransactionStatus transactionStatus;
    //NOT ENTITY
    private String transactionActor;
    @PositiveOrZero
    private Integer id;
    private Boolean isEnsiBank;
}
