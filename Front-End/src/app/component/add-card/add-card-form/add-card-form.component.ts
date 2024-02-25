import {Component, Input, OnInit} from '@angular/core';
import {FlexModule} from "@angular/flex-layout";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {PreFilledCardInformationDTO} from "../../../DTO/card_product/PreFilledCardInformation.dto";
import {AddCardFormObserverService} from "../../../service/add-card-form/add-card-form-observer.service";
import {MatSelectModule} from "@angular/material/select";
import {CookieToolService} from "../../../tool/cookie-tool.service";
import {CardProductDTO} from "../../../DTO/card_product/CardProduct.dto";
import {catchError, throwError} from "rxjs";
import {UtilService} from "../../../tool/util.service";
import {ResponseDTO} from "../../../DTO/response/Response.dto";

@Component({
  selector: 'app-add-card-form',
  standalone: true,
    imports: [
        FlexModule,
        FormsModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSelectModule
    ],
  templateUrl: './add-card-form.component.html',
  styleUrl: './add-card-form.component.css'
})
export class AddCardFormComponent implements OnInit {
    protected addCardForm: FormGroup
    private addCardInformation: PreFilledCardInformationDTO = {} as PreFilledCardInformationDTO
    @Input() idClient: number = -1
    @Input() idCurrentBankAccount: number = -1
    idCardProduct: number = -1
    cardProducts: {name: string, id: number}[] = [];

    constructor(private formBuilder: FormBuilder,
                private cookieToolService: CookieToolService,
                private utilService: UtilService,
                private addCardFormObserverService: AddCardFormObserverService) {
        this.addCardForm = this.formBuilder.group({
            idCardProductSelected: undefined,
            expirationDateInYears: undefined,
            upperLimitPerMonth: undefined,
            upperLimitPerTransaction: undefined
        });
    }

    ngOnInit() {
        const cardProductsInformation: CardProductDTO[] = Object.assign([], this.cookieToolService.getCardProductsInformation())
        for (let cardProduct of cardProductsInformation) {
            this.cardProducts.push({name: cardProduct.name, id: cardProduct.idCardProduct})
        }
    }

    getNewCardInformation():void {
        this.idCardProduct = this.addCardForm.get("idCardProductSelected")!.value
        this.addCardInformation.expirationDateInYears = this.addCardForm.get("expirationDateInYears")!.value
        this.addCardInformation.upperLimitPerMonth = this.addCardForm.get("upperLimitPerMonth")!.value
        this.addCardInformation.upperLimitPerTransaction = this.addCardForm.get("upperLimitPerTransaction")!.value
    }

    addCard(): void {
        this.addCardFormObserverService.addCard(
                this.idClient,
            this.idCurrentBankAccount,
            this.idCardProduct,
            this.addCardInformation)
            .pipe(
                catchError(data => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Add Card", data.error.interactionResponse.data)
                    return throwError(() => Error(data.interactionResponse.data))
                })
            )
            .subscribe(
                (data: ResponseDTO): void => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Add Card", data.message)
                })
    }

    onSubmit(): void {
        this.getNewCardInformation()
        this.addCard()
    }
}
