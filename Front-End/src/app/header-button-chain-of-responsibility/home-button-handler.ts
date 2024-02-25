import {HeaderButtonHandler} from "./handler-button";
import {ButtonType} from "./buttonType";
import {Role} from "../tool/role";

export class HomeButtonHandler extends HeaderButtonHandler{
    constructor(nextHandler: HeaderButtonHandler | undefined = undefined) {
        super(nextHandler, [Role.ACCOUNTANT, Role.BANKER, Role.CLIENT, Role.MASTER, Role.COLLECTOR, Role.SIMULATOR, Role.NO_ROLE])
    }

    override handleHeaderButton(buttonType: ButtonType, currentUserRole: Role): boolean {
        if (buttonType == ButtonType.HOME) {
            return this.allowedRoleList.includes(currentUserRole)
        }
        return super.handleHeaderButton(buttonType, currentUserRole)
    }
}
