import {ButtonType} from "./buttonType";
import {Role} from "../tool/role";

export abstract class HeaderButtonHandler {
    protected nextHandler!: HeaderButtonHandler | undefined
    protected allowedRoleList!: Role[]

    protected constructor(nextHandler: HeaderButtonHandler | undefined, allowedRoleList: Role[]) {
        this.nextHandler = nextHandler
        this.allowedRoleList = allowedRoleList
    }

    public handleHeaderButton(buttonType: ButtonType, currentUserRole: Role): boolean {
        if (this.nextHandler == undefined) {
            return false
        }
        return this.nextHandler.handleHeaderButton(buttonType, currentUserRole)
    }

}
