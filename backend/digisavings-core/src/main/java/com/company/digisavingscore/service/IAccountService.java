package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.TransactionRequest;
import com.company.digisavingscore.dto.response.AccountDetailsResponse;
import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.dto.response.UserProfileDetailsResponse;

/**
 * @author Khamitkar Sai Chandan
 */
public interface IAccountService {

    AccountDetailsResponse getAccountDetails();

    UserProfileDetailsResponse getProfileDetails();

    TransactionResponse deposit(TransactionRequest request);

    TransactionResponse withdraw(TransactionRequest request);

    TransactionHistoryResponse getTransactionHistory();
}
