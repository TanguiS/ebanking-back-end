import { Injectable } from '@angular/core';
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {BeneficiaryDTO} from "../../DTO/beneficiary/Beneficiary.dto";
import {map, Observable} from "rxjs";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {PreFilledCardInformationDTO} from "../../DTO/card_product/PreFilledCardInformation.dto";

@Injectable({
  providedIn: 'root'
})
export class AddCardFormObserverService {
    private url: string = RequestToServerToolsService.standardURL + "/" + this.cookieToolService.getRole() + "/add/request/card"

    constructor(private http: HttpClient,
                private cookieToolService: CookieToolService,
                private requestToServerToolsService: RequestToServerToolsService) { }

    addCard(idClient: number, idCurrentBankAccount: number, idCardProduct: number, data: PreFilledCardInformationDTO): Observable<ResponseDTO> {
        const headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        const requestURL: string = this.url + "/" + idClient + "/" + idCurrentBankAccount + "/" + idCardProduct
        return this.http
            .post<ResponseDTO>(requestURL, data,
                { headers: headers, observe: "response" })
            .pipe(
                map((response: HttpResponse<ResponseDTO>): ResponseDTO => {
                        return <ResponseDTO>response.body
                    }
                )
            );
    }
}
