import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {map, Observable} from "rxjs";
import {SimulatorTransactionDTO} from "../../DTO/transaction/SimulatorTransaction.dto";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";

@Injectable({
  providedIn: 'root'
})
export class SimulatorObserverService {
    _url: string = RequestToServerToolsService.standardURL + "/simulation/transaction/"

    constructor(private http: HttpClient,
                private requestToServerToolsService:RequestToServerToolsService) { }

    generateTransactionSimulation(selectedNumberOfTransactions: number): Observable<SimulatorTransactionDTO[]> {
        const headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        const requestURL: string = this._url + "generate/collectable/" + selectedNumberOfTransactions
        return this.http
            .get<ResponseDTO>(requestURL,
                { headers: headers, observe: "response" })
            .pipe(
                map((response: HttpResponse<ResponseDTO>): SimulatorTransactionDTO[] => {
                        return <SimulatorTransactionDTO[]>RequestToServerToolsService.extractDataFromServerResponse(response)
                    }
                )
            );
    }

    runTransactionSimulation(data: SimulatorTransactionDTO[]): Observable<ResponseDTO> {
        const headers: HttpHeaders = this.requestToServerToolsService.getStandardHeadersForRequest()
        const requestURL: string = this._url + "run/collectable"
        return this.http
            .post<ResponseDTO>(requestURL , data,
                { headers: headers, observe: "response" })
            .pipe(
                map((response: HttpResponse<ResponseDTO>): ResponseDTO => {
                    return <ResponseDTO>response.body
                })
            );
    }
}
