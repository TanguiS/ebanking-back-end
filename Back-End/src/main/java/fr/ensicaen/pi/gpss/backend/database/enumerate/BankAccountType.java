package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum BankAccountType implements DBEnumerateFormat {
    YOUTH_PASSBOOK(0, "YOUTH_PASSBOOK", "bank account designed specifically for young individuals, typically adolescents or young adults."),
    SAVING_ACCOUNT(1, "SAVING_ACCOUNT", "basic type of bank account that is widely used by individuals to deposit and save money securely."),
    CURRENT_ACCOUNT(2, "CURRENT_ACCOUNT", "also known as a checking account in some regions, is a type of bank account that is designed for frequent and everyday financial transactions."),
    ATM_ACCOUNT(3, "ATM_ACCOUNT", "basic type of ATM account");

    private final int id;
    private final String name;
    private final String description;

    BankAccountType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String label() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }
}
