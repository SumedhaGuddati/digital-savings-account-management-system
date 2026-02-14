import { shallowMount, VueWrapper } from '@vue/test-utils';
import UserHomeView from '@/views/user/UserHomeView.vue';
import * as usersApi from '@/api/endpoints/users/users.api';
import { mapApiTransactionsToUi } from '@/mapper/banking/transaction.mapper';
import { TransactionType, Transaction } from '@/types/api/banking';
import { toast } from '@steveyuowo/vue-hot-toast';
import { Account, AccountStatus } from '@/types/app/banking';

jest.mock('@/api/endpoints/users/users.api');
jest.mock('@/mapper/banking/transaction.mapper');
jest.mock('@steveyuowo/vue-hot-toast', () => ({
    toast: { success: jest.fn(), error: jest.fn() },
    Toaster: { render: jest.fn() },
}));

const mockedGetUserHomeDetails = usersApi.getUserHomeDetails as jest.MockedFunction<
    typeof usersApi.getUserHomeDetails
>;
const mockedGetUserTransactions = usersApi.getUserTransactions as jest.MockedFunction<
    typeof usersApi.getUserTransactions
>;
const mockedMapApiTransactionsToUi = mapApiTransactionsToUi as jest.MockedFunction<
    typeof mapApiTransactionsToUi
>;
const mockedToast = toast as jest.Mocked<typeof toast>;

const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0));

const accountMock: Account = {
    accountHolderName: 'John Doe',
    accountNumber: '1234567890',
    accountType: 'Premium',
    balance: 10000,
    status: AccountStatus.ACTIVE,
};

const transactionsMock: Transaction[] = [
    {
        transactionId: 'tx1',
        description: 'Deposit',
        transactionDate: '2026-02-14T10:00:00',
        amount: 5000,
        transactionType: TransactionType.DEPOSIT,
    },
    {
        transactionId: 'tx2',
        description: 'Withdrawal',
        transactionDate: '2026-02-14T11:00:00',
        amount: 2000,
        transactionType: TransactionType.WITHDRAW,
    },
];

const uiTransactionsMock = [
    {
        id: 'tx1',
        description: 'Deposit',
        date: '2026-02-14T10:00:00',
        amount: 5000,
        type: TransactionType.DEPOSIT,
    },
    {
        id: 'tx2',
        description: 'Withdrawal',
        date: '2026-02-14T11:00:00',
        amount: 2000,
        type: TransactionType.WITHDRAW,
    },
];

describe('UserHomeView.vue - Unit Tests', () => {
    let wrapper: VueWrapper<any>;

    beforeEach(async () => {
        jest.clearAllMocks();

        // Default API stubs
        mockedGetUserHomeDetails.mockResolvedValue({
            data: accountMock,
            status: 200,
            statusText: 'OK',
            headers: {},
            config: {},
        } as any);
        mockedGetUserTransactions.mockResolvedValue({
            data: { transactions: transactionsMock },
            status: 200,
            statusText: 'OK',
            headers: {},
            config: {},
        } as any);
        mockedMapApiTransactionsToUi.mockReturnValue(uiTransactionsMock);

        wrapper = shallowMount(UserHomeView, {
            global: {
                stubs: ['AppNavbar', 'AppLoader', 'AccountCard', 'BalanceCard', 'QuickActions', 'TransactionsPreview', 'AccountBenefitsCard', 'HelpCard', 'TransactionModal', 'Toaster'],
                mocks: {
                    $router: { push: jest.fn() },
                },
            },
        });

        await flushPromises();
    });

    it('should render component and show loader initially', () => {
        expect(wrapper.exists()).toBe(true);
        expect(wrapper.vm.isLoading).toBe(false);
        expect(mockedGetUserHomeDetails).toHaveBeenCalled();
        expect(mockedGetUserTransactions).toHaveBeenCalled();
    });

    it('fetches account details and transactions on mount', () => {
        expect(wrapper.vm.account).toEqual(accountMock);
        expect(wrapper.vm.transactions).toEqual(uiTransactionsMock);
    });

    it('opens deposit modal correctly', async () => {
        wrapper.vm.openDepositModal();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.transactionType).toBe(TransactionType.DEPOSIT);
        expect(wrapper.vm.showTransactionModal).toBe(true);
    });

    it('opens withdraw modal correctly', async () => {
        wrapper.vm.openWithdrawModal();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.transactionType).toBe(TransactionType.WITHDRAW);
        expect(wrapper.vm.showTransactionModal).toBe(true);
    });

    it('handles transaction confirmation with toast and fetches data', async () => {
        await wrapper.vm.handleTransactionConfirmation(1000);

        expect(mockedToast.success).toHaveBeenCalledWith('DEPOSIT of ₹1000 successful!');
        expect(mockedGetUserHomeDetails).toHaveBeenCalledTimes(2); // initial + refresh
        expect(mockedGetUserTransactions).toHaveBeenCalledTimes(2);
    });

    it('navigates to profile correctly', async () => {
        wrapper.vm.goProfile();
        expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/me/profile');
    });

    it('navigates to statements correctly', async () => {
        wrapper.vm.goStatements();
        expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/me/transactions');
    });

    it('logs out correctly', async () => {
        wrapper.vm.handleLogout();
        expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/login');
    });

    it('sets error toast on API failure', async () => {
        const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => { });
        mockedGetUserHomeDetails.mockRejectedValue(new Error('API Error'));
        mockedGetUserTransactions.mockRejectedValue(new Error('API Error'));

        const errorWrapper = shallowMount(UserHomeView, {
            global: {
                stubs: ['AppNavbar', 'AppLoader', 'AccountCard', 'BalanceCard', 'QuickActions', 'TransactionsPreview', 'AccountBenefitsCard', 'HelpCard', 'TransactionModal', 'Toaster'],
                mocks: {
                    $router: { push: jest.fn() },
                },
            },
        });
        await flushPromises();

        expect(mockedToast.error).toHaveBeenCalledWith('Failed to fetch account details. Please try again later.');
        expect(mockedToast.error).toHaveBeenCalledWith('Failed to fetch transactions. Please try again later.');

        consoleSpy.mockRestore();
    });
});
