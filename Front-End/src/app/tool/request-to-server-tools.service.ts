import { Injectable } from '@angular/core';
import {HttpHeaders, HttpResponse} from "@angular/common/http";
import {CookieToolService} from "./cookie-tool.service";
import {ResponseDTO} from "../DTO/response/Response.dto";
import {Path} from "./path";


@Injectable({
  providedIn: 'root'
})
export class RequestToServerToolsService {

    constructor(
        private cookieTool: CookieToolService
    ) {
    }
    static standardURL: string = Path.STANDARD
    static extractDataFromServerResponse(response: HttpResponse<ResponseDTO>): Object {
        return response.body?.interactionResponse.data
    }

    static extractStringDataFromServerResponse(response: HttpResponse<ResponseDTO>): string {
        return response.body?.interactionResponse.data
    }

    getStandardHeadersForRequest(): HttpHeaders{
        return new HttpHeaders().set('Token', this.cookieTool.getToken())
    }
}
