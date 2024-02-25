import { Injectable } from '@angular/core';
import { CookieService } from "ngx-cookie-service";
import {UserInformationDTO} from "../DTO/user_information/UserInformation.dto";
import {BankAccountDTO} from "../DTO/bank_account/BankAccount.dto";
import { jwtDecode } from "jwt-decode";
import {BankerDashboardDTO} from "../DTO/banker/BankerDashboard.dto";
import {CardProductDTO} from "../DTO/card_product/CardProduct.dto";

@Injectable({
  providedIn: 'root'
})
export class CookieToolService {

    private readonly TOKEN_KEY = 'authToken';
    private readonly USER_KEY = 'userInformation';
    private readonly BANK_ACCOUNTS_KEY = "bankAccounts"
    private readonly BANKER_DASHBOARD_KEY = "bankerDashboard"
    private readonly CARD_PRODUCT_KEY = "cardProduct"
    private readonly CARD_PRODUCTS_KEY = "cardProducts"


  constructor(private cookieService: CookieService) { }

    removeAll(): void {
        try {
            this.cookieService.deleteAll()
            sessionStorage.clear()
        } catch (err) {
            console.error("no data stored within cookie or session storage")
        }
    }

    isTokenSet(): boolean {
        return this.cookieService.check(this.TOKEN_KEY)
    }
    setToken(token: string):void {
        this.cookieService.set(this.TOKEN_KEY, token, new Date(jwtDecode(token).exp! * 1000))
    }

    getToken(): string {
        try {
            return this.cookieService.get(this.TOKEN_KEY)
        } catch (err) {
            console.error("non token")
            return ""
        }
    }

    getRoleType(): string {
        try {
            const user: UserInformationDTO = <UserInformationDTO>JSON.parse(this.cookieService.get(this.USER_KEY))
            return user.role.roleType
        } catch (err) {
            return ''
        }
    }

    getRole(): string {
        return this.getRoleType().slice(5).toLowerCase() // Remove "ROLE_"
    }

    removeUserInformation(): void {
        sessionStorage.removeItem(this.USER_KEY)
    }

    setUserInformation(user: UserInformationDTO):void {
        this.cookieService.set(this.USER_KEY, JSON.stringify(user), this.getExpirationDate())
    }

    getUserInformation(): UserInformationDTO {
        try {
            return <UserInformationDTO>JSON.parse(this.cookieService.get(this.USER_KEY))
        } catch (err) {
            console.error("no user set")
            return {} as UserInformationDTO
        }
    }

    removeBankAccountsInformation(): void {
        sessionStorage.removeItem(this.BANK_ACCOUNTS_KEY)
    }

    setBankAccountsInformation(accounts: BankAccountDTO[]): void {
        let sessionObjc: Object = {
            value: accounts,
            expirationDate: this.getExpirationDate() ? this.getExpirationDate()?.toISOString() : undefined
        }
        sessionStorage.setItem(this.BANK_ACCOUNTS_KEY, JSON.stringify(sessionObjc)   )
    }

    getBankAccountsInformation(): BankAccountDTO[] | undefined {
        const temp =sessionStorage.getItem(this.BANK_ACCOUNTS_KEY)
        if ( temp == null) {
            return undefined
        }
        const accountInformation = JSON.parse(temp)
        if (accountInformation.expirationDate === undefined) {
            return undefined
        }
        const currentDate: string = new Date().toISOString()
        if (Date.parse(accountInformation.expirationDate) < Date.parse(currentDate)) {
            this.removeBankAccountsInformation()
            return undefined
        }
        return <BankAccountDTO[]>(JSON.parse(temp).value)
    }

    removeCardProductsInformation(): void {
        sessionStorage.removeItem(this.CARD_PRODUCTS_KEY)
    }

    setCardProductsInformation(cardProducts: CardProductDTO[]): void {
        let sessionObjc: Object = {
            value: cardProducts,
            expirationDate: this.getExpirationDate() ? this.getExpirationDate()?.toISOString() : undefined
        }
        sessionStorage.setItem(this.CARD_PRODUCTS_KEY, JSON.stringify(sessionObjc)   )
    }

    getCardProductsInformation(): CardProductDTO[] | undefined {
        const temp = sessionStorage.getItem(this.CARD_PRODUCTS_KEY)
        if ( temp == null) {
            return undefined
        }
        const cardProductsInformation = JSON.parse(temp)
        if (cardProductsInformation.expirationDate === undefined) {
            return undefined
        }
        const currentDate: string = new Date().toISOString()
        if (Date.parse(cardProductsInformation.expirationDate) < Date.parse(currentDate)) {
            this.removeCardProductsInformation()
            return undefined
        }
        return <CardProductDTO[]>(JSON.parse(temp).value)
    }

    getExpirationDate(): Date | undefined {
        if (!this.cookieService.check(this.TOKEN_KEY)) {
            return undefined
        }
        return new Date(jwtDecode(this.getToken()).exp! * 1000)
    }

    removeBankerDashboardInformation(): void {
        sessionStorage.removeItem(this.BANKER_DASHBOARD_KEY)
    }

    setBankerDashboardInformation(bankerDashboard: BankerDashboardDTO): void {
        let sessionObjc: Object = {
            value: bankerDashboard,
            expirationDate: this.getExpirationDate() ? this.getExpirationDate()?.toISOString() : undefined
        }
        sessionStorage.setItem(this.BANKER_DASHBOARD_KEY, JSON.stringify(sessionObjc)   )
    }

    getBankerDashboardInformation(): BankerDashboardDTO | undefined {
        const temp = sessionStorage.getItem(this.BANKER_DASHBOARD_KEY)
        if ( temp == null) {
            return undefined
        }
        const bankerDashboard = JSON.parse(temp)
        const currentDate: string = new Date().toISOString()
        if (Date.parse(bankerDashboard.expirationDate) < Date.parse(currentDate)) {
            this.removeBankerDashboardInformation()
            return undefined
        }
        return <BankerDashboardDTO>(JSON.parse(temp).value)
    }

    removeCardProduct(): void {
        sessionStorage.removeItem(this.CARD_PRODUCT_KEY)
    }

    setCardProduct(cardProduct: CardProductDTO): void {
        let sessionObjc: Object = {
            value: cardProduct,
            expirationDate: this.getExpirationDate() ? this.getExpirationDate()?.toISOString() : undefined
        }
        sessionStorage.setItem(this.CARD_PRODUCT_KEY, JSON.stringify(sessionObjc)   )
    }

    getCardProductSelected(): CardProductDTO | undefined {
        const temp = sessionStorage.getItem(this.CARD_PRODUCT_KEY)
        if ( temp == null) {
            return undefined
        }
        const cardProduct = JSON.parse(temp)
        const currentDate: string = new Date().toISOString()
        if (Date.parse(cardProduct.expirationDate) < Date.parse(currentDate)) {
            this.removeBankerDashboardInformation()
            return undefined
        }
        return <CardProductDTO>(JSON.parse(temp).value)
    }
}
