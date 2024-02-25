import {Component} from '@angular/core';
import {Path} from "../../../tool/path";
import {MatButtonModule} from "@angular/material/button";
import {NgClass, NgIf} from "@angular/common";
import {NavigationEnd, Router, RouterLink} from "@angular/router";
import {CookieToolService} from "../../../tool/cookie-tool.service";
import {ButtonType} from "../../../header-button-chain-of-responsibility/buttonType";
import {HandlerButtonService} from "../../../service/header-button-chain-of-responsibility/handler-button.service";

@Component({
  selector: 'app-navigation-buttons-list',
  standalone: true,
    imports: [
        MatButtonModule,
        NgIf,
        RouterLink,
        NgClass
    ],
  templateUrl: './navigation-buttons-list.component.html',
  styleUrl: './navigation-buttons-list.component.css'
})
export class NavigationButtonsListComponent {
    private _currentRoute: string = ''
    constructor(protected cookieTool: CookieToolService,
                private router: Router,
                protected handlerButtonService: HandlerButtonService) {
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this._currentRoute = event.urlAfterRedirects;
            }
        });
    }

    isCurrentPage(page: string): boolean {
        return this._currentRoute.includes(page);
    }

    isUserLoggedIn(): boolean {
        return this.cookieTool.isTokenSet();
    }

    logOutUser(): void {
        this.router.navigate([Path.HOME]).then( () => {
            this.cookieTool.removeAll()
        })

    }

    protected readonly Path = Path;
    protected readonly ButtonType = ButtonType;
}
