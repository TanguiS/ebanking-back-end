export interface AuthorizationPolicyRequirementDTO {
    lowerConsecutiveOfflineLimit: number;
    upperConsecutiveOfflineLimit: number;
    cumulativeTotalTransactionAmountLimit: number;
    cumulativeTotalTransactionAmountUpperLimit: number;
}
