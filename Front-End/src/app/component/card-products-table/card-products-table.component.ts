import {Component, Inject, Input, LOCALE_ID, OnInit, ViewChild} from '@angular/core';
import {CardProductDTO} from "../../DTO/card_product/CardProduct.dto";
import {MatTable, MatTableModule} from "@angular/material/table";
import {UtilService} from "../../tool/util.service";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {UserInformationDTO} from "../../DTO/user_information/UserInformation.dto";
import {PageTitleComponent} from "../page-title/page-title.component";
import {Role} from "../../tool/role";
import {Path} from "../../tool/path";
import {ActivatedRoute, Router} from "@angular/router";


@Component({
  selector: 'app-card-products-table',
  standalone: true,
    imports: [
        MatTableModule,
        PageTitleComponent
    ],
  templateUrl: './card-products-table.component.html',
  styleUrl: './card-products-table.component.css'
})
export class CardProductsTableComponent implements OnInit {
    protected title: string = 'Card product'
    @Input() isMainComponent: boolean = true
    @Input() cardProducts: CardProductDTO[] = []
    @Input() clickable: boolean = true

    cardProductsColumns: string[] = ['idCard', 'name', 'cardType']

    @ViewChild("cardProductsTable") cardProductsTable: MatTable<any> | undefined

    constructor(
        @Inject(LOCALE_ID) public locale: string,
        private utilService: UtilService,
        private cookieTool: CookieToolService,
        private router: Router) {
    }

    ngOnInit(): void {
        if (this.utilService.resetSessionIfNoToken()) {
            return
        }
        this.refreshTable()
    }

    public assignListOfDTO(data: CardProductDTO[]): void {
        this.cardProducts = Object.assign([], data)
    }

    public refreshTable() {
        this.utilService.refreshTable(this.cardProductsTable)
    }

    protected navigateToDetailedCardTable(cardProductId: any): void {
        this.router.navigate(["/" + this.cookieTool.getRole() + "/" + Path.CARD_PRODUCT , cardProductId]).then();
    }
}
