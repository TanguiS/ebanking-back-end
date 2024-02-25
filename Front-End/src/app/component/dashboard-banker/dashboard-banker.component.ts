import {AfterViewInit, Component, Inject, Input, LOCALE_ID, OnInit, ViewChild} from '@angular/core';
import {MatTable, MatTableModule} from "@angular/material/table";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {UtilService} from "../../tool/util.service";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {UserInformationDTO} from "../../DTO/user_information/UserInformation.dto";
import {DashboardBankerObserverService} from "../../service/dashboard-banker/dashboard-banker-observer.service";
import {BankerDashboardDTO} from "../../DTO/banker/BankerDashboard.dto";
import {Observable} from "rxjs";
import {HandleLoadRequestService} from "../../tool/handle-load-request.service";
import {BeneficiaryPopupButtonComponent} from "../beneficiary/beneficiary-popup-button/beneficiary-popup-button.component";
import {BlockCardUserComponent} from "../block-card-user/block-card-user.component";
import {CurrencyPipe, NgIf} from "@angular/common";
import {MatExpansionModule} from "@angular/material/expansion";
import {ClientDashboardDTO} from "../../DTO/client/ClientDashboard.dto";
import {CardProductDTO} from "../../DTO/card_product/CardProduct.dto";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {PageTitleComponent} from "../page-title/page-title.component";
import {CardProductsTableComponent} from "../card-products-table/card-products-table.component";
import {Path} from "../../tool/path";
import {Router} from "@angular/router";
import {MatRippleModule} from "@angular/material/core";

@Component({
  selector: 'app-dashboard-banker',
  standalone: true,
    imports: [
        MatTableModule,
        BeneficiaryPopupButtonComponent,
        BlockCardUserComponent,
        CurrencyPipe,
        MatExpansionModule,
        NgIf,
        PageTitleComponent,
        CardProductsTableComponent,
        MatRippleModule
    ],
  templateUrl: './dashboard-banker.component.html',
  styleUrl: './dashboard-banker.component.css'
})
export class DashboardBankerComponent implements OnInit {
    @Input() isMainComponent: boolean = true
    protected title: string = 'Dashboard'

    clientColumns: string[] = ['idUser', 'firstName', 'lastName']
    bankerDashboard: BankerDashboardDTO = {} as BankerDashboardDTO
    protected clientDashboards: ClientDashboardDTO[] = []
    protected cardProducts: CardProductDTO[] = []
    allowedRole: string[] = [this.cookieTool.getRoleType()]

    @ViewChild("clientsTable") clientsTable: MatTable<any> | undefined
    @ViewChild("cardProductsTable") cardProductsTable: CardProductsTableComponent | undefined

    constructor(
        @Inject(LOCALE_ID) public locale: string,
        private dashboardBankerObserverService: DashboardBankerObserverService,
        private utilService: UtilService,
        private handleLoadRequestService: HandleLoadRequestService,
        private router: Router,
        private cookieTool: CookieToolService) {
    }

    ngOnInit(): void {
        this.utilService.openWaitingPopupDialog()
        if (this.utilService.resetSessionIfNoToken()) {
            return
        }
        const user: UserInformationDTO = this.cookieTool.getUserInformation()
        if (!this.allowedRole.includes(user.role.roleType)) {
            return this.utilService.handleDataIssue()
        }
        let tempBankerDashboard: BankerDashboardDTO | undefined = this.cookieTool.getBankerDashboardInformation()
        if (tempBankerDashboard === undefined) {
            let observable: Observable<BankerDashboardDTO> = this.handleLoadRequestService.loadRequest<BankerDashboardDTO>(
                this.dashboardBankerObserverService.getDashboard());
            observable.subscribe((data: BankerDashboardDTO ): void => {
                this.assignListOfDTO(data)
                this.refreshTables()
                this.utilService.closeDialog()
            })
        } else {
            this.assignListOfDTO(tempBankerDashboard)
        }
        this.refreshTables()
        this.utilService.closeDialog()
    }

    private assignListOfDTO(data: BankerDashboardDTO): void {
        this.bankerDashboard = data
        this.clientDashboards = Object.assign([], this.bankerDashboard.clients)
        this.cardProducts = Object.assign([], this.bankerDashboard.cardProducts)
        this.cardProductsTable?.assignListOfDTO(this.cardProducts)
    }

    private refreshTables() {
        this.utilService.refreshTable(this.clientsTable)
        this.cardProductsTable?.refreshTable()
    }

    protected navigateToDetailedClientTable(idClient: any): void {
        this.router.navigate(["/" + this.cookieTool.getRole() + "/" + Path.DETAILED_ACCOUNT, idClient]).then();
    }
}
