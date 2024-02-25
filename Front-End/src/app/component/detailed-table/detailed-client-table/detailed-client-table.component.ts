import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {BeneficiaryPopupButtonComponent} from "../../beneficiary/beneficiary-popup-button/beneficiary-popup-button.component";
import {BlockCardUserComponent} from "../../block-card-user/block-card-user.component";
import {CurrencyPipe, NgIf} from "@angular/common";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTableModule} from "@angular/material/table";
import {UserInformationDTO} from "../../../DTO/user_information/UserInformation.dto";
import {CookieToolService} from "../../../tool/cookie-tool.service";
import {ClientDashboardDTO} from "../../../DTO/client/ClientDashboard.dto";
import {BankAccountsTableComponent} from "../../bank-accounts-table/bank-accounts-table.component";
import {MatDividerModule} from "@angular/material/divider";
import {PageTitleComponent} from "../../page-title/page-title.component";
import {ActivatedRoute, Router, Routes} from "@angular/router";
import {UtilService} from "../../../tool/util.service";

@Component({
  selector: 'app-detailed-client-table',
  standalone: true,
    imports: [
        BeneficiaryPopupButtonComponent,
        BlockCardUserComponent,
        CurrencyPipe,
        MatExpansionModule,
        MatTableModule,
        NgIf,
        BankAccountsTableComponent,
        MatDividerModule,
        PageTitleComponent
    ],
  templateUrl: './detailed-client-table.component.html',
  styleUrl: './detailed-client-table.component.css'
})
export class DetailedClientTableComponent implements OnInit {
    @Input() isMainComponent: boolean = true
    protected title: string = "Client Dashboard"

    idClient: number | undefined
    clientInformationColumns: string[] = [
        "id",
        "firstName",
        "lastName",
        "phoneNumber",
        "email"
    ]
    clientInformation: UserInformationDTO | undefined

    constructor(private cookieToolService: CookieToolService,
                private utilService: UtilService,
                private route: ActivatedRoute) {}

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            this.idClient = params["idAccount"]
        })
        this.clientInformation = this.getClientInformationSelected()
        this.utilService.setClientInformationSelectedIntoSessionStorage(this.cookieToolService.getBankerDashboardInformation()?.clients!, this.idClient!)
    }

    private getClientInformationSelected() {
        const bankerDashboard = this.cookieToolService.getBankerDashboardInformation()
        const client: ClientDashboardDTO | undefined = bankerDashboard?.clients.find((x: ClientDashboardDTO) => {
            return x.client.idUser == this.idClient
        })
        return client?.client
    }
}
