package fr.ensicaen.pi.gpss.backend.data_management.pan;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.Nullable;

public class PrimaryAccountNumberComponent {
    private final String bin;
    private final Integer checkLuhn;

    public PrimaryAccountNumberComponent(
            @NotBlank String binCardScheme,
            @NotBlank String postBin,
            @Nullable Integer checkLuhn
    ) {
        bin = binCardScheme + postBin;
        if (checkLuhn != null && checkLuhn < 0) {
            throw new IllegalArgumentException("If check Luhn is not null it has to be >= 0");
        }
        this.checkLuhn = checkLuhn;
    }

    public PrimaryAccountNumberComponent(@NotBlank String biNCardScheme, @NotBlank String postBin) {
        this(biNCardScheme, postBin, null);
    }

    @NotBlank
    public String getBin() {
        return bin;
    }

    @Nullable
    public Integer getCheckLuhn() {
        return checkLuhn;
    }
}
