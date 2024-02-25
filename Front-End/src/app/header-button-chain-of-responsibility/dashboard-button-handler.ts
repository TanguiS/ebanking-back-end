import {HeaderButtonHandler} from "./handler-button";
import {Role} from "../tool/role";
import {ButtonType} from "./buttonType";

export class DashboardButtonHandler extends HeaderButtonHandler {
    constructor(nextHandler: HeaderButtonHandler | undefined = undefined) {
        super(nextHandler, [Role.CLIENT, Role.BANKER])
    }

    override handleHeaderButton(buttonType: ButtonType, currentUserRole: Role): boolean {
        if (buttonType == ButtonType.DASHBOARD) {
            return this.allowedRoleList.includes(currentUserRole)
        }
        return super.handleHeaderButton(buttonType, currentUserRole)
    }
}
