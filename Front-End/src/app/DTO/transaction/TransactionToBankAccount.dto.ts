import {TransactionDTO} from "./Transaction.dto";
import {BankAccountDTO} from "../bank_account/BankAccount.dto";

export interface TransactionToBankAccountDTO {
    idTransactionToBankAccount: number;
    transaction: TransactionDTO;
    bankAccount: BankAccountDTO;
}
