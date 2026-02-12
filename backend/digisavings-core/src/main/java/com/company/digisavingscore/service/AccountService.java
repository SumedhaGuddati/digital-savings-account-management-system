package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.TransactionRequest;
import com.company.digisavingscore.dto.response.AccountDetailsResponse;
import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.dto.response.UserProfileDetailsResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.AccountTransaction;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.enums.TransactionType;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.exception.AccountInactiveException;
import com.company.digisavingscore.exception.BusinessRuleViolationException;
import com.company.digisavingscore.exception.InsufficientBalanceException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.TransactionMapper;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import com.company.digisavingscore.repository.AccountTransactionRepository;
import com.company.digisavingscore.util.SecurityUtils;
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
    private final AccountApplicationRepository accountApplicationRepository;
    private final TransactionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public AccountDetailsResponse getAccountDetails() {
        SavingsAccount account = getCurrentUserAccount();
        AccountApplication application = getUserApplication(account.getUser().getId());

        log.info("Account details fetched | accountId={}", account.getId());
        return AccountDetailsResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getUser().getName())
                .balance(account.getBalance())
                .status(account.getStatus())
                .accountType(application.getAccountType())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDetailsResponse getProfileDetails() {
        SavingsAccount account = getCurrentUserAccount();
        AccountApplication application = getUserApplication(account.getUser().getId());

        log.info("Account details fetched | accountId={}", account.getId());
        return UserProfileDetailsResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getUser().getName())
                .balance(account.getBalance())
                .status(account.getStatus())
                .accountType(application.getAccountType())
                .dob(application.getDob())
                .applicationDate(application.getApplicationDate())
                .reviewDate(application.getReviewDate())
                .occupation(application.getOccupation())
                .incomeSource(application.getIncomeSource())
                .annualIncome(application.getAnnualIncome())
                .parentName(application.getParentName())
                .transactionsCount(accountTransactionRepository.count())
                .build();
    }


    @Override
    public TransactionResponse deposit(TransactionRequest request) {

        SavingsAccount account = getCurrentActiveUserAccount();
        BigDecimal amount = validateAmount(request.getAmount());

        log.info("Deposit request received | accountId={} | amount={}",
                account.getId(), request.getAmount());

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

        return mapper.toResponse(transaction, "Deposit successful for accountId: " + account.getId() + ", deposited: " + amount);
    }

    @Override
    public TransactionResponse withdraw(TransactionRequest request) {

        SavingsAccount account = getCurrentActiveUserAccount();
        BigDecimal amount = validateAmount(request.getAmount());

        log.info("Withdraw request received | accountId={} | amount={}", account.getId(), request.getAmount());

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
                TransactionType.WITHDRAW,
                amount,
                request.getDescription()
        );

        log.info("Withdraw successful | accountId={} | transactionId={} | withdrawn amount={}", account.getId(), transaction.getId(), amount);

        return mapper.toResponse(transaction, "Withdrawal successful for accountId: " + account.getId() + ", withdrawn: " + amount);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionHistoryResponse getTransactionHistory() {
        SavingsAccount account = getCurrentUserAccount();

        List<AccountTransaction> transactions = accountTransactionRepository.findByAccountIdOrderByTransactionDateDesc(account.getId());

        log.info("Transaction history fetched | accountId={} | count={}", account.getId(), transactions.size());

        return mapper.toHistoryResponse(
                account.getId(),
                mapper.toHistoryItems(transactions)
        );
    }

    private SavingsAccount getCurrentUserAccount() {
        String email = SecurityUtils.getCurrentUserEmail();
        return savingsAccountRepository.findByUserEmail(email)
                .orElseThrow(() -> {
                    log.warn("Account not found for user email={}", email);
                    return new ResourceNotFoundException("Account not found for current user");
                });
    }

    private SavingsAccount getCurrentActiveUserAccount() {
        SavingsAccount account = getCurrentUserAccount();

        if (account.getStatus() != AccountStatus.ACTIVE) {
            log.warn("Account inactive | accountId={} | status={}", account.getId(), account.getStatus());
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

    private AccountApplication getUserApplication(Long id) {
        return accountApplicationRepository.findByUserId(id)
                .orElseThrow(() -> {
                    log.warn("Application not found with id={}", id);
                    return new ResourceNotFoundException("Application not found");
                });
    }
}
