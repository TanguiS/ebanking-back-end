export interface TransactionDTO {
    transactionType: string;
    amount: number;
    transactionStatus: string;
    accountingDirection: string;
    currency: string;
    transactionDate: Date;
    idTransaction: number;
}
