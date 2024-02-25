package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum Role implements DBEnumerateFormat {
    ROLE_CLIENT(1, "ROLE_CLIENT", "Usual client"),
    ROLE_BANKER(2, "ROLE_BANKER", "Usual banker"),
    ROLE_ACCOUNTANT(3, "ROLE_ACCOUNTANT", "Accountant"),
    ROLE_SIMULATOR(4, "ROLE_SIMULATOR", "Has access to simulation API to test"),
    ROLE_TRANSACTION_COLLECTOR(
            5, "ROLE_TRANSACTION_COLLECTOR", "Has access to the transaction's collection"
    ),
    ROLE_MASTER(6, "ROLE_MASTER", "Giga chad MASTER"),
    ROLE_ATM(7, "ROLE_ATM", "Automated teller machine manager");

    private final int id;
    private final String name;
    private final String description;

    Role(int id, String name, String description) {
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
