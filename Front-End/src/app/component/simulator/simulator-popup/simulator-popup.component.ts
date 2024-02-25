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
  selector: 'app-simulator-classic-popup',
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
  templateUrl: './simulator-popup.component.html',
  styleUrl: './simulator-classic-classic-popup.component.css'
})
export class SimulatorPopupComponent {

    constructor(@Inject(MAT_DIALOG_DATA) public data: { message: string }) {}
}
