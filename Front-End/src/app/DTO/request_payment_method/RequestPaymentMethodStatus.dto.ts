export interface RequestPaymentMethodStatusDTO {
    userReceivedPaymentMethodDate: Date;
    idRequestPaymentMethodStatus: number;
    bankRequestPaymentMethodDate: Date;
    bankReceivedPaymentMethodDate: Date;
    orderStatus: string;
    userRequestPaymentMethodDate: Date;
}
