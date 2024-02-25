import {RequestCardDTO} from "./RequestCard.dto";
import {RequestChequeBookDTO} from "./RequestChequeBook.dto";

export interface PaymentMethodRequestDTO {
    idRequestPaymentMethodManager: number;
    requestCard: RequestCardDTO;
    requestChequeBook: RequestChequeBookDTO;
}
