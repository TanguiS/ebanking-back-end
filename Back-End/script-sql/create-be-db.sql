DROP SCHEMA IF EXISTS "User" CASCADE;
CREATE SCHEMA "User";

DROP SCHEMA IF EXISTS "Account" CASCADE;
CREATE SCHEMA "Account";

DROP SCHEMA IF EXISTS "RequestPaymentMethod" CASCADE;
CREATE SCHEMA "RequestPaymentMethod";

DROP SCHEMA IF EXISTS "PaymentMethod" CASCADE;
CREATE SCHEMA "PaymentMethod";

DROP SCHEMA IF EXISTS "Card" CASCADE;
CREATE SCHEMA "Card";

DROP SCHEMA IF EXISTS "Enum" CASCADE;
CREATE SCHEMA "Enum";

CREATE TYPE "Enum"."Currency" AS ENUM (
	'EUR',
	'CNY',
	'USD',
	'GBP'
);

CREATE TYPE "Enum"."RequestStatus" AS ENUM (
    'PENDING',
    'AUTHORIZED',
    'UNAUTHORIZED'
);

CREATE TYPE "Enum"."Schedule" AS ENUM(
    'NOT_RECURRENT',
    'EVERY_DAY',
    'EVERY_MONTH',
    'EVERY_YEAR'
);

CREATE TYPE "Enum"."RoleType" AS ENUM (
    'ROLE_CLIENT',
    'ROLE_BANKER',
    'ROLE_ACCOUNTANT',
    'ROLE_MASTER',
    'ROLE_SIMULATOR',
    'ROLE_TRANSACTION_COLLECTOR',
    'ROLE_ATM'
);

CREATE TYPE "Enum"."CardType" AS ENUM (
    'DEBIT_CARD',
    'CREDIT_CARD'
);

CREATE TYPE "Enum"."BankAccountType" AS ENUM (
    'YOUTH_PASSBOOK',
    'SAVING_ACCOUNT',
    'CURRENT_ACCOUNT',
    'ATM_ACCOUNT'
);

CREATE TYPE "Enum"."TransactionStatus" AS ENUM (
    'PENDING',
    'APPROVED',
    'REJECTED'
);

CREATE TYPE "Enum"."CardStatus" AS ENUM (
    'BLOCKED',
    'DISABLE',
    'ACTIVATED'
);

CREATE TYPE "Enum"."AuthorizationType" AS ENUM (
    'OFF_LINE',
    'ON_LINE'
);

CREATE TYPE "Enum"."CardScheme" as ENUM (
    'MASTERCARD',
    'VISA',
    'CB'
);

CREATE TYPE "Enum"."PriorityUseLevel" as ENUM (
    'FIRST',
    'SECOND',
    'THIRD'
);

CREATE TYPE "Enum"."OrderStatus" AS ENUM (
    'NOT_ORDERED',
    'ORDERED',
    'RECEIVED'
);

CREATE TYPE "Enum"."TransactionType" AS ENUM (
    'DEPOSIT',
    'TRANSFER',
    'CREDIT_CARD',
    'DEBIT_CARD',
    'MOBILE_PAYMENT',
    'CHEQUE'
);

CREATE TYPE "Enum"."TransactionAccountingDirection" AS ENUM (
    'DEBIT',
    'CREDIT'
);

CREATE TYPE "Enum"."TransferStatus" AS ENUM (
  'DONE',
  'IN_PROGRESS'
);

CREATE TABLE "User"."UserInformation" (
                                    "idUser" SERIAL PRIMARY KEY,
                                    "FirstName" varchar NOT NULL,
                                    "LastName" varchar NOT NULL,
                                    "Password" varchar NOT NULL,
                                    "Email" varchar NOT NULL,
                                    "Gender" int NOT NULL,
                                    "RequestStatus" "Enum"."RequestStatus" NOT NULL,
                                    "PhoneNumber" varchar NOT NULL
);

CREATE TABLE "User"."UserRole" (
                            "idRole" SERIAL PRIMARY KEY,
                            "idUserInformation" int NOT NULL UNIQUE,
                            "RoleType" "Enum"."RoleType" NOT NULL
);

CREATE TABLE "Account"."AccountManager" (
                                "idAccountManager" SERIAL PRIMARY KEY,
                                "idUserInformation" int NOT NULL,
                                "idClientAccount" int UNIQUE,
                                "idBankerAccount" int UNIQUE,
                                "idATMAccount" int UNIQUE
);

