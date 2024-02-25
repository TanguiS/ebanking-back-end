import {HeaderButtonHandler} from "./handler-button";
import {Role} from "../tool/role";
import {ButtonType} from "./buttonType";

export class SimulatorButtonHandler extends HeaderButtonHandler{
    constructor(nextHandler: HeaderButtonHandler | undefined = undefined) {
        super(nextHandler, [Role.SIMULATOR])
    }

    override handleHeaderButton(buttonType: ButtonType, currentUserRole: Role): boolean {
        if (buttonType == ButtonType.SIMULATOR) {
            return this.allowedRoleList.includes(currentUserRole)
        }
        return super.handleHeaderButton(buttonType, currentUserRole)
    }
}
