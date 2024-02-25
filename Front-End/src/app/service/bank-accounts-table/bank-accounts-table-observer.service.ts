import { Injectable } from '@angular/core';
import {map, Observable} from "rxjs";
import {BankAccountDTO} from "../../DTO/bank_account/BankAccount.dto";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";

@Injectable({
  providedIn: 'root'
})
export class BankAccountsTableObserverService {
    url: string ="http://localhost:8080/client/dashboard"

    constructor(
        private cookieTool: CookieToolService,
        private http: HttpClient,
        private requestToServerToolsService: RequestToServerToolsService
    ) { }

    getAccounts(): Observable<BankAccountDTO[]> {
        let headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        return this.http.get<ResponseDTO>(this.url, { headers: headers, observe: 'response' }).pipe(
            map((response: HttpResponse<ResponseDTO>): BankAccountDTO[] => {
                this.cookieTool.setBankAccountsInformation(response.body?.interactionResponse.data)
                return response.body?.interactionResponse.data as BankAccountDTO[];
            })
        );
    }
}
