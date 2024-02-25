import { Injectable } from '@angular/core';
import {map, Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {CookieToolService} from "../../tool/cookie-tool.service";

@Injectable({
  providedIn: 'root'
})
export class BlockCardUserObserverService {
    url: string = RequestToServerToolsService.standardURL + "/" + this.cookieToolService.getRole() + "/block/card/"

    constructor(private http: HttpClient,
              private cookieToolService: CookieToolService,
              private requestToServerToolsService: RequestToServerToolsService) { }

    blockCard(idCard: number, idClient?: number): Observable<ResponseDTO> {
        const headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        const requestURL: string = idClient ? this.url + idClient + "/" + idCard : this.url + idCard
        return this.http
            .patch<ResponseDTO>(requestURL, {},
                { headers: headers, observe: "response" })
            .pipe(
                map((response: HttpResponse<ResponseDTO>): ResponseDTO => {
                        return <ResponseDTO>response.body
                    }
                )
            );
    }
}
