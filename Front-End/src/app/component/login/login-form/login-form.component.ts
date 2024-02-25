import {AfterContentChecked, Component, HostListener} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpErrorResponse} from "@angular/common/http";
import {catchError} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {HeaderService} from "../../../service/header/header.service";
import {LoginFormObserverService} from "../../../service/login-form/login-form-observer.service";
import {UserInformationDTO} from "../../../DTO/user_information/UserInformation.dto";
import {UtilService} from "../../../tool/util.service";
import {ResponseDTO} from "../../../DTO/response/Response.dto";


@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css',
  '../../../resources/form.css']
})
export class LoginFormComponent implements AfterContentChecked {
    public title: string = "Login"
    public userForm!: FormGroup
    private _userLogin: UserInformationDTO = {} as UserInformationDTO
    private _regexPassword: RegExp = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).*$/
    private _globalErrorMessage: string = "An error has occurred: please verify your information."
    private _globalSuccessMessage: string = "You are connected."
    public globalMessage: string = this._globalSuccessMessage
    private _passwordMissingMessage: string = "Password is missing"
    private _isPasswordValid: string = "Password is incorrect"
    private _isRequestCompliant: boolean = true
    public passwordMessage: string = this._passwordMissingMessage
    public passwordFormatDescription: string = "At least 8 characters whose a special, an uppercase letter, " +
        "a lowercase letter, and a number"
    private _responseMessage: string = "Server response"
    private _tokenExpirationDate: Date = new Date()

    constructor(private formBuilder: FormBuilder,
                public dialog: MatDialog,
                private headerService: HeaderService,
                private utilService: UtilService,
                private loginFormObserverService: LoginFormObserverService) {
        this.userForm = this.formBuilder.group({
            _email: '',
            _password: ['', [Validators.required, Validators.pattern(this._regexPassword)]]
        });
        this._tokenExpirationDate.setMinutes(this._tokenExpirationDate.getMinutes() + 5);
    }

    ngAfterContentChecked(): void {
        this.updateFormWidth()
    }

    @HostListener('window:resize', ['$event'])
    onResize(event: any): void {
        this.updateFormWidth()
    }

    onSubmit():void {
        this.displayErrorMessages()

        if ( !this._isRequestCompliant ) {
            this.setGlobalErrorMessage(this._globalErrorMessage)
            this.displayGlobalMessage()
            this._isRequestCompliant = true
            return
        } else {
            this.hideGlobalMessage()
        }

        this.getUserLogin()
        this.logInUser()

        if ( !this._isRequestCompliant ) {
            this.displayGlobalMessage()
            this._isRequestCompliant = true
            return
        }

    }

    isEmailMissing():boolean {
        return this.userForm.get("_email")!.value.toString() === ""
    }

    isPasswordMissing():boolean {
        this.passwordMessage = this._passwordMissingMessage
        return this.userForm.get("_password")!.value.toString() === ""
    }

    setGlobalSuccessMessage():void {
        this.globalMessage = this._globalSuccessMessage
        document.getElementById("global-message")!.classList.remove("global-error")
        document.getElementById("global-message")!.classList.add("global-success")
    }

    setGlobalErrorMessage(globalErrorMessage:string):void {
        this.globalMessage = globalErrorMessage
        document.getElementById("global-message")!.classList.remove("global-success")
        document.getElementById("global-message")!.classList.add("global-error")
    }

    displayGlobalMessage():void {
        this.hideGlobalMessage()
        document.getElementById("global-message")!.classList.remove("hidden-message")
        document.getElementById("global-message")!.classList.add("displayed-message")
    }

    hideGlobalMessage():void {
        document.getElementById("global-message")!.classList.remove("displayed-message")
        document.getElementById("global-message")!.classList.add("hidden-message")
    }

    displayErrorEmailMessage():void {
        document.getElementById("email-missing-container")!.classList.remove("hidden-message")
        document.getElementById("email-missing-container")!.classList.add("displayed-message")
        document.getElementById("email-container")!.classList.remove("container-ok")
        document.getElementById("email-container")!.classList.add("container-error")
    }

    hideErrorEmailMessage():void {
        document.getElementById("email-missing-container")!.classList.remove("displayed-message")
        document.getElementById("email-missing-container")!.classList.add("hidden-message")
        document.getElementById("email-container")!.classList.remove("container-error")
        document.getElementById("email-container")!.classList.add("container-ok")
    }

    displayErrorPasswordMessage():void {
        document.getElementById("password-error-container")!.classList.remove("hidden-message")
        document.getElementById("password-error-container")!.classList.add("displayed-message")
        document.getElementById("password-container")!.classList.remove("container-ok")
        document.getElementById("password-container")!.classList.add("container-error")
    }

    hideErrorPasswordMessage():void {
        document.getElementById("password-error-container")!.classList.remove("displayed-message")
        document.getElementById("password-error-container")!.classList.add("hidden-message")
        document.getElementById("password-container")!.classList.remove("container-error")
        document.getElementById("password-container")!.classList.add("container-ok")
    }

    displayErrorMessages():void {
        if ( this.isEmailMissing() ) {
            this._isRequestCompliant = false
            this.displayErrorEmailMessage()
        } else {
            this.hideErrorEmailMessage()
        }

        if ( this.isPasswordMissing() ) {
            this._isRequestCompliant = false
            this.displayErrorPasswordMessage()
        } else {
            if ( !this.isPasswordValid() ) {
                this._isRequestCompliant = false
                this.displayErrorPasswordMessage()
            } else {
                this.hideErrorPasswordMessage()
            }
        }
    }

    isPasswordValid():boolean {
        const password: FormControl<string> = this.userForm.get("_password")! as FormControl<string>
        if (password.valid && password.value.toString().length >= 8) {
            return true
        }
        this.passwordMessage = this._isPasswordValid
        return false
    }

    disableSubmitButton():void {
        const submitButton: HTMLButtonElement = document.getElementById("submit-button")! as HTMLButtonElement
        submitButton.disabled = true
        submitButton.classList.add("disabled-button")
    }

    getUserLogin():void {
        this._userLogin.email = this.userForm.get("_email")!.value
        this._userLogin.password = this.userForm.get("_password")!.value
    }

    logInUser(): void {
        this.utilService.openWaitingPopupDialog()
        this.loginFormObserverService.logInUser(this._userLogin)
            .pipe(
                catchError(data => {
                    this.utilService.closeDialog()
                    this.displayErrorResponse(data)
                    return data;
                })
            )
            .subscribe(
            (): void => {
                this.utilService.closeDialog()
                this.displayLoggedInResponse()
            })
    }

    displayLoggedInResponse() {
        this.setGlobalSuccessMessage()
        this.displayGlobalMessage()
        this.disableSubmitButton()
        this._responseMessage = "You have successfully logged in!"
        this.utilService.openLoginPopupDialog(this._responseMessage)
    }

    displayErrorResponse(error: HttpErrorResponse):void  {
        this.setGlobalErrorMessage(error.error.interactionResponse.data)
        this.displayGlobalMessage()
        this._responseMessage = error.error.interactionResponse.data
        this.utilService.openPopupDialog("Login", this._responseMessage)
    }

    updateFormWidth() {
        if (this.headerService.isSmallScreen()) {
            document.getElementById("form-element")!.classList.add("small-screen-form")
        } else {
            document.getElementById("form-element")!.classList.remove("small-screen-form")
        }
    }
}

