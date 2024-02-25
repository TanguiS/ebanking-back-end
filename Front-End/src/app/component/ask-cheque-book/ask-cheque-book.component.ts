import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {HandleLoadRequestService} from "../../tool/handle-load-request.service";
import {AskChequeBookObservableService} from "../../service/ask-cheque-book/ask-cheque-book-observable.service";
import {catchError, Observable, throwError} from "rxjs";
import {MatTooltipModule} from "@angular/material/tooltip";
import {UtilService} from "../../tool/util.service";
import {ResponseDTO} from "../../DTO/response/Response.dto";

@Component({
  selector: 'app-ask-cheque-book',
  standalone: true,
    imports: [
        MatButtonModule,
        MatTooltipModule
    ],
  templateUrl: './ask-cheque-book.component.html',
  styleUrl: './ask-cheque-book.component.css'
})
export class AskChequeBookComponent {
    @Output() protected eventAskChequeBook: EventEmitter<boolean> = new EventEmitter<boolean>()
    @Input() idBankAccount!: number
    @Input() isButtonDisabled: boolean = false

    constructor(private handleLoadRequestService: HandleLoadRequestService,
                private utilService: UtilService,
                private askChequeBookObservableService: AskChequeBookObservableService) {
    }

    protected onClick(): void {
        this.utilService.openWaitingPopupDialog()
        let observable: Observable<ResponseDTO> = this.handleLoadRequestService.loadRequest<ResponseDTO>(
            this.askChequeBookObservableService.askChequeBook(this.idBankAccount));
        observable
            .pipe(
                catchError(data => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Add Cheque Book", data.error.interactionResponse.data)
                    return throwError(() => Error(data.error))
                })
            )
            .subscribe( (data: ResponseDTO) => {
                this.utilService.closeDialog()
                this.utilService.openPopupDialog( "Add Cheque Book", data.message)
                this.eventAskChequeBook.emit(true)
            })
    }
}
