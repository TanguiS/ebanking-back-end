package fr.ensicaen.pi.gpss.backend.data_management.pan;

public enum PrimaryAccountNumberShift {
    MAX_DIGIT(9),
    EVEN(2),
    MODULUS(10);

    private final int shift;

    PrimaryAccountNumberShift(int shift) {
        this.shift = shift;
    }

    public int shift() {
        return shift;
    }
}
