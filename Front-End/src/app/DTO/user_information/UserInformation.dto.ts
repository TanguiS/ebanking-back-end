import {RoleDTO} from "./Role.dto";

export interface UserInformationDTO {
    idUser: number;
    firstName: string;
    lastName: string;
    role: RoleDTO;
    phoneNumber: string;
    gender: number;
    email: string;
    password: string;
}
