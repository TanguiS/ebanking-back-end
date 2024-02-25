import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import localeFr from '@angular/common/locales/fr';
registerLocaleData(localeFr);
import { AppComponent } from './app.component';
import { HeaderComponent } from './component/navigation/header/header.component';
import { MatTabsModule } from '@angular/material/tabs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {NgOptimizedImage, registerLocaleData} from "@angular/common";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { HttpClientModule } from "@angular/common/http";
import { RouterOutlet } from "@angular/router";
import { AppRoutingModule } from './app-routing.module';
import { RegistrationFormComponent } from './component/registration-form/registration-form.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatTooltipModule } from '@angular/material/tooltip';
import { LoginFormComponent } from './component/login/login-form/login-form.component';
import { LoginPopupComponent } from './component/login/login-popup/login-popup.component';
import { MatDialogModule } from "@angular/material/dialog";
import { FlexLayoutModule } from '@angular/flex-layout';
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {CookieService} from "ngx-cookie-service";
import {BlockCardUserComponent} from "./component/block-card-user/block-card-user.component";
import {MatNativeDateModule} from "@angular/material/core";
import {NavigationButtonsListComponent} from "./component/navigation/navigation-buttons-list/navigation-buttons-list.component";
import {PageTitleComponent} from "./component/page-title/page-title.component";

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        RegistrationFormComponent,
        LoginFormComponent,
        LoginPopupComponent
    ],
    imports: [
        BrowserModule,
        MatTabsModule,
        BrowserAnimationsModule,
        NgOptimizedImage,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        HttpClientModule,
        RouterOutlet,
        AppRoutingModule,
        FormsModule,
        ReactiveFormsModule,
        MatTooltipModule,
        BrowserAnimationsModule,
        MatDialogModule,
        FlexLayoutModule,
        MatButtonToggleModule,
        BlockCardUserComponent,
        MatNativeDateModule,
        NavigationButtonsListComponent,
        PageTitleComponent

    ],
    providers: [CookieService],
    bootstrap: [AppComponent],
    exports: [
        LoginFormComponent
    ]
})
export class AppModule { }
