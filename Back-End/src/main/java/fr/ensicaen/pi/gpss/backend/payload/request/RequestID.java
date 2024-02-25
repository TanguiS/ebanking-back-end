package fr.ensicaen.pi.gpss.backend.payload.request;

public enum RequestID {
    REGISTER(0, "Register", "Request to register a new user to the Bank DB " +
            "and put it in the pending status"),
    LOGIN(1, "Login", "Request to Login to the Bank DB"),
    COLLECTED_TRANSACTION(201, "Collected Transaction", "Request to collect the transactions"),
    GENERATE_TRANSACTION(301, "Generate Transaction", "Request to generate random transaction " +
            "to simulate a ebanking Front End"),
    SIMULATE_TRANSACTION_COLLECTION(302, "Simulate Transaction Collection",
            "Request to simulation a transaction collection, transactions will be deleted after that"),
    GET_ALL_ACCOUNT_USER_INFORMATION(2, "Get All Account User Information", "Request to get all account user information"),
    GET_USER_NAMES(3, "Get User's Name", "Request to get the name of the user"),
    BLOCK_USER_CARD(4, "Block User Card", "Request to block a users card"),
    ADD_USER_BENEFICIARY(5, "Add User Beneficiary", "Request to add a new beneficiary to user's bank account"),
    ADD_REQUEST_CHEQUEBOOK_USER(6, "Add User Request Cheque Book", "Request to add a new cheque book request to user bank account"),
    GET_BANKER_DASHBOARD(50, "Get Banker Dashboard", "Request to get the banker dashboard"),
    BLOCK_USER_CARD_FROM_BANKER(51, "Block User Card From Banker Request", "Request to block a user's card from banker demand"),
    ADD_USER_BANK_ACCOUNT(52, "Add A New Bank Account", "Request to add a new bank account to a specified banker's client"),
    ADD_REQUEST_CARD_FROM_BANKER(53, "Add A New request card", "Request to add a new card request to a specified banker's client"),
    GET_ACCOUNTANT_DASHBOARD(100, "Get Accountant dashboard", "Request to show accountant dashboard"),
    GET_USER_MANAGER_DASHBOARD(401, "Get User Manager Dashboard", "Request to show user manager dashboard"),
    UPDATE_USER_CREDENTIAL(402, "Path User Credential", "Request to update user's credential"),
    UNRESOLVED_MAPPING(-1, "Unresolved Mapping", "Request generated an error from a unresolved mapping");

    public final int id;
    public final String label;
    public final String description;

    RequestID(int id, String label, String description) {
        this.id = id;
        this.label = label;
        this.description = description;
    }

    public int id() {
        return id;
    }

    public String label() {
        return label;
    }

    public String description() {
        return description;
    }
}
