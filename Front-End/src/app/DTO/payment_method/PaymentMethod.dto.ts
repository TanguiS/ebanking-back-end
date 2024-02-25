import {CardDTO} from "./Card.dto";

export interface PaymentMethodDTO {
    idPaymentMethodManager: number;
    card: CardDTO;
}
