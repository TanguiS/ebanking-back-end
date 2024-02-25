import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {UtilService} from "../../../tool/util.service";

@Component({
  selector: 'app-add-card-popup-button',
  standalone: true,
    imports: [
        MatButtonModule
    ],
  templateUrl: './add-card-popup-button.component.html',
  styleUrl: './add-card-popup-button.component.css'
})
export class AddCardPopupButtonComponent {
    @Input() idClient: number | undefined
    @Input() idCurrentBankAccount: number | undefined
    @Output() eventCardAdded: EventEmitter<boolean> = new EventEmitter<boolean>()

    constructor(private utilService: UtilService) {}

    protected addCard() {
        if (typeof this.idClient === "number"
        && typeof this.idCurrentBankAccount === "number") {
            const closeEvent = this.utilService.openAddCardPopupDialog(this.idClient, this.idCurrentBankAccount)
            closeEvent.subscribe(() => {
                this.eventCardAdded.emit(true)
            })
        }
    }
}