CREATE TABLE "Account"."ClientAccount" (
                                    "idClientAccount" SERIAL PRIMARY KEY,
                                    "idBankAccount" int UNIQUE NOT NULL
);

CREATE TABLE "Account"."BankAccount" (
                                    "idBankAccount" SERIAL PRIMARY KEY,
                                    "IBAN" varchar NOT NULL UNIQUE,
                                    "RIB" varchar NOT NULL UNIQUE,
                                    "BankAccountType" "Enum"."BankAccountType" NOT NULL,
                                    "Amount" int NOT NULL
);

CREATE TABLE "RequestPaymentMethod"."RequestPaymentMethodManager" (
                                  "idRequestPaymentMethodManager" SERIAL PRIMARY KEY,
                                  "idRequestPaymentCard" int,
                                  "idRequestPaymentChequeBook" int,
                                  "idBankAccount" int NOT NULL
);

CREATE TABLE "RequestPaymentMethod"."RequestCard" (
                                              "idRequestCard" SERIAL PRIMARY KEY,
                                              "idCardInformation" int NOT NULL,
                                              "idRequestPaymentMethodStatus" int NOT NULL
);

CREATE TABLE "RequestPaymentMethod"."RequestChequeBook" (
                                          "idRequestChequeBook" SERIAL PRIMARY KEY,
                                          "idRequestPaymentMethodStatus" int NOT NULL
);


CREATE TABLE "Account"."Transaction" (
                                    "idTransaction" SERIAL PRIMARY KEY,
                                    "Amount" int NOT NULL,
                                    "Currency" "Enum"."Currency" NOT NULL,
                                    "TransactionDate" timestamp NOT NULL,
                                    "TransactionStatus" "Enum"."TransactionStatus" NOT NULL,
                                    "TransactionType" "Enum"."TransactionType" NOT NULL,
                                    "TransactionAccountingDirection" "Enum"."TransactionAccountingDirection" NOT NULL,
                                    "ClearedDate" timestamp,
                                    "PAN" varchar,
                                    "IBAN" varchar
);

CREATE TABLE "Account"."TransactionToBankAccount" (
                                        "idTransactionToBankAccount" SERIAL PRIMARY KEY,
                                        "idTransaction" int NOT NULL,
                                        "idBankAccount" int NOT NULL
);

CREATE TABLE "PaymentMethod"."PaymentMethodManager" (
                                        "idPaymentMethodManager" SERIAL PRIMARY KEY,
                                        "idCheque" int,
                                        "idCard" int,
                                        "idBankAccount" int NOT NULL
);

CREATE TABLE "PaymentMethod"."ChequeBook" (
                               "idCheque" SERIAL PRIMARY KEY,
                               "PayerName" varchar NOT NULL,
                               "PayeeName" varchar NOT NULL
);

CREATE TABLE "PaymentMethod"."Card" (
                             "idCard" SERIAL PRIMARY KEY,
                             "idCardInformation" int NOT NULL UNIQUE,
                             "CardStatus" "Enum"."CardStatus" NOT NULL
);

CREATE TABLE "Card"."CardInformation" (
                                        "idCardInformation" SERIAL PRIMARY KEY,
                                        "idCardProduct" int NOT NULL,
                                        "PAN" varchar NOT NULL UNIQUE,
                                        "ExpirationCardDate" timestamp NOT NULL,
                                        "CVX2" int NOT NULL,
                                        "UpperLimitPerMonth" int NOT NULL,
                                        "UpperLimitPerTransaction" int NOT NULL
);

CREATE TABLE "Card"."CardProduct" (
                                    "idCardProduct" SERIAL PRIMARY KEY,
                                    "Name" varchar NOT NULL UNIQUE,
                                    "CardType" "Enum"."CardType" NOT NULL,
                                    "ContactlessUpperLimitPerTransaction" int NOT NULL,
                                    "NumberOfContactlessTransactionBeforeAskingPIN" int NOT NULL
);

CREATE TABLE "Card"."SoftwareCard" (
                                    "idSoftwareList" SERIAL PRIMARY KEY,
                                    "idCardProduct" int NOT NULL,
                                    "CardScheme" "Enum"."CardScheme" NOT NULL,
                                    "PriorityUseLevel" "Enum"."PriorityUseLevel" NOT NULL
);

