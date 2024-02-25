import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {UtilService} from "../../../tool/util.service";

@Component({
  selector: 'app-beneficiary-popup-button',
  standalone: true,
  imports: [
    MatButtonModule
  ],
  templateUrl: './beneficiary-popup-button.component.html',
  styleUrl: './beneficiary-popup-button.component.css'
})
export class BeneficiaryPopupButtonComponent {
    @Input() idCurrentBankAccount: number | undefined
    @Output() eventBeneficiaryAdded: EventEmitter<boolean> = new EventEmitter<boolean>()

    constructor(private utilService: UtilService) {}

    protected addBeneficiary() {
        if (typeof this.idCurrentBankAccount === "number") {
            const closeEvent = this.utilService.openBeneficiaryPopupDialog(this.idCurrentBankAccount)
            closeEvent.subscribe(() => {
                this.eventBeneficiaryAdded.emit(true)
            })
        }
    }
}
