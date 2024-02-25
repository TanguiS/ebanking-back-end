import {Component, Inject} from '@angular/core';
import {FlexModule} from "@angular/flex-layout";
import {MatButtonModule} from "@angular/material/button";
import {
    MAT_DIALOG_DATA,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle
} from "@angular/material/dialog";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-classic-popup',
  standalone: true,
  imports: [
    FlexModule,
    MatButtonModule,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    RouterLink
  ],
  templateUrl: './classic-popup.component.html',
  styleUrl: './classic-popup.component.css'
})
export class ClassicPopupComponent {

    constructor(@Inject(MAT_DIALOG_DATA) public data: { title: string, message: string }) {}
}
