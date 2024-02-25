import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {BlockCardUserObserverService} from "../../service/block-card-user/block-card-user-observer.service";
import {UtilService} from "../../tool/util.service";
import {catchError, throwError} from "rxjs";
import {ResponseDTO} from "../../DTO/response/Response.dto";

@Component({
  selector: 'app-block-card-user',
  standalone: true,
    imports: [
        MatButtonModule
    ],
  templateUrl: './block-card-user.component.html',
  styleUrl: './block-card-user.component.css'
})
export class BlockCardUserComponent {
    @Input() idCard: number | undefined
    @Input() idClient: number | undefined
    @Output() eventCardBlocked: EventEmitter<boolean> = new EventEmitter<boolean>()

    constructor(private blockCardUserObserverService: BlockCardUserObserverService,
                private utilService: UtilService){
    }
    onClick(): void {
        this.utilService.openWaitingPopupDialog()
        if (this.idCard) {

            this.blockCardUserObserverService.blockCard(this.idCard, this.idClient)
                .pipe(
                    catchError(data => {
                        this.utilService.closeDialog()
                        this.utilService.openPopupDialog( "Block card", data.error.interactionResponse.data)
                        return throwError(() => Error(data.error))
                    })
                )
                .subscribe( (data: ResponseDTO) => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Block card", data.message)
                    this.eventCardBlocked.emit(true)
            })
        }
    }
}
