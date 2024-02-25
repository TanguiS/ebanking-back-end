import {RequestPaymentMethodStatusDTO} from "./RequestPaymentMethodStatus.dto";

export interface RequestCardDTO {
    idRequestCard: number;
    requestPaymentMethodStatus: RequestPaymentMethodStatusDTO;
}
