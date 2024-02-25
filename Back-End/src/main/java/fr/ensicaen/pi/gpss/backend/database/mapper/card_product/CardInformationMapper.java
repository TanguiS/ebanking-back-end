package fr.ensicaen.pi.gpss.backend.database.mapper.card_product;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentification;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumber;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberConfiguration;
import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.SoftwareCardDTO;
import fr.ensicaen.pi.gpss.backend.database.entity.card_product.CardInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardScheme;
import fr.ensicaen.pi.gpss.backend.database.mapper.DTOEntityMapper;
import fr.ensicaen.pi.gpss.backend.payload.request.PreFilledCardInformation;
import fr.ensicaen.pi.gpss.backend.tools.StringOperation;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

@Component
@Validated
public class CardInformationMapper implements DTOEntityMapper<CardInformationDTO, CardInformationEntity> {
    private final CardProductMapper cardProductMapper;
    private final PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;

    public CardInformationMapper(
            CardProductMapper cardProductMapper,
            PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration,
            BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration
    ) {
        this.cardProductMapper = cardProductMapper;
        this.primaryAccountNumberConfiguration = primaryAccountNumberConfiguration;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
    }

    @Override
    public CardInformationDTO toDTO(CardInformationEntity entity) {
        if (entity == null) {
            return null;
        }
        return CardInformationDTO.builder()
                .idCardInformation(entity.getIdCardInformation())
                .pan(entity.getPan())
                .cvx2(entity.getCvx2())
                .expirationCardDate(entity.getExpirationCardDate())
                .cardProduct(cardProductMapper.toDTO(entity.getCardProduct()))
                .upperLimitPerMonth(entity.getUpperLimitPerMonth())
                .upperLimitPerTransaction(entity.getUpperLimitPerTransaction())
                .build();
    }

    @Override
    public CardInformationEntity toEntity(CardInformationDTO dto) {
        if (dto == null) {
            return null;
        }
        CardInformationEntity entity = new CardInformationEntity(
                dto.getPan(),
                dto.getExpirationCardDate(),
                dto.getCvx2(),
                dto.getUpperLimitPerMonth(),
                dto.getUpperLimitPerTransaction(),
                cardProductMapper.toEntity(dto.getCardProduct())
        );
        entity.setIdCardInformation(dto.getIdCardInformation());
        return entity;
    }

    public CardInformationDTO toNew(
            @NotNull CardProductDTO cardProduct,
            @NotNull BankAccountDTO bankAccount,
            @NotNull PreFilledCardInformation preFilledCardInformation
    ) {
        Optional<SoftwareCardDTO> cardSchemeSoftware = cardProduct.getSoftwareCards().stream()
                .filter(dto -> dto.getCardScheme() != CardScheme.CB)
                .findFirst();
        if (cardSchemeSoftware.isEmpty()) {
            throw new IllegalArgumentException("Card Product is not a Valid one");
        }
        BankAccountIdentification iban = bankAccountIdentificationConfiguration.fromExistingIban(bankAccount.getIban());

        PrimaryAccountNumber pan = primaryAccountNumberConfiguration.newEnsiBankPanFromCardScheme(
                cardSchemeSoftware.get().getCardScheme(), iban
        );

        Integer cvx2 = Integer.valueOf(StringOperation.generateRandomStringWithNumber(3));

        return CardInformationDTO.builder()
                .pan(pan.getEncryptedPan())
                .cvx2(cvx2)
                .expirationCardDate(Timestamp.valueOf(
                        LocalDate.now()
                            .plusYears(preFilledCardInformation.expirationDateInYears())
                            .plusDays(1)
                            .atStartOfDay())
                )
                .cardProduct(cardProduct)
                .upperLimitPerMonth(preFilledCardInformation.upperLimitPerMonth())
                .upperLimitPerTransaction(preFilledCardInformation.upperLimitPerTransaction())
                .build();
    }

    public CardInformationDTO toDashBoard(@NotNull CardInformationEntity entity) {
        PrimaryAccountNumber pan = primaryAccountNumberConfiguration.fromExistingPan(entity.getPan());
        return CardInformationDTO.builder()
                .idCardInformation(entity.getIdCardInformation())
                .pan(
                        String.format("****%s",
                                pan.getPan().substring(pan.getPan().length() - 5, pan.getPan().length() - 1))
                )
                .expirationCardDate(entity.getExpirationCardDate())
                .upperLimitPerMonth(entity.getUpperLimitPerMonth())
                .upperLimitPerTransaction(entity.getUpperLimitPerTransaction())
                .build();
    }
}
