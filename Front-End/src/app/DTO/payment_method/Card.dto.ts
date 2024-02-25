import {CardInformationDTO} from "../card_product/CardInformation.dto";

export interface CardDTO {
    idCard: number;
    cardInformation: CardInformationDTO;
    cardStatus: string;
}
