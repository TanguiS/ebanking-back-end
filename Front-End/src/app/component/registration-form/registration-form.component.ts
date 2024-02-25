import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {catchError, throwError} from "rxjs";
import {RegistrationFormObserverService} from "../../service/registration-form/registration-form-observer.service";
import {UserInformationDTO} from "../../DTO/user_information/UserInformation.dto";
import {ResponseDTO} from "../../DTO/response/Response.dto";
import {UtilService} from "../../tool/util.service";

@Component({
    selector: 'app-registration-form',
    templateUrl: './registration-form.component.html',
    styleUrls: ['./registration-form.component.css',
    '../../resources/form.css']
})


export class RegistrationFormComponent {
    public title:string = "Registration"
    public userForm!: FormGroup
    private _userData: UserInformationDTO = {} as UserInformationDTO
    private _regexPassword:RegExp = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).*$/
    private _regexPhoneNumber: RegExp = /^(?:\+33[2-9]|0[2-9])\d{8}$/
    private _globalErrorMessage:string = "An error has occurred: please verify your information."
    private _globalSuccessMessage:string = "Your request has been taken into bank_account."
    public globalMessage:string = this._globalSuccessMessage
    private _phoneNumberMissingMessage:string = "Phone number is missing"
    private _isPhoneNumberValid:string = "Phone number is incorrect"
    private _passwordMissingMessage:string = "Password is missing"
    private _isPasswordValid:string = "Password is incorrect"
    private _isRequestCompliant:boolean = true
    public phoneNumberMessage:string = this._phoneNumberMissingMessage
    public passwordMessage:string = this._passwordMissingMessage
    public passwordFormatDescription:string = "At least 8 characters whose a special, an uppercase letter, a lowercase letter, and a number"

    constructor(private formBuilder: FormBuilder,
                private utilService: UtilService,
                private registrationFormObserverService: RegistrationFormObserverService) {
        this.userForm = this.formBuilder.group({
            _firstName: '',
            _lastName: '',
            _gender: -1,
            _phoneNumber: '',
            _email: '',
            _emailAgain: '',
            _password: ['', [Validators.required, Validators.pattern(this._regexPassword)]],
            _passwordAgain: ['', [Validators.required, Validators.pattern(this._regexPassword)]]
        });
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

        this.getUserData()
        this.registerUser()

        if ( !this._isRequestCompliant ) {
            this.displayGlobalMessage()
            this._isRequestCompliant = true
            return
        }
    }

    checkEmailAddress():boolean {
        return this.userForm.get("_email")!.value.toString() === this.userForm.get("_emailAgain")!.value.toString()
    }

    checkPassword():boolean {
        return this.userForm.get("_password")!.value.toString() === this.userForm.get("_passwordAgain")!.value.toString()
    }

    isFirstNameMissing():boolean {
        return this.userForm.get("_firstName")!.value.toString() === ""
    }

    isLastNameMissing():boolean {
        return this.userForm.get("_lastName")!.value.toString() === ""
    }

    isGenderMissing():boolean {
        return this.userForm.get("_gender")!.value.toString() === "-1"
    }

    isPhoneNumberMissing():boolean {
        this.phoneNumberMessage = this._phoneNumberMissingMessage
        return this.userForm.get("_phoneNumber")!.value.toString() === ""
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

    displayFirstNameErrorMessage():void {
        document.getElementById("first-name-error-container")!.classList.remove("hidden-message")
        document.getElementById("first-name-error-container")!.classList.add("displayed-message")
        document.getElementById("first-name-container")!.classList.remove("container-ok")
        document.getElementById("first-name-container")!.classList.add("container-error")
    }

    hideFirstNameErrorMessage():void {
        document.getElementById("first-name-error-container")!.classList.remove("displayed-message")
        document.getElementById("first-name-error-container")!.classList.add("hidden-message")
        document.getElementById("first-name-container")!.classList.remove("container-error")
        document.getElementById("first-name-container")!.classList.add("container-ok")
    }

    displayLastNameErrorMessage():void {
        document.getElementById("last-name-error-container")!.classList.remove("hidden-message")
        document.getElementById("last-name-error-container")!.classList.add("displayed-message")
        document.getElementById("last-name-container")!.classList.remove("container-ok")
        document.getElementById("last-name-container")!.classList.add("container-error")
    }

    hideLastNameErrorMessage():void {
        document.getElementById("last-name-error-container")!.classList.remove("displayed-message")
        document.getElementById("last-name-error-container")!.classList.add("hidden-message")
        document.getElementById("last-name-container")!.classList.remove("container-error")
        document.getElementById("last-name-container")!.classList.add("container-ok")
    }

    displayGenderErrorMessage():void {
        document.getElementById("gender-error-container")!.classList.remove("hidden-message")
        document.getElementById("gender-error-container")!.classList.add("displayed-message")
        document.getElementById("gender-container")!.classList.remove("container-ok")
        document.getElementById("gender-container")!.classList.add("container-error")
    }

    hideGenderErrorMessage():void {
        document.getElementById("gender-error-container")!.classList.remove("displayed-message")
        document.getElementById("gender-error-container")!.classList.add("hidden-message")
        document.getElementById("gender-container")!.classList.remove("container-error")
        document.getElementById("gender-container")!.classList.add("container-ok")
    }

    displayErrorPhoneNumberMessage():void {
        document.getElementById("phoneNumber-missing-container")!.classList.remove("hidden-message")
        document.getElementById("phoneNumber-missing-container")!.classList.add("displayed-message")
        document.getElementById("phoneNumber-container")!.classList.remove("container-ok")
        document.getElementById("phoneNumber-container")!.classList.add("container-error")
    }

    hideErrorPhoneNumberMessage():void {
        document.getElementById("phoneNumber-missing-container")!.classList.remove("displayed-message")
        document.getElementById("phoneNumber-missing-container")!.classList.add("hidden-message")
        document.getElementById("phoneNumber-container")!.classList.remove("container-error")
        document.getElementById("phoneNumber-container")!.classList.add("container-ok")
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

    displayErrorEmailAgainMessage():void {
        document.getElementById("email-again-error-container")!.classList.remove("hidden-message")
        document.getElementById("email-again-error-container")!.classList.add("displayed-message")
        document.getElementById("email-again-container")!.classList.remove("container-ok")
        document.getElementById("email-again-container")!.classList.add("container-error")
    }

    hideErrorEmailAgainMessage():void {
        document.getElementById("email-again-error-container")!.classList.remove("displayed-message")
        document.getElementById("email-again-error-container")!.classList.add("hidden-message")
        document.getElementById("email-again-container")!.classList.remove("container-error")
        document.getElementById("email-again-container")!.classList.add("container-ok")
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

    displayErrorPasswordAgainMessage():void {
        document.getElementById("password-again-error-container")!.classList.remove("hidden-message")
        document.getElementById("password-again-error-container")!.classList.add("displayed-message")
        document.getElementById("password-again-container")!.classList.remove("container-ok")
        document.getElementById("password-again-container")!.classList.add("container-error")
    }

    hideErrorPasswordAgainMessage():void {
        document.getElementById("password-again-error-container")!.classList.remove("displayed-message")
        document.getElementById("password-again-error-container")!.classList.add("hidden-message")
        document.getElementById("password-again-container")!.classList.remove("container-error")
        document.getElementById("password-again-container")!.classList.add("container-ok")
    }

    displayErrorMessages():void {
        if ( this.isFirstNameMissing() ) {
            this._isRequestCompliant = false
            this.displayFirstNameErrorMessage();
        } else {
            this.hideFirstNameErrorMessage();
        }

        if ( this.isLastNameMissing() ) {
            this._isRequestCompliant = false
            this.displayLastNameErrorMessage();
        } else {
            this.hideLastNameErrorMessage();
        }

        if ( this.isGenderMissing() ) {
            this._isRequestCompliant = false
            this.displayGenderErrorMessage();
        } else {
            this.hideGenderErrorMessage();
        }

        if ( this.isPhoneNumberMissing() ) {
            this._isRequestCompliant = false
            this.displayErrorPhoneNumberMessage();
        } else {
            if ( !this.isPhoneNumberValid() ) {
                this._isRequestCompliant = false
                this.displayErrorPhoneNumberMessage()
            } else {
                this.hideErrorPhoneNumberMessage()
            }
        }

        if ( this.isEmailMissing() ) {
            this._isRequestCompliant = false
            this.displayErrorEmailMessage();
        } else {
            this.hideErrorEmailMessage();
        }

        if ( !this.checkEmailAddress() ) {
            this._isRequestCompliant = false
            this.displayErrorEmailAgainMessage();
        } else {
            this.hideErrorEmailAgainMessage();
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

        if ( !this.checkPassword() ) {
            this._isRequestCompliant = false
            this.displayErrorPasswordAgainMessage();
        } else {
            this.hideErrorPasswordAgainMessage();
        }
    }

    isPhoneNumberValid():boolean {
        const phoneNumber: FormControl<string> = this.userForm.get("_phoneNumber")! as FormControl<string>
        this.userForm.get("_phoneNumber")?.setValue(phoneNumber.value.replace(/\s/g, ""))
        if (this._regexPhoneNumber.test(phoneNumber.value)) {
            return true
        }
        this.phoneNumberMessage = this._isPhoneNumberValid
        return false
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

    getUserData():void {
        this._userData.firstName = this.userForm.get("_firstName")!.value
        this._userData.lastName = this.userForm.get("_lastName")!.value
        this._userData.gender = parseInt(this.userForm.get("_gender")!.value)
        this._userData.phoneNumber = this.userForm.get("_phoneNumber")!.value.toString().replace(/\s/g, "")
        this._userData.email = this.userForm.get("_email")!.value
        this._userData.password = this.userForm.get("_password")!.value
    }

    registerUser(): void {
        this.utilService.openWaitingPopupDialog()
        this.registrationFormObserverService.registerUser(this._userData)
            .pipe(
                catchError(data => {
                    this.utilService.closeDialog()
                    this.setGlobalErrorMessage(data.error.interactionResponse.data)
                    this.displayGlobalMessage()
                    this.utilService.openPopupDialog( "Registration", data.error.interactionResponse.data)
                    return throwError(() => Error(data.error))
                })
            )
            .subscribe( (data: ResponseDTO) => {
                this.utilService.closeDialog()
                this.setGlobalSuccessMessage()
                this.displayGlobalMessage()
                this.disableSubmitButton()
                this.utilService.openPopupDialog( "Registration", data.message)
            })
    }
}
