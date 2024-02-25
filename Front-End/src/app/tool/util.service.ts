import {Injectable} from '@angular/core';
import {LoginPopupComponent} from "../component/login/login-popup/login-popup.component";
import {WaitingPopupComponent} from "../component/popup/waiting-popup/waiting-popup.component";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CookieToolService} from "./cookie-tool.service";
import {Router} from "@angular/router";
import {Path} from "./path";
import {BeneficiaryPopupComponent} from "../component/beneficiary/beneficiary-popup/beneficiary-popup.component";
import {MatTable} from "@angular/material/table";
import {ClientDashboardDTO} from "../DTO/client/ClientDashboard.dto";
import {CardProductDTO} from "../DTO/card_product/CardProduct.dto";
import {ClassicPopupComponent} from "../component/popup/classic-popup/classic-popup.component";
import {AddCardPopupComponent} from "../component/add-card/add-card-popup/add-card-popup.component";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UtilService {
    animationDuration: string = "300ms"

    constructor(private dialog: MatDialog,
                private cookieTookService: CookieToolService,
                private router: Router) { }

    openLoginPopupDialog(message: string): void {
        this.dialog.open(LoginPopupComponent, {
            enterAnimationDuration: this.animationDuration,
            exitAnimationDuration: this.animationDuration,
            data: { message: message },
            disableClose: true
        });
    }

    openPopupDialog(title: string, message: string): void {
        this.dialog.open(ClassicPopupComponent, {
            enterAnimationDuration: this.animationDuration,
            exitAnimationDuration: this.animationDuration,
            data: { title: title, message: message },
            disableClose: true
        });
    }

    openWaitingPopupDialog(): void {
        this.dialog.open(WaitingPopupComponent, {
            enterAnimationDuration: this.animationDuration,
            exitAnimationDuration: this.animationDuration,
            disableClose: true
        });
    }

    openBeneficiaryPopupDialog(idCurrentBankAccount: number): Observable<boolean> {
        let dialogRef: MatDialogRef<BeneficiaryPopupComponent, any> = this.dialog.open(BeneficiaryPopupComponent, {
            width: "500px",
            enterAnimationDuration: this.animationDuration,
            exitAnimationDuration: this.animationDuration,
            disableClose: false
        });
        dialogRef.componentInstance.idCurrentBankAccount = idCurrentBankAccount
        return dialogRef.afterClosed()
    }

    openAddCardPopupDialog(idClient: number, idCurrentBankAccount: number): Observable<boolean> {
        let dialogRef: MatDialogRef<AddCardPopupComponent, any> = this.dialog.open(AddCardPopupComponent, {
            width: "500px",
            enterAnimationDuration: this.animationDuration,
            exitAnimationDuration: this.animationDuration,
            disableClose: false
        });
        dialogRef.componentInstance.idClient = idClient
        dialogRef.componentInstance.idCurrentBankAccount = idCurrentBankAccount
        return dialogRef.afterClosed()
    }

    closeDialog() {
        this.dialog.closeAll()
    }

    resetSessionIfNoToken(): boolean {
        if (!this.cookieTookService.isTokenSet()) {
            this.router.navigate(["/"+Path.HOME]).then()
            return true
        }
        return false
    }

    handleDataIssue(): void {
        this.router.navigate(["/"+Path.HOME]).then()
    }

    public refreshTable(table: MatTable<any> | undefined): void {
        table?.renderRows()
    }

    public setClientInformationSelectedIntoSessionStorage(clientDashboards: ClientDashboardDTO[], idClient: number): void {
        const client: ClientDashboardDTO | undefined = clientDashboards.find((x: ClientDashboardDTO) => x.client.idUser == idClient)
        this.cookieTookService.setBankAccountsInformation(client!.bankAccounts)
    }

    public setCardProductSelectedIntoSessionStorage(cardProducts: CardProductDTO[], idCardProduct: number): void {
        const cardProduct: CardProductDTO | undefined = cardProducts.find((x: CardProductDTO) => {
            return x.idCardProduct == idCardProduct
        })
        this.cookieTookService.setCardProduct(cardProduct!)
    }
}