CREATE TABLE "Card"."AuthorizationPolicy" (
                                             "idAuthorizationPolicy" SERIAL PRIMARY KEY,
                                             "idCardProduct" int NOT NULL,
                                             "AuthorizationType" "Enum"."AuthorizationType" NOT NULL,
                                             "LowerConsecutiveOfflineLimit" int,
                                             "UpperConsecutiveOfflineLimit" int,
                                             "CumulativeTotalTransactionAmountLimit" int,
                                             "CumulativeTotalTransactionAmountUpperLimit" int,
                                             "PriorityUseLevel" "Enum"."PriorityUseLevel" NOT NULL
);

CREATE TABLE "Account"."BeneficiaryToBankAccount" (
                                              "idBeneficiaryToBankAccount" SERIAL PRIMARY KEY,
                                              "idBeneficiary" int NOT NULL,
                                              "idBankAccount" int NOT NULL
);

CREATE TABLE "Account"."Beneficiary" (
                                    "idBeneficiary" SERIAL PRIMARY KEY,
                                    "FirstName" varchar NOT NULL,
                                    "LastName" varchar NOT NULL,
                                    "IBAN" varchar,
                                    "RIB" varchar,
                                    "idBankAccount" int NOT NULL
);

CREATE TABLE "Account"."BankerAccount" (
                                    "idBankerAccount" SERIAL PRIMARY KEY,
                                    "idUser" int
);

CREATE TABLE "Account"."ATMAccount" (
                                    "idATMAccount" SERIAL PRIMARY KEY,
                                    "idBankAccount" int NOT NULL
);

CREATE TABLE "Account"."DirectDebitToBankAccount"(
                                    "idDirectDebitToBankAccount" SERIAL PRIMARY KEY,
                                    "idDirectDebit" int NOT NULL,
                                    "idBankAccount" int NOT NULL
);

CREATE TABLE "Account"."DirectDebit"(
                                    "idDirectDebit" SERIAL PRIMARY KEY,
                                    "IBAN" varchar NOT NULL,
                                    "Recurrence" "Enum"."Schedule" NOT NULL,
                                    "idIndividual" int,
                                    "idLegalEntity" int
);

CREATE TABLE "Account"."Individual" (
                                   "idIndividual" SERIAL PRIMARY KEY,
                                   "FirstName" varchar NOT NULL,
                                   "LastName" varchar NOT NULL
);

CREATE TABLE "Account"."LegalEntity" (
                                    "idLegalEntity" SERIAL PRIMARY KEY,
                                    "Name" varchar NOT NULL,
                                    "NumberSiret" varchar NOT NULL
);

CREATE TABLE "RequestPaymentMethod"."RequestPaymentMethodStatus" (
                                                   "idRequestPaymentMethodStatus" SERIAL PRIMARY KEY,
                                                   "UserRequestPaymentMethodDate" timestamp NOT NULL,
                                                   "BankRequestPaymentMethodDate" timestamp,
                                                   "BankReceivedPaymentMethod" timestamp,
                                                   "UserReceivedPaymentMethod" timestamp,
                                                   "OrderStatus" "Enum"."OrderStatus" NOT NULL
);

ALTER TABLE "User"."UserRole" ADD FOREIGN KEY ("idUserInformation") REFERENCES "User"."UserInformation" ("idUser");
ALTER TABLE "Account"."AccountManager" ADD FOREIGN KEY ("idUserInformation") REFERENCES "User"."UserInformation" ("idUser");
ALTER TABLE "Account"."AccountManager" ADD FOREIGN KEY ("idClientAccount") REFERENCES "Account"."ClientAccount" ("idClientAccount");

ALTER TABLE "Account"."ClientAccount" ADD FOREIGN KEY ("idBankAccount") REFERENCES "Account"."BankAccount" ("idBankAccount");

