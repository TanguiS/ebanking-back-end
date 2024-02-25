import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatTable, MatTableDataSource, MatTableModule} from "@angular/material/table";
import {BankAccountDTO} from "../../../DTO/bank_account/BankAccount.dto";
import {CookieToolService} from "../../../tool/cookie-tool.service";
import {UtilService} from "../../../tool/util.service";
import {Path} from "../../../tool/path";
import {ActivatedRoute, Router} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatIconModule} from "@angular/material/icon";
import {MatInputModule} from "@angular/material/input";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {CurrencyPipe, DatePipe, NgIf} from "@angular/common";
import {BeneficiaryToBankAccountDTO} from "../../../DTO/beneficiary/BeneficiaryToBankAccount.dto";
import {DirectDebitToBankAccountDTO} from "../../../DTO/direct_debit/DirectDebitToBankAccount.dto";
import {PaymentMethodDTO} from "../../../DTO/payment_method/PaymentMethod.dto";
import {BlockCardUserComponent} from "../../block-card-user/block-card-user.component";
import {Observable} from "rxjs";
import {HandleLoadRequestService} from "../../../tool/handle-load-request.service";
import {BankAccountsTableObserverService} from "../../../service/bank-accounts-table/bank-accounts-table-observer.service";
import {PaymentMethodRequestDTO} from "../../../DTO/request_payment_method/PaymentMethodRequest.dto";
import {MatSortModule} from "@angular/material/sort";
import {BeneficiaryPopupButtonComponent} from "../../beneficiary/beneficiary-popup-button/beneficiary-popup-button.component";
import {TransactionToBankAccountDTO} from "../../../DTO/transaction/TransactionToBankAccount.dto";
import {MatPaginator, MatPaginatorModule} from "@angular/material/paginator";
import {AskChequeBookComponent} from "../../ask-cheque-book/ask-cheque-book.component";
import {Role} from "../../../tool/role";
import {BankerDashboardDTO} from "../../../DTO/banker/BankerDashboard.dto";
import {DashboardBankerObserverService} from "../../../service/dashboard-banker/dashboard-banker-observer.service";
import {AddCardFormComponent} from "../../add-card/add-card-form/add-card-form.component";
import {AddCardPopupButtonComponent} from "../../add-card/add-card-popup-button/add-card-popup-button.component";

@Component({
  selector: 'app-detailed-bank_account-table',
  standalone: true,
    imports: [
        MatTableModule,
        MatButtonModule,
        MatExpansionModule,
        MatIconModule,
        MatInputModule,
        MatDatepickerModule,
        NgIf,
        CurrencyPipe,
        BlockCardUserComponent,
        DatePipe,
        MatSortModule,
        BlockCardUserComponent,
        BeneficiaryPopupButtonComponent,
        MatPaginatorModule,
        AskChequeBookComponent,
        AddCardFormComponent,
        AddCardPopupButtonComponent
    ],
  templateUrl: './detailed-account-table.component.html',
  styleUrl: './detailed-account-table.component.css'
})
export class DetailedAccountTableComponent implements OnInit, AfterViewInit, OnDestroy {
    protected accountInformation: BankAccountDTO | undefined
    protected beneficiariesList: BeneficiaryToBankAccountDTO[] = []
    protected debitMandateList: DirectDebitToBankAccountDTO[] = []
    protected paymentMethodList: PaymentMethodDTO[] = []
    protected paymentMethodRequestList: PaymentMethodRequestDTO[] = []
    protected transactionList: TransactionToBankAccountDTO[] = []
    protected dataSource!: MatTableDataSource<TransactionToBankAccountDTO>
    protected amountFormat: string = '1.2-2'
    protected accountId!: number
    protected idClient: number | undefined
    protected isChequeBookRequested: boolean = false
    protected accountColumns: string[] = [
        'Account Type1',
        'Amount1',
        'IBAN1',
        'RIB1'
    ]

    @ViewChild("GeneralAccountInformationTable") generalAccountInformationTable : MatTable<any> | undefined;
    @ViewChild("CardInformationTable") cardInformationTable : MatTable<any> | undefined;
    @ViewChild("BeneficiaryInformationTable") beneficiaryInformationTable : MatTable<any> | undefined;
    @ViewChild(MatPaginator) paginator: MatPaginator | undefined;

