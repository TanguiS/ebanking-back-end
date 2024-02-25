import { Injectable } from '@angular/core';
import {UtilService} from "./util.service";
import {catchError, Observable, ObservableInput, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class HandleLoadRequestService {

    constructor(private utilService: UtilService) { }

    public loadRequest<Type>(funct: Observable<any>): Observable<Type> {
        return funct.pipe(
            catchError((err: any, caught: Observable<any>): ObservableInput<any> => {
                console.error("Error loading data from request")
                console.error(err)
                throw err
            }),
            tap(() => {
            })
        );
    }
}
