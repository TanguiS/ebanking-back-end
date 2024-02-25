import {Injectable} from '@angular/core';
import {HeaderButtonHandler} from "../../header-button-chain-of-responsibility/handler-button";
import {HomeButtonHandler} from "../../header-button-chain-of-responsibility/home-button-handler";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {ButtonType} from "../../header-button-chain-of-responsibility/buttonType";
import {Role} from "../../tool/role";
import {SimulatorButtonHandler} from "../../header-button-chain-of-responsibility/simulator-button-handler";
import {DashboardButtonHandler} from "../../header-button-chain-of-responsibility/dashboard-button-handler";

@Injectable({
  providedIn: 'root'
})
export class HandlerButtonService {
    private headerButtonHandler: HeaderButtonHandler
    constructor(private cookieToolService: CookieToolService) {
        this.headerButtonHandler = new HomeButtonHandler(new DashboardButtonHandler(new SimulatorButtonHandler()))
    }
    public ShouldDisplayButton(buttonType: ButtonType): boolean {
        const role: Role = <Role>this.cookieToolService.getRole()
        return this.headerButtonHandler.handleHeaderButton(buttonType, role);
    }
}
