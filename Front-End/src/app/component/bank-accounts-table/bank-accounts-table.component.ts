import {Component, Inject, Input, LOCALE_ID, OnInit, ViewChild} from '@angular/core';
import {MatTable, MatTableModule} from '@angular/material/table';
import {CurrencyPipe, NgIf} from "@angular/common";
import {BankAccountDTO} from "../../DTO/bank_account/BankAccount.dto";
import {BankAccountsTableObserverService} from "../../service/bank-accounts-table/bank-accounts-table-observer.service";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {UtilService} from "../../tool/util.service";
import {CookieToolService} from "../../tool/cookie-tool.service";
import {RequestToServerToolsService} from "../../tool/request-to-server-tools.service";
import {HandleLoadRequestService} from "../../tool/handle-load-request.service";
import {Observable} from "rxjs";
import {Role} from "../../tool/role";
import {PageTitleComponent} from "../page-title/page-title.component";
import {Router} from "@angular/router";
import {Path} from "../../tool/path";
import {MatRippleModule} from "@angular/material/core";

@Component({
    imports: [
        MatTableModule,
        CurrencyPipe,
        NgIf,
        MatProgressSpinnerModule,
        PageTitleComponent,
        MatRippleModule
    ],
    selector: 'app-bank-accounts-table',
    standalone: true,
    styleUrl: './bank-accounts-table.component.css',
    templateUrl: './bank-accounts-table.component.html'
})
export class BankAccountsTableComponent implements OnInit {
    @Input() isMainComponent: boolean = true

    displayedColumns: string[] = ['IBAN', 'accountType', 'amount']
    amountFormat: string = '1.2-2'
    totalAmount: number = 0
    url: string = RequestToServerToolsService.standardURL + "client/dashboard"
    accounts: BankAccountDTO[] = []
    allowedRole: string[] = [Role.CLIENT, Role.BANKER]
    protected title: string = 'Dashboard'

    @Input() idClient: number | undefined
    @ViewChild(MatTable) table : MatTable<any> | undefined

    constructor(
        @Inject(LOCALE_ID) public locale: string,
        private accountTableObserverService:BankAccountsTableObserverService,
        private utilService:UtilService,
        private cookieTool: CookieToolService,
        private handleLoadRequestService: HandleLoadRequestService,
        private router: Router) {
    }

    ngOnInit(): void {
        this.utilService.openWaitingPopupDialog()
        if (this.utilService.resetSessionIfNoToken()) {
            return
        }
        if (!this.allowedRole.includes(this.cookieTool.getRole())) {
            return this.utilService.handleDataIssue()
        }
        let tempAccounts: BankAccountDTO[] | undefined = this.cookieTool.getBankAccountsInformation()
        if (tempAccounts === undefined && this.cookieTool.getRole() === Role.CLIENT) {
            let observable: Observable<BankAccountDTO[]> = this.handleLoadRequestService.loadRequest<BankAccountDTO[]>(
                this.accountTableObserverService.getAccounts());
            observable.subscribe((data: BankAccountDTO[] ): void => {
                this.accounts = Object.assign([], data)
                this.utilService.refreshTable(this.table)
                this.utilService.closeDialog()
            })
        } else {
            this.accounts = Object.assign([], tempAccounts)
        }
        this.utilService.refreshTable(this.table)
        this.utilService.closeDialog()
    }


    protected getTotalAmount(): number {
        if (this.accounts.length == 0) {
            return 0
        }
        return this.totalAmount = this.accounts.map((element: BankAccountDTO) => element.amount)
            .reduce(function(a: number, b: number)
            {
                return a + b;
            })
    }

    protected navigateToDetailedBankAccount(bankAccountId: any): void {
        switch (this.cookieTool.getRole()) {
            case Role.CLIENT:
                this.router.navigate(["/" + this.cookieTool.getRole() + "/" + Path.DETAILED_ACCOUNT, bankAccountId]).then();
                break
            case Role.BANKER:
                this.router.navigate(["/" + this.cookieTool.getRole() + "/" + Path.DETAILED_ACCOUNT + "/" + this.idClient, bankAccountId]).then();
                break
        }
    }
}
