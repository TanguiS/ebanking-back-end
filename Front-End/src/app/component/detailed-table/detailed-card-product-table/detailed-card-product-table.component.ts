import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {BeneficiaryPopupButtonComponent} from "../../beneficiary/beneficiary-popup-button/beneficiary-popup-button.component";
import {BlockCardUserComponent} from "../../block-card-user/block-card-user.component";
import {CurrencyPipe, NgIf} from "@angular/common";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTableModule} from "@angular/material/table";
import {CardProductDTO} from "../../../DTO/card_product/CardProduct.dto";
import {PageTitleComponent} from "../../page-title/page-title.component";
import {CardProductsTableComponent} from "../../card-products-table/card-products-table.component";
import {SoftwareCardDTO} from "../../../DTO/card_product/SoftwareCard.dto";
import {AuthorizationPolicyDTO} from "../../../DTO/card_product/AuthorizationPolicyDTO";
import {MatListModule} from "@angular/material/list";
import {MatGridListModule} from "@angular/material/grid-list";
import {CookieToolService} from "../../../tool/cookie-tool.service";
import {FlexModule} from "@angular/flex-layout";
import {UtilService} from "../../../tool/util.service";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-detailed-card-product-table',
    standalone: true,
    imports: [
        BeneficiaryPopupButtonComponent,
        BlockCardUserComponent,
        CurrencyPipe,
        MatExpansionModule,
        MatTableModule,
        NgIf,
        PageTitleComponent,
        CardProductsTableComponent,
        MatListModule,
        MatGridListModule,
        FlexModule
    ],
    templateUrl: './detailed-card-product-table.component.html',
    styleUrl: './detailed-card-product-table.component.css'
})
export class DetailedCardProductTableComponent implements OnInit {
    title: string = "Card product"
    @Input() isMainComponent: boolean = true
    amountFormat: string = '1.2-2'

    idCardProduct: number | undefined
    cardProduct: CardProductDTO | undefined
    softwaresCard: SoftwareCardDTO[] = []
    softwaresColumns: string[] = ["id", "cardScheme", "priorityUseLevel"]
    authorizationPolicies: AuthorizationPolicyDTO[] = []
    authorizationPoliciesColumns: string[] = ["id", "type", "priorityUseLevel", "requirement"]

    @ViewChild("cardProductTable") cardProductTable: CardProductsTableComponent | undefined

    constructor(private cookieToolService: CookieToolService,
                private utilService: UtilService,
                private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.params.subscribe(params => {
            this.idCardProduct = params["idCardProduct"]
        })
        this.utilService.setCardProductSelectedIntoSessionStorage(this.cookieToolService.getBankerDashboardInformation()?.cardProducts!, this.idCardProduct!)

        this.cardProduct = this.cookieToolService.getCardProductSelected()
        this.cardProductTable?.assignListOfDTO([this.cardProduct!])
        this.assignListOfDTO()
    }

    assignListOfDTO() {
        this.softwaresCard = this.cardProduct ? this.cardProduct.softwareCards : []
        this.authorizationPolicies = this.cardProduct ? this.cardProduct.authorizationPolicies : []
    }
}
