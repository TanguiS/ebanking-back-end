package fr.ensicaen.pi.gpss.backend.simulation.generate_transaction;

import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationComponent;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationConfiguration;
import fr.ensicaen.pi.gpss.backend.data_management.iban.BankAccountIdentificationLength;
import fr.ensicaen.pi.gpss.backend.database.enumerate.CountryCode;
import fr.ensicaen.pi.gpss.backend.tools.StringOperation;
import org.springframework.lang.NonNull;

import java.util.Random;

public class RandomBankAccountIdentificationComponent {
    private final Random randomizer;
    private final BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration;
    private static final CountryCode[] COUNTRY_CODES_POOL = CountryCode.values();

    public RandomBankAccountIdentificationComponent(
            @NonNull Random randomizer,
            @NonNull BankAccountIdentificationConfiguration bankAccountIdentificationConfiguration
    ) {
        this.randomizer = randomizer;
        this.bankAccountIdentificationConfiguration = bankAccountIdentificationConfiguration;
    }

    private String shuffleCountryCode() {
        return COUNTRY_CODES_POOL[randomizer.nextInt(0, COUNTRY_CODES_POOL.length)].label();
    }

    private String shuffleBranchCode() {
        return StringOperation.generateRandomStringWithNumberBaseOnSeed(
                BankAccountIdentificationLength.BRANCH_CODE.length(), randomizer
        );
    }

    private String shuffleBankCode() {
        return StringOperation.generateRandomStringWithNumberBaseOnSeed(
                BankAccountIdentificationLength.BANK_CODE.length(), randomizer
        );
    }

    public String shuffle() {
        final String countryCode = shuffleCountryCode();
        final String branchCode = shuffleBranchCode();
        final String bankCode = shuffleBankCode();
        return bankAccountIdentificationConfiguration.fromComponent(
                new BankAccountIdentificationComponent(bankCode, branchCode, countryCode)
        ).getIban();
    }
}
