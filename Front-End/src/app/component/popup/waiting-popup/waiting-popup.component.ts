import { Component } from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {RouterLink} from "@angular/router";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {FlexModule} from "@angular/flex-layout";

@Component({
  selector: 'app-waiting-popup',
  standalone: true,
    imports: [
        MatButtonModule,
        MatDialogActions,
        MatDialogClose,
        MatDialogContent,
        MatDialogTitle,
        RouterLink,
        MatProgressSpinnerModule,
        FlexModule
    ],
  templateUrl: './waiting-popup.component.html',
  styleUrl: './waiting-popup.component.css'
})
export class WaitingPopupComponent {

}
