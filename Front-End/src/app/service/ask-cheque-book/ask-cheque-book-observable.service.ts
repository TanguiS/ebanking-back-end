import { Injectable } from '@angular/core';
import {Path} from "../../tool/path";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {ResponseDTO} from "../../DTO/response/Response.dto";

@Injectable({
  providedIn: 'root'
})
export class AskChequeBookObservableService {
    private url: string = Path.STANDARD + "/" + this.cookieToolService.getRole() + "/add/request/chequebook/"
    constructor(private requestToServerToolsService: RequestToServerToolsService,
              private http: HttpClient,
              private cookieToolService: CookieToolService) {}

    askChequeBook(bankAccountID: number, idClient?: number): Observable<ResponseDTO> {
        const requestURL: string = idClient ? this.url + idClient + "/" + bankAccountID : this.url + bankAccountID
        const headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        return this.http.get<ResponseDTO>(requestURL, { headers: headers, observe: 'response' }).pipe(
            map((response: HttpResponse<ResponseDTO>): ResponseDTO => {
                return <ResponseDTO>response.body
            })
        );
    }
}
