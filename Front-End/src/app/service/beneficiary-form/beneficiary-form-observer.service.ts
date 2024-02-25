import { Injectable } from '@angular/core';
import {map, Observable} from "rxjs";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {BeneficiaryDTO} from "../../DTO/beneficiary/Beneficiary.dto";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";

@Injectable({
  providedIn: 'root'
})
export class BeneficiaryFormObserverService {
    private _url: string = RequestToServerToolsService.standardURL + "/client/add/beneficiary/"

    constructor(private http: HttpClient,
                private requestToServerToolsService: RequestToServerToolsService) { }

    addBeneficiary(beneficiaryInformation: BeneficiaryDTO, idCurrentBankAccount: number): Observable<ResponseDTO> {
        const headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        const requestURL: string = this._url + idCurrentBankAccount
        return this.http
            .post<ResponseDTO>(requestURL, beneficiaryInformation,
                {headers: headers, observe: 'response'})
            .pipe(map((response): ResponseDTO => {
                return <ResponseDTO>response.body
            }))
    }
}