    constructor(
                private cookieTool: CookieToolService,
                private utilService: UtilService,
                private router: Router,
                private route: ActivatedRoute,
                private handleLoadRequestService: HandleLoadRequestService,
                private accountTableObserverService: BankAccountsTableObserverService,
                private dashboardBankerObserverService: DashboardBankerObserverService) {}

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            this.accountId = Number(params['idAccount'])
            this.idClient = Number(params['idClient'])
        })
        !this.idClient ? this.accountColumns.push("commandChequeBook") : null
        const accountInformationList: BankAccountDTO[] | undefined = this.cookieTool.getBankAccountsInformation()
        this.accountInformation = accountInformationList?.find(
            account => account.idBankAccount == this.accountId
        )
        if (this.accountInformation === undefined) {
            this.utilService.handleDataIssue()
            this.router.navigate(["/" + Path.DASHBOARD]).then()
        }
        this.initTable()
        this.setCardProductsIntoSessionStorage()
    }

    ngAfterViewInit() {
        if (this.paginator) {
            this.dataSource.paginator = this.paginator;
        }
    }

    ngOnDestroy(): void {
        if (this.cookieTool.getRole() == Role.BANKER) {
            this.cookieTool.removeBankAccountsInformation()
        }
    }

    private initTable(): void {
        this.beneficiariesList = this.accountInformation?.beneficiaries ? this.accountInformation.beneficiaries : []
        this.debitMandateList = this.accountInformation?.directDebits ? this.accountInformation.directDebits : []
        this.paymentMethodList = this.accountInformation?.paymentMethods ? this.accountInformation.paymentMethods : []
        this.paymentMethodRequestList = this.accountInformation?.paymentMethodRequests ? this.accountInformation.paymentMethodRequests : []
        this.transactionList = this.accountInformation?.transactions ? this.accountInformation.transactions : []
        this.sortTable()
        this.searchChequeBookRequest()
        this.dataSource = new MatTableDataSource<TransactionToBankAccountDTO>(this.transactionList);
        this.generalAccountInformationTable?.renderRows()
        this.cardInformationTable?.renderRows()
        this.beneficiaryInformationTable?.renderRows()
    }

    protected refreshTable(canTableBeRefreshed: boolean): void {
        if (!canTableBeRefreshed) {
            return
        }
        this.updateDTO()
    }

    protected canCardBeBlocked(cardStatus: string): boolean {
        return cardStatus == "ACTIVATED";
    }

    private sortTable() {
        this.paymentMethodRequestList.sort((a, b) => {
            if (a.requestCard === undefined && b.requestChequeBook !== undefined) {
                return 1
            } else if (a.requestCard !== undefined && b.requestChequeBook === undefined) {
                return -1
            } else {
                return 0
            }
        })
        this.transactionList.sort((a, b) => {
            if (a.transaction.transactionDate >= b.transaction.transactionDate) {
                return 1
            }
            return -1
        })
    }

    private searchChequeBookRequest(): void {
        for (let request of this.paymentMethodRequestList ) {
            if (request.requestChequeBook) {
                if (request.requestChequeBook.requestPaymentMethodStatus.orderStatus.toUpperCase() == "NOT_ORDERED") {
                    this.isChequeBookRequested = true
                    return
                }
            }
        }
    }

    isUserABanker(): boolean {
        return this.cookieTool.getRole() == Role.BANKER
    }

    updateDTO() {
        if (this.isUserABanker()) {
            let observable: Observable<BankerDashboardDTO> = this.handleLoadRequestService.loadRequest<BankerDashboardDTO>(
                this.dashboardBankerObserverService.getDashboard());
            observable.subscribe((data: BankerDashboardDTO ): void => {
                this.utilService.setClientInformationSelectedIntoSessionStorage(data.clients, this.idClient!)
                const accountInformationList: BankAccountDTO[] | undefined = this.cookieTool.getBankAccountsInformation()
                this.accountInformation = accountInformationList?.find(
                    (account: BankAccountDTO): boolean => account.idBankAccount == this.accountId
                )
                this.initTable()
            })
        } else {
            let observable: Observable<BankAccountDTO[]> = this.handleLoadRequestService.loadRequest<BankAccountDTO[]>(
                this.accountTableObserverService.getAccounts());
            observable.subscribe((): void => {
                const accountInformationList: BankAccountDTO[] | undefined = this.cookieTool.getBankAccountsInformation()
                this.accountInformation = accountInformationList?.find(
                    (account: BankAccountDTO): boolean => account.idBankAccount == this.accountId
                )
                this.initTable()
            })
        }
    }

    private setCardProductsIntoSessionStorage() {
        const bankerDashboard = this.cookieTool.getBankerDashboardInformation()
        this.cookieTool.setCardProductsInformation(bankerDashboard?.cardProducts!)
    }

    protected readonly console = console;
}
