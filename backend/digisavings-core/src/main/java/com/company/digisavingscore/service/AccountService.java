package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.TransactionRequest;
import com.company.digisavingscore.dto.response.AccountDetailsResponse;
import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.entity.AccountTransaction;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.enums.TransactionType;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.exception.AccountInactiveException;
import com.company.digisavingscore.exception.BusinessRuleViolationException;
import com.company.digisavingscore.exception.InsufficientBalanceException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.TransactionMapper;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import com.company.digisavingscore.repository.AccountTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
@Transactional
public class AccountService implements IAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional(readOnly = true)
    public AccountDetailsResponse getAccountDetails(Long accountId) {
        log.debug("Fetching account details | accountId={}", accountId);

        SavingsAccount account = getAccountOrThrow(accountId);

        log.info("Account details fetched | accountId={}", accountId);

        return AccountDetailsResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getCustomer().getFullName())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }

    @Override
    public TransactionResponse deposit(TransactionRequest request) {
        log.info("Deposit request received | accountId={} | amount={}", request.getAccountId(), request.getAmount());

        SavingsAccount account = getActiveAccount(request.getAccountId());
        BigDecimal amount = validateAmount(request.getAmount());

        account.setBalance(account.getBalance().add(amount));
        savingsAccountRepository.save(account);

        log.debug("Balance updated | accountId={}", account.getId());

        AccountTransaction transaction = saveTransaction(
                account,
                TransactionType.DEPOSIT,
                amount,
                request.getDescription()
        );

        log.info("Deposit successful | accountId={} | transactionId={} | deposited amount={}", account.getId(), transaction.getId(), amount);

        return transactionMapper.toResponse(transaction, "Deposit successful for accountId: " + request.getAccountId() + ", deposited: " + amount);
    }

    @Override
    public TransactionResponse withdraw(TransactionRequest request) {
        log.info("Withdraw request received | accountId={} | amount={}", request.getAccountId(), request.getAmount());

        SavingsAccount account = getActiveAccount(request.getAccountId());
        BigDecimal amount = validateAmount(request.getAmount());

        if (account.getBalance().compareTo(amount) < 0) {
            log.warn("Withdrawal failed — insufficient balance | accountId={} | balance={} | amount={}",
                    account.getId(), account.getBalance(), amount);
            throw new InsufficientBalanceException();
        }

        account.setBalance(account.getBalance().subtract(amount));
        savingsAccountRepository.save(account);

        log.debug("Balance updated | accountId={}", account.getId());

        AccountTransaction transaction = saveTransaction(
                account,
                TransactionType.WITHDRAWAL,
                amount,
                request.getDescription()
        );

        log.info("Withdraw successful | accountId={} | transactionId={} | withdrawn amount={}", account.getId(), transaction.getId(), amount);

        return transactionMapper.toResponse(transaction, "Withdrawal successful for accountId: " + request.getAccountId() + ", withdrawn: " + amount);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionHistoryResponse getTransactionHistory(Long accountId) {
        log.debug("Fetching transaction history | accountId={}", accountId);

        getAccountOrThrow(accountId);
        List<AccountTransaction> transactions = accountTransactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId);

        log.info("Transaction history fetched | accountId={} | count={}", accountId, transactions.size());

        return transactionMapper.toHistoryResponse(
                accountId,
                transactionMapper.toHistoryItems(transactions)
        );
    }

    private SavingsAccount getAccountOrThrow(Long accountId) {
        return savingsAccountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.warn("Account not found | accountId={}", accountId);
                    throw new ResourceNotFoundException("Account not found with id: " + accountId);
                });
    }

    private SavingsAccount getActiveAccount(Long accountId) {
        SavingsAccount account = getAccountOrThrow(accountId);

        if (account.getStatus() != AccountStatus.ACTIVE) {
            log.warn("Account inactive | accountId={} | status={}", accountId, account.getStatus());
            throw new AccountInactiveException();
        }
        return account;
    }

    private BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Invalid transaction amount | amount={}", amount);
            throw new BusinessRuleViolationException("Requesting amount must be greater than zero");
        }
        return amount;
    }

    private AccountTransaction saveTransaction(
            SavingsAccount account,
            TransactionType type,
            BigDecimal amount,
            String description
    ) {
        log.debug("Saving transaction | accountId={} | type={} | amount={}", account.getId(), type, amount);

        AccountTransaction tx = AccountTransaction.builder()
                .account(account)
                .transactionType(type)
                .amount(amount)
                .transactionDate(LocalDateTime.now())
                .description(description)
                .build();

        return accountTransactionRepository.save(tx);
    }
}
