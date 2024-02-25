import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FlexLayoutModule } from '@angular/flex-layout';
import {UserInformationDTO} from "../../DTO/user_information/UserInformation.dto";
import {CookieToolService} from "../../tool/cookie-tool.service";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, FlexLayoutModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
    public title: string = "Home"
    protected _firstName: string | undefined = undefined
    protected _lastName: string | undefined = undefined
    constructor(private cookieTool: CookieToolService) {
    }

    ngOnInit(): void {
        this.updateUserInformation()
    }

    protected isUserLoggedIn(): boolean {
        return this.cookieTool.isTokenSet();
    }
    public updateUserInformation(): void {
        if (this.isUserLoggedIn()) {
            let user: UserInformationDTO = this.cookieTool.getUserInformation()
            this._firstName = user.firstName
            this._lastName = user.lastName
        }
    }
}
