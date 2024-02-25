import {Component, ViewChild} from '@angular/core';
import {SimulatorTransactionDTO} from "../../../DTO/transaction/SimulatorTransaction.dto";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatTable, MatTableModule} from "@angular/material/table";
import {MatMenuModule} from "@angular/material/menu";
import {MatButtonModule} from "@angular/material/button";
import {FlexModule} from "@angular/flex-layout";
import {MatSelectModule} from "@angular/material/select";
import {FormBuilder, FormGroup, ReactiveFormsModule, ɵElement} from "@angular/forms";
import {SimulatorObserverService} from "../../../service/simulator/simulator-observer.service";
import {MatStepperModule} from "@angular/material/stepper";
import {catchError, throwError} from "rxjs";
import {UtilService} from "../../../tool/util.service";
import {ResponseDTO} from "../../../DTO/response/Response.dto";

@Component({
  selector: 'app-simulator',
  standalone: true,
    imports: [
        NgIf,
        MatProgressSpinnerModule,
        CurrencyPipe,
        MatTableModule,
        MatMenuModule,
        MatButtonModule,
        FlexModule,
        MatSelectModule,
        ReactiveFormsModule,
        NgForOf,
        MatStepperModule
    ],
  templateUrl: './simulator.component.html',
  styleUrl: './simulator.component.css'
})
export class SimulatorComponent {
    title: string = "Simulator"

    numberOfTransactions: {value: number, name: string}[] = [
        {value: 10, name: '10'},
        {value: 20, name: '20'},
        {value: 50, name: '50'},
        {value: 100, name: '100'}
    ];
    selectedNumberOfTransactions: number = this.numberOfTransactions[0].value;
    numberOfTransactionsForm: FormGroup<{
        [K in keyof { name: string; value: number }]: ɵElement<{
            name: string;
            value: number
        }[K], null>
    }> = this.formBuilder.group({
        value: 10,
        name: ''
    });

    simulatorTransactionsList: SimulatorTransactionDTO[] = []
    displayedColumns: string[] = [
        "accountingDirection",
        "amount",
        "currency",
        "transactionActor",
        "transactionDate",
        "transactionType"
    ]

    @ViewChild(MatTable) table: MatTable<any> | undefined;

    constructor(private formBuilder: FormBuilder,
                private utilService: UtilService,
                private simulatorObserverService: SimulatorObserverService) {
    }

    generateTransactionSimulation(): void {
        this.simulatorObserverService.generateTransactionSimulation(this.selectedNumberOfTransactions).subscribe(
            (data: SimulatorTransactionDTO[]): void => {
                this.simulatorTransactionsList = Object.assign([], data)
                this.refreshTable()
            }
        )
    }

    runTransactionSimulation(): void {
        this.utilService.openWaitingPopupDialog()
        this.simulatorObserverService.runTransactionSimulation(this.simulatorTransactionsList)
            .pipe(
                catchError(data => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Simulation result", data.error.interactionResponse.data)
                    return throwError(() => Error(data.error.interactionResponse.data))
                })
            )
            .subscribe(
                (data: ResponseDTO) => {
                    this.utilService.closeDialog()
                    this.utilService.openPopupDialog( "Simulation result", data.message)
                    return
                }
        )
    }

    refreshTable(): void {
        this.table?.renderRows()
    }

    onFormSubmit(): void {
        this.generateTransactionSimulation()
    }

    onRun(): void {
        this.runTransactionSimulation()
    }

    isSimulatorTransactionListEmpty(): boolean {
        return this.simulatorTransactionsList.length == 0
    }
}
