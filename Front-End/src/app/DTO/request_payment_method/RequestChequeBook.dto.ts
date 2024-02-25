import {RequestPaymentMethodStatusDTO} from "./RequestPaymentMethodStatus.dto";

export interface RequestChequeBookDTO {
    idRequestChequeBook: number;
    requestPaymentMethodStatus: RequestPaymentMethodStatusDTO;
}
