import { Injectable } from '@angular/core';
import {map, Observable} from "rxjs";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {UserInformationDTO} from "../../DTO/user_information/UserInformation.dto";

@Injectable({
  providedIn: 'root'
})
export class LoginFormObserverService {
    _url: string = RequestToServerToolsService.standardURL + "/login"

    constructor(private http: HttpClient,
                private cookieToolService:CookieToolService) { }

    logInUser(userLogin: UserInformationDTO): Observable<void> {
        return this.http.post<ResponseDTO>(this._url, userLogin, {observe: 'response'})
            .pipe(map((response: HttpResponse<ResponseDTO>): void => {
                this.cookieToolService.setToken(<string>response.headers.get('Token'))
                this.cookieToolService.setUserInformation(response.body?.interactionResponse.data)
            })
        )
    }
}
