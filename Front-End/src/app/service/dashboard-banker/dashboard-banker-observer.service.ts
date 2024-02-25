import { Injectable } from '@angular/core';
import {CookieToolService} from "../../tool/cookie-tool.service";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {map, Observable} from "rxjs";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {Path} from "../../tool/path";
import {BankerDashboardDTO} from "../../DTO/banker/BankerDashboard.dto";
import {Role} from "../../tool/role";

@Injectable({
  providedIn: 'root'
})
export class DashboardBankerObserverService {
    url: string = Path.STANDARD + "/" + Role.BANKER + "/" + Path.DASHBOARD

    constructor(
        private cookieTool: CookieToolService,
        private http: HttpClient,
        private requestToServerToolsService: RequestToServerToolsService
    ) { }

    getDashboard(): Observable<BankerDashboardDTO> {
        let headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        return this.http.get<ResponseDTO>(this.url, { headers: headers, observe: 'response' }).pipe(
            map((response: HttpResponse<ResponseDTO>): BankerDashboardDTO => {
                this.cookieTool.setBankerDashboardInformation(response.body?.interactionResponse.data)
                return response.body?.interactionResponse.data as BankerDashboardDTO;
            })
        );
    }
}
