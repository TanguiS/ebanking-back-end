package fr.ensicaen.pi.gpss.backend.service.account;

import fr.ensicaen.pi.gpss.backend.database.dto.account.BankAccountDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.card_product.CardProductDTO;
import fr.ensicaen.pi.gpss.backend.database.dto.user.UserInformationDTO;
import fr.ensicaen.pi.gpss.backend.database.mapper.account.BankAccountMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.card_product.CardProductMapper;
import fr.ensicaen.pi.gpss.backend.database.mapper.user.UserInformationMapper;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.account.BankerAccountRepository;
import fr.ensicaen.pi.gpss.backend.database.repository.card_product.CardProductRepository;
import fr.ensicaen.pi.gpss.backend.payload.response.banker.BankerDashboardDTO;
import fr.ensicaen.pi.gpss.backend.payload.response.banker.BankersClientDashboardDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
@Service
public class BankerAccountService {
    private final UserInformationMapper userInformationMapper;
    private final BankerAccountRepository bankerAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final CardProductMapper cardProductMapper;
    private final CardProductRepository cardProductRepository;

    public BankerAccountService(
            UserInformationMapper userInformationMapper,
            BankerAccountRepository bankerAccountRepository,
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            CardProductMapper cardProductMapper,
            CardProductRepository cardProductRepository
    ) {
        this.userInformationMapper = userInformationMapper;
        this.bankerAccountRepository = bankerAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.cardProductMapper = cardProductMapper;
        this.cardProductRepository = cardProductRepository;
    }

    public BankerDashboardDTO findAllBankersClient(@Valid @NotNull UserInformationDTO banker) {
        List<BankersClientDashboardDTO> clients = bankerAccountRepository.findAllClientByBankerUser(
                        userInformationMapper.toEntity(banker)
                ).stream()
                .flatMap(client -> {
                    UserInformationDTO clientDTO = userInformationMapper.toBasicDashboard(client);
                    List<BankAccountDTO> bankAccounts = bankAccountRepository.findAllByUserInformation(
                                    userInformationMapper.toEntity(clientDTO)
                            ).stream()
                            .map(bankAccountMapper::toDashboard)
                            .toList();
                    return Stream.of(new BankersClientDashboardDTO(clientDTO, bankAccounts));
                })
                .collect(Collectors.toCollection(ArrayList::new));
        List<CardProductDTO> productCards = cardProductRepository.findAll().stream()
                .map(cardProductMapper::toDTO)
                .toList();
        return new BankerDashboardDTO(clients, productCards);
    }

    public boolean isIdClientNotOneOfTheBankersClient(
            @Valid @NotNull UserInformationDTO banker,
            @Valid @NotNull @PositiveOrZero Integer idClient
    ) {
        return bankerAccountRepository.findAllClientByBankerUser(
                        userInformationMapper.toEntity(banker)
                ).stream()
                .noneMatch(value -> Objects.equals(value.getIdUser(), idClient));
    }

    public UserInformationDTO resolveClient(
            @Valid @NotNull UserInformationDTO banker,
            @Valid @NotNull @PositiveOrZero Integer idClient
    ) {
        return userInformationMapper.toDTO(
                bankerAccountRepository.findAllClientByBankerUser(
                                userInformationMapper.toEntity(banker)
                        ).stream()
                        .filter(value -> Objects.equals(value.getIdUser(), idClient))
                        .findAny()
                        .orElseThrow(IllegalArgumentException::new)
        );
    }
}
