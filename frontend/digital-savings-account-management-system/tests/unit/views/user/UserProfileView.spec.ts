import { shallowMount } from '@vue/test-utils';
import UserProfileView from '@/views/user/UserProfileView.vue';
import * as usersApi from '@/api/endpoints/users/users.api';
import { toast } from '@steveyuowo/vue-hot-toast';

/*
 @author Khamitkar Sai Chandan
*/

jest.mock('@/api/endpoints/users/users.api');
jest.mock('@steveyuowo/vue-hot-toast', () => ({
    toast: { success: jest.fn(), error: jest.fn() },
    Toaster: { template: '<div />' }
}));

const mockedGetUserProfileDetails = usersApi.getUserProfileDetails as jest.MockedFunction<
    typeof usersApi.getUserProfileDetails
>;

const mockedToastSuccess = toast.success as jest.MockedFunction<typeof toast.success>;
const mockedToastError = toast.error as jest.MockedFunction<typeof toast.error>;

const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0));

const mockUserProfile = {
    accountId: 123,
    accountNumber: 'ACC-0001',
    accountHolderName: 'John Doe',
    balance: 5000,
    status: 'ACTIVE',
    accountType: 'Premium',
    dob: '1990-01-01',
    applicationDate: '2024-01-10T10:00:00',
    reviewDate: '2024-01-12T10:00:00',
    occupation: 'Engineer',
    incomeSource: 'Salary',
    annualIncome: 100000,
    parentName: 'Jane Doe',
    transactionsCount: 15
};

describe('UserProfileView.vue - Unit Tests', () => {
    beforeEach(() => {
        jest.clearAllMocks();

        mockedGetUserProfileDetails.mockResolvedValue({
            data: mockUserProfile,
            status: 200,
            statusText: 'OK',
            headers: {},
            config: {},
        } as any);
    });

    it('should mount and fetch user profile successfully', async () => {
        const wrapper = shallowMount(UserProfileView, {
            global: { stubs: { Toaster: true } }
        });

        await flushPromises();

        expect(wrapper.exists()).toBe(true);
        expect(mockedGetUserProfileDetails).toHaveBeenCalled();
        expect(wrapper.vm.userProfile).toEqual(mockUserProfile);
        expect(wrapper.vm.isLoading).toBe(false);
    });

    it('handles API failure with error toast', async () => {
        mockedGetUserProfileDetails.mockRejectedValueOnce(new Error('API Error'));

        const wrapper = shallowMount(UserProfileView, {
            global: { stubs: { Toaster: true } }
        });

        // trigger fetch manually since mounted() is async
        await wrapper.vm.fetchUserProfile();
        await flushPromises();

        expect(mockedToastError).toHaveBeenCalledWith(
            'Failed to load profile. Please try again later.'
        );
        expect(wrapper.vm.isLoading).toBe(false);
    });

    it('copies text to clipboard successfully', async () => {
        const writeTextMock = jest.fn().mockResolvedValue(undefined);
        Object.assign(navigator, { clipboard: { writeText: writeTextMock } });

        const wrapper = shallowMount(UserProfileView, {
            global: { stubs: { Toaster: true } }
        });

        await wrapper.vm.copyToClipboard('hello world');
        expect(writeTextMock).toHaveBeenCalledWith('hello world');
        expect(mockedToastSuccess).toHaveBeenCalledWith('Copied to clipboard!');
    });


    it('handles clipboard copy failure', async () => {
        const writeTextMock = jest.fn().mockRejectedValue(new Error('Failed'));
        Object.assign(navigator, { clipboard: { writeText: writeTextMock } });

        const wrapper = shallowMount(UserProfileView, {
            global: { stubs: { Toaster: true } }
        });

        await wrapper.vm.copyToClipboard('fail test');
        expect(writeTextMock).toHaveBeenCalledWith('fail test');
        expect(mockedToastError).toHaveBeenCalledWith('Failed to copy!');
    });
});
