import { Injectable } from '@angular/core';
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {UserInformationDTO} from "../../DTO/user_information/UserInformation.dto";

@Injectable({
  providedIn: 'root'
})
export class RegistrationFormObserverService {
    _url: string = RequestToServerToolsService.standardURL + "/register"

    constructor(private http: HttpClient) { }

    registerUser(userData: UserInformationDTO): Observable<ResponseDTO> {
        return this.http.post<ResponseDTO>(this._url, userData, {observe: 'response'})
            .pipe(
                map((response: HttpResponse<ResponseDTO>): ResponseDTO => {
                    return <ResponseDTO>response.body
                })
            )
    }
}
