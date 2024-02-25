import {Component, Input, ViewChild} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {FlexModule} from "@angular/flex-layout";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {AddCardFormComponent} from "../add-card-form/add-card-form.component";

@Component({
  selector: 'app-add-card-popup',
  standalone: true,
    imports: [
        MatButtonModule,
        FlexModule,
        MatDialogActions,
        MatDialogClose,
        MatDialogContent,
        MatDialogTitle,
        AddCardFormComponent
    ],
  templateUrl: './add-card-popup.component.html',
  styleUrl: './add-card-popup.component.css'
})
export class AddCardPopupComponent {
    @Input() idClient: number = -1
    @Input() idCurrentBankAccount: number = -1
    @ViewChild('addCardFormComponent') form: AddCardFormComponent | undefined
    constructor() {}

    onSubmit() {
        this.form?.onSubmit()
    }
}
