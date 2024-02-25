package fr.ensicaen.pi.gpss.backend.simulation.generate_transaction;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberComponent;
import fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberConfiguration;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CardScheme;
import fr.ensicaen.pi.gpss.backend.tools.StringOperation;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberLength.BIN;
import static fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberLength.BIN_CARD_SCHEME;

public class RandomPrimaryAccountNumberComponent {
    private final Random randomizer;
    private final PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;
    private final RandomBankAccountIdentificationComponent ibanRandomizer;
    private static final List<CardScheme> CARD_SCHEMES_POOL = Arrays.stream(CardScheme.values())
            .filter(value -> value != CardScheme.CB)
            .toList();

    public RandomPrimaryAccountNumberComponent(
            @NonNull Random randomizer,
            @NonNull PrimaryAccountNumberConfiguration primaryAccountNumberConfiguration,
            @NonNull BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration
            ) {
        this.randomizer = randomizer;
        this.primaryAccountNumberConfiguration = primaryAccountNumberConfiguration;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
        ibanRandomizer = new RandomBankAccountIdentificationComponent(
                randomizer, bankAccountIdentificationConfiguration
        );
    }

    private String shuffleBinCardScheme() {
        return String.valueOf(CARD_SCHEMES_POOL.get(randomizer.nextInt(0, CARD_SCHEMES_POOL.size())).id());
    }

    private String shufflePostBin() {
        return StringOperation.generateRandomStringWithNumberBaseOnSeed(
                BIN.length() - BIN_CARD_SCHEME.length(), randomizer
        );
    }

    public String shuffle() {
        final String binCardScheme = shuffleBinCardScheme();
        final String postBin = shufflePostBin();
        final PrimaryAccountNumberComponent component = new PrimaryAccountNumberComponent(binCardScheme, postBin);
        String shuffled = ibanRandomizer.shuffle();
        return primaryAccountNumberConfiguration.fromComponent(
                component,
                bankAccountIdentificationConfiguration.fromExistingIban(shuffled)
        ).getPan();
    }
}
