import {
    AfterContentChecked,
    Component,
    ElementRef,
    HostListener, Injectable, OnDestroy, OnInit,
    ViewChild
} from '@angular/core';
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import { HeaderService } from '../../../service/header/header.service';
import {CookieToolService} from "../../../tool/cookie-tool.service";
import {UserInformationDTO} from "../../../DTO/user_information/UserInformation.dto";

@Injectable({
    providedIn: 'root',
})

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, AfterContentChecked, OnDestroy {
    private _titleContainerWidth: number = -1;
    private _buttonsContainerWidth: number = -1;
    public _firstName: string | undefined = undefined
    public _lastName: string | undefined = undefined

    @ViewChild('titleContainer')
    _titleContainer!: ElementRef;
    @ViewChild('buttonsContainer')
    _buttonsContainer!: ElementRef;
    @ViewChild('spacerLarge')
    _spacerLarge!: ElementRef;

    constructor(
        private matIconRegistry: MatIconRegistry,
        private domSanitizer: DomSanitizer,
        private headerService: HeaderService,
        protected cookieTool: CookieToolService
    ) {
        this.matIconRegistry.addSvgIcon(
            "ensicaen-logo",
            this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/Ensicaen-logo-2017.svg")
        );
    }

    ngOnInit(): void {
        this.headerService.setHeaderComponent(this);
    }

    ngAfterContentChecked(): void {
        this.getTitleContainerSize()
        this.getButtonsContainerSize()
        this.updateUserInformation()
    }

    ngOnDestroy() {
        this.cookieTool.removeAll()
    }

    @HostListener('window:resize', ['$event'])
    onResize(): void {
        this.getTitleContainerSize()
        this.getButtonsContainerSize()
    }

    isSmallScreen(): boolean {
        if (this._buttonsContainerWidth != -1 && this._titleContainerWidth != -1) {
            return window.innerWidth < this._buttonsContainerWidth + this._titleContainerWidth + 32;
        }
        return false
    }

    private getButtonsContainerSize(): void {
        const buttonsContainerElement: HTMLElement = this._buttonsContainer?.nativeElement;
        this._buttonsContainerWidth = buttonsContainerElement? buttonsContainerElement.offsetWidth : this._buttonsContainerWidth
    }

    private getTitleContainerSize(): void {
        const titleContainerElement: HTMLElement = this._titleContainer?.nativeElement;
        this._titleContainerWidth = titleContainerElement?.offsetWidth
    }

    protected isUserLoggedIn(): boolean {
        return this.cookieTool.isTokenSet();
    }

    private updateUserInformation(): void {
        if (this.isUserLoggedIn()) {
            let user: UserInformationDTO = this.cookieTool.getUserInformation()
            this._firstName = user.firstName
            this._lastName = user.lastName
        }
    }
}
