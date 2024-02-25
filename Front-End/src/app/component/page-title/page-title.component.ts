import {Component, Input} from '@angular/core';
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-page-title',
  standalone: true,
    imports: [
        NgIf
    ],
  templateUrl: './page-title.component.html',
  styleUrl: './page-title.component.css'
})
export class PageTitleComponent {
    @Input() title: string | undefined
    @Input() isMainComponent: boolean = true
}
