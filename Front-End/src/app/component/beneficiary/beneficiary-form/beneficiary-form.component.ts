import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {BeneficiaryDTO} from "../../../DTO/beneficiary/Beneficiary.dto";
import {FlexModule} from "@angular/flex-layout";
import {BeneficiaryFormObserverService} from "../../../service/beneficiary-form/beneficiary-form-observer.service";
import {catchError, throwError} from "rxjs";
import {UtilService} from "../../../tool/util.service";
import {ResponseDTO} from "../../../DTO/response/Response.dto";

@Component({
  selector: 'app-beneficiary-form',
  standalone: true,
    imports: [
        FormsModule,
        MatTooltipModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        FlexModule
    ],
  templateUrl: './beneficiary-form.component.html',
  styleUrl: './beneficiary-form.component.css'
})
export class BeneficiaryFormComponent {
    protected beneficiaryForm: FormGroup
    private beneficiaryInformation: BeneficiaryDTO = {} as BeneficiaryDTO
    @Input() idCurrentBankAccount: number = -1

    constructor(private formBuilder: FormBuilder,
                private utilService: UtilService,
                private beneficiaryFormObserverService: BeneficiaryFormObserverService) {
        this.beneficiaryForm = this.formBuilder.group({
            firstName: '',
            lastName: '',
            IBAN: ''
        });
    }

    getBeneficiaryInformation():void {
        this.beneficiaryInformation.firstName = this.beneficiaryForm.get("firstName")!.value
        this.beneficiaryInformation.lastName = this.beneficiaryForm.get("lastName")!.value
        this.beneficiaryInformation.iban = this.beneficiaryForm.get("IBAN")!.value
    }

    addBeneficiary(): void {
        this.utilService.openWaitingPopupDialog()
        this.beneficiaryFormObserverService.addBeneficiary(this.beneficiaryInformation, this.idCurrentBankAccount)
            .pipe(
                catchError(data => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Add Beneficiary", data.error.interactionResponse.data)
                    return throwError(() => Error(data.error.interactionResponse.data))
                })
            )
            .subscribe(
                (data: ResponseDTO): void => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Add Beneficiary", data.message)
                    return
                })
    }

    onSubmit(): void {
        this.getBeneficiaryInformation()
        this.addBeneficiary()
    }
}
