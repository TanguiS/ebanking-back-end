import {InteractionResponseDTO} from "./InteractionResponse.dto";

export interface ResponseDTO {
    message: string;
    interactionResponse: InteractionResponseDTO;
    status: number;
    timestamp: Date;
}
