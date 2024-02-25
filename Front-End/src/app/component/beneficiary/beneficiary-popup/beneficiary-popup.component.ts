import {Component, Input, ViewChild} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatButtonModule} from "@angular/material/button";
import {
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle
} from "@angular/material/dialog";
import {RouterLink} from "@angular/router";
import {BeneficiaryFormComponent} from "../beneficiary-form/beneficiary-form.component";
import {FlexModule} from "@angular/flex-layout";

@Component({
    selector: 'app-beneficiary-popup',
    standalone: true,
    imports: [
        FormsModule,
        MatTooltipModule,
        ReactiveFormsModule,
        MatButtonModule,
        MatDialogActions,
        MatDialogClose,
        MatDialogContent,
        MatDialogTitle,
        RouterLink,
        BeneficiaryFormComponent,
        FlexModule
    ],
  templateUrl: './beneficiary-popup.component.html',
  styleUrl: './beneficiary-popup.component.css'
})
export class BeneficiaryPopupComponent {
    @Input() idCurrentBankAccount: number = -1
    @ViewChild('beneficiaryFormComponent') form: BeneficiaryFormComponent | undefined
    constructor() {}

    onSubmit() {
        this.form?.onSubmit()
    }
}
