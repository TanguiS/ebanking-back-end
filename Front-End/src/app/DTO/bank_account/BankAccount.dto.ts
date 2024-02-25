import {PaymentMethodRequestDTO} from "../request_payment_method/PaymentMethodRequest.dto";
import {PaymentMethodDTO} from "../payment_method/PaymentMethod.dto";
import {TransactionToBankAccountDTO} from "../transaction/TransactionToBankAccount.dto";
import {DirectDebitToBankAccountDTO} from "../direct_debit/DirectDebitToBankAccount.dto";
import {BeneficiaryToBankAccountDTO} from "../beneficiary/BeneficiaryToBankAccount.dto";

export interface BankAccountDTO {
    amount: number;
    iban: string;
    directDebits: DirectDebitToBankAccountDTO[];
    bankAccountType: string;
    paymentMethodRequests: PaymentMethodRequestDTO[];
    paymentMethods: PaymentMethodDTO[];
    rib: string;
    idBankAccount: number;
    transactions: TransactionToBankAccountDTO[];
    beneficiaries: BeneficiaryToBankAccountDTO[];
}
