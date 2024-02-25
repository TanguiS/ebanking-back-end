import {DirectDebitToBankAccountDTO} from "../direct_debit/DirectDebitToBankAccount.dto";
import {PaymentMethodRequestDTO} from "../request_payment_method/PaymentMethodRequest.dto";
import {PaymentMethodDTO} from "../payment_method/PaymentMethod.dto";
import {TransactionToBankAccountDTO} from "../transaction/TransactionToBankAccount.dto";
import {BeneficiaryToBankAccountDTO} from "../beneficiary/BeneficiaryToBankAccount.dto";
import {ClientDashboardDTO} from "../client/ClientDashboard.dto";
import {CardProductDTO} from "../card_product/CardProduct.dto";

export interface BankerDashboardDTO {
    clients: ClientDashboardDTO[];
    cardProducts: CardProductDTO[];
}
