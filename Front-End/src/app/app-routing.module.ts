import {LOCALE_ID, NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes} from "@angular/router";
import { RegistrationFormComponent } from "./component/registration-form/registration-form.component";
import { LoginFormComponent } from "./component/login/login-form/login-form.component";
import {BankAccountsTableComponent} from "./component/bank-accounts-table/bank-accounts-table.component";
import { HomeComponent } from "./component/home/home.component";
import {SimulatorComponent} from "./component/simulator/simulator/simulator.component";
import {DetailedAccountTableComponent} from "./component/detailed-table/detailed-account-table/detailed-account-table.component";
import {Role} from "./tool/role";
import {Path} from "./tool/path";
import {DashboardBankerComponent} from "./component/dashboard-banker/dashboard-banker.component";
import {DetailedClientTableComponent} from "./component/detailed-table/detailed-client-table/detailed-client-table.component";
import {DetailedCardProductTableComponent} from "./component/detailed-table/detailed-card-product-table/detailed-card-product-table.component";



const routes: Routes = [
    {path: '', redirectTo: "/home", pathMatch: 'full'},
    {path: 'home', component: HomeComponent},
    {path: 'register', component: RegistrationFormComponent},
    {path: 'login', component: LoginFormComponent},
    {path: Role.CLIENT + "/" + Path.DASHBOARD, component: BankAccountsTableComponent},
    {path: Role.BANKER + "/" + Path.DASHBOARD, component: DashboardBankerComponent},
    {path: 'simulator', component: SimulatorComponent},
    {path: Role.CLIENT + "/" + Path.DETAILED_ACCOUNT + "/:idAccount", component: DetailedAccountTableComponent},
    {path: Role.BANKER + "/" + Path.DETAILED_ACCOUNT + "/:idAccount", component: DetailedClientTableComponent},
    {path: Role.BANKER + "/" + Path.DETAILED_ACCOUNT + "/:idClient" + "/:idAccount", component: DetailedAccountTableComponent},
    {path: Role.BANKER + "/" + Path.CARD_PRODUCT + "/:idCardProduct", component: DetailedCardProductTableComponent}
]

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        RouterModule.forRoot(routes)
    ],
    exports: [RouterModule],
    providers: [{
        provide: LOCALE_ID,
        useValue: 'fr-FR' // 'de-DE' for Germany, 'fr-FR' for France ...
    },
    ]
})

export class AppRoutingModule { }