ALTER TABLE "RequestPaymentMethod"."RequestPaymentMethodManager" ADD FOREIGN KEY ("idBankAccount") REFERENCES "Account"."BankAccount" ("idBankAccount");
ALTER TABLE "RequestPaymentMethod"."RequestPaymentMethodManager" ADD FOREIGN KEY ("idRequestPaymentCard") REFERENCES "RequestPaymentMethod"."RequestCard" ("idRequestCard");
ALTER TABLE "RequestPaymentMethod"."RequestCard" ADD FOREIGN KEY ("idCardInformation") REFERENCES "Card"."CardInformation" ("idCardInformation");
ALTER TABLE "RequestPaymentMethod"."RequestCard" ADD FOREIGN KEY ("idRequestPaymentMethodStatus") REFERENCES "RequestPaymentMethod"."RequestPaymentMethodStatus" ("idRequestPaymentMethodStatus");
ALTER TABLE "RequestPaymentMethod"."RequestPaymentMethodManager" ADD FOREIGN KEY ("idRequestPaymentChequeBook") REFERENCES "RequestPaymentMethod"."RequestChequeBook" ("idRequestChequeBook");
ALTER TABLE "RequestPaymentMethod"."RequestChequeBook" ADD FOREIGN KEY ("idRequestPaymentMethodStatus") REFERENCES "RequestPaymentMethod"."RequestPaymentMethodStatus" ("idRequestPaymentMethodStatus");

ALTER TABLE "Account"."TransactionToBankAccount" ADD FOREIGN KEY ("idTransaction") REFERENCES "Account"."Transaction" ("idTransaction");
ALTER TABLE "Account"."TransactionToBankAccount" ADD FOREIGN KEY ("idBankAccount") REFERENCES "Account"."BankAccount" ("idBankAccount");

ALTER TABLE "PaymentMethod"."PaymentMethodManager" ADD FOREIGN KEY ("idBankAccount") REFERENCES "Account"."BankAccount" ("idBankAccount");

ALTER TABLE "PaymentMethod"."PaymentMethodManager" ADD FOREIGN KEY ("idCheque") REFERENCES "PaymentMethod"."ChequeBook"("idCheque");

ALTER TABLE "PaymentMethod"."PaymentMethodManager" ADD FOREIGN KEY ("idCard") REFERENCES "PaymentMethod"."Card" ("idCard");
ALTER TABLE "PaymentMethod"."Card" ADD FOREIGN KEY ("idCardInformation") REFERENCES "Card"."CardInformation" ("idCardInformation");
ALTER TABLE "Card"."AuthorizationPolicy" ADD FOREIGN KEY ("idCardProduct") REFERENCES "Card"."CardProduct" ("idCardProduct");
ALTER TABLE "Card"."SoftwareCard" ADD FOREIGN KEY ("idCardProduct") REFERENCES "Card"."CardProduct" ("idCardProduct");
ALTER TABLE "Card"."CardInformation" ADD FOREIGN KEY ("idCardProduct") REFERENCES "Card"."CardProduct" ("idCardProduct");

ALTER TABLE "Account"."BeneficiaryToBankAccount" ADD FOREIGN KEY ("idBeneficiary") REFERENCES "Account"."Beneficiary"("idBeneficiary");

ALTER TABLE "Account"."AccountManager" ADD FOREIGN KEY ("idBankerAccount") REFERENCES "Account"."BankerAccount"("idBankerAccount");
ALTER TABLE "Account"."BankerAccount" ADD FOREIGN KEY ("idUser") REFERENCES "User"."UserInformation"("idUser");

ALTER TABLE "Account"."BeneficiaryToBankAccount" ADD FOREIGN KEY ("idBankAccount") REFERENCES "Account"."BankAccount"("idBankAccount");

ALTER TABLE "Account"."ATMAccount" ADD FOREIGN KEY ("idBankAccount") REFERENCES "Account"."BankAccount"("idBankAccount");
ALTER TABLE "Account"."AccountManager" ADD FOREIGN KEY ("idATMAccount") REFERENCES "Account"."ATMAccount"("idATMAccount");

ALTER TABLE "Account"."DirectDebitToBankAccount" ADD FOREIGN KEY ("idBankAccount") REFERENCES "Account"."BankAccount"("idBankAccount");
ALTER TABLE "Account"."DirectDebitToBankAccount"ADD FOREIGN KEY ("idDirectDebit") REFERENCES "Account"."DirectDebit"("idDirectDebit");

ALTER TABLE "Account"."DirectDebit" ADD FOREIGN KEY ("idIndividual") REFERENCES "Account"."Individual" ("idIndividual");
ALTER TABLE "Account"."DirectDebit" ADD FOREIGN KEY ("idLegalEntity") REFERENCES "Account"."LegalEntity" ("idLegalEntity");
