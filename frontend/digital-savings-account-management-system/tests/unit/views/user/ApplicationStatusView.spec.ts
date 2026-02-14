import { shallowMount } from '@vue/test-utils'
import ApplicationStatusView from '@/views/user/ApplicationStatusView.vue'
import { getUserApplicationStatus } from '@/api/endpoints/users/users.api'
import * as applicationMapper from '@/mapper/application/application.mapper'
import { Application } from '@/types/app/application'

/*
  @author Karanam Hima Varsha
*/

const stubs = {
    FontAwesomeIcon: true,
    StatusHeader: true,
    ApplicationProgress: true,
    ApplicationDetails: true,
    ApprovedCard: true,
    RejectedCard: true,
    ApplicationTimeline: true,
    HelpCard: true,
    AppLoader: true,
    Toaster: true
}

const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0))

jest.mock('@/api/endpoints/users/users.api', () => ({
    getUserApplicationStatus: jest.fn()
}))

jest.mock('@/mapper/application/application.mapper', () => ({
    mapApiApplicationToUi: jest.fn()
}))

jest.mock('@steveyuowo/vue-hot-toast', () => ({
    toast: {
        error: jest.fn(),
        success: jest.fn()
    },
    Toaster: { template: '<div></div>' }
}))

describe('ApplicationStatusView.vue - Unit Tests', () => {
    let wrapper: any;
    let consoleErrorSpy: jest.SpyInstance;
    let mockRouter: any;

    const mockApiApplication = {
        id: 1,
        userId: 123,
        accountType: 'SAVINGS',
        applicationDate: '2026-02-10T10:00:00Z',
        status: 'PENDING' as const,
        reviewedBy: null,
        reviewDate: null
    }

    const mockUiApplication: Application = {
        id: 1,
        userId: 123,
        accountType: 'SAVINGS',
        applicationDate: '2026-02-10T10:00:00Z',
        status: 'PENDING',
        reviewedBy: null,
        reviewDate: null
    }

    beforeEach(() => {
        consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation(() => { });

        jest.clearAllMocks();
        mockRouter = {
            back: jest.fn(),
            push: jest.fn()
        };

        (getUserApplicationStatus as jest.Mock).mockResolvedValue({
            data: mockApiApplication
        });

        (applicationMapper.mapApiApplicationToUi as jest.Mock).mockReturnValue(mockUiApplication);
    })

    afterEach(() => {
        consoleErrorSpy.mockRestore();
        if (wrapper) {
            wrapper.unmount();
        }
    })

    const createWrapper = () => {
        return shallowMount(ApplicationStatusView, {
            global: {
                stubs,
                mocks: {
                    $router: mockRouter
                }
            }
        })
    }

    describe('Component Initialization', () => {
        it('renders correctly and fetches application status on mount', async () => {
            wrapper = createWrapper();

            expect(wrapper.exists()).toBe(true);
            await flushPromises();

            expect(getUserApplicationStatus).toHaveBeenCalledTimes(1);
            expect(applicationMapper.mapApiApplicationToUi).toHaveBeenCalledWith(mockApiApplication);
        })

        it('sets isLoading correctly during fetch', async () => {
            wrapper = createWrapper();

            expect(wrapper.vm.isLoading).toBe(true);

            await flushPromises();

            expect(wrapper.vm.isLoading).toBe(false);
        })

        it('sets application data after successful fetch', async () => {
            wrapper = createWrapper();

            await flushPromises();

            expect(wrapper.vm.application).toEqual(mockUiApplication);
        })
    })

    describe('Application Status - No Application', () => {
        it('shows "No Application Found" message when application is null', async () => {
            (getUserApplicationStatus as jest.Mock).mockRejectedValue(new Error('Not found'));

            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.vm.application).toBeNull();
            expect(wrapper.find('h2').text()).toContain('No Application Found');
        })

        it('navigates to home when "Apply for Savings Account" button is clicked', async () => {
            (getUserApplicationStatus as jest.Mock).mockRejectedValue(new Error('Not found'));

            wrapper = createWrapper();
            await flushPromises();

            const applyButton = wrapper.find('button[class*="bg-gradient-to-r"]');
            await applyButton.trigger('click');

            expect(mockRouter.push).toHaveBeenCalledWith('/');
        })
    })

    describe('Application Status - PENDING', () => {
        it('renders all components when application status is PENDING', async () => {
            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.findComponent({ name: 'StatusHeader' }).exists()).toBe(true);
            expect(wrapper.findComponent({ name: 'ApplicationProgress' }).exists()).toBe(true);
            expect(wrapper.findComponent({ name: 'ApplicationDetails' }).exists()).toBe(true);
            expect(wrapper.findComponent({ name: 'ApplicationTimeline' }).exists()).toBe(true);
            expect(wrapper.findComponent({ name: 'HelpCard' }).exists()).toBe(true);
        })

        it('does not render ApprovedCard or RejectedCard for PENDING status', async () => {
            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.findComponent({ name: 'ApprovedCard' }).exists()).toBe(false);
            expect(wrapper.findComponent({ name: 'RejectedCard' }).exists()).toBe(false);
        })
    })

    describe('Application Status - APPROVED', () => {
        beforeEach(() => {
            const approvedApplication = {
                ...mockUiApplication,
                status: 'APPROVED' as const,
                reviewedBy: 'admin@bank.com',
                reviewDate: '2026-02-12T15:00:00Z'
            };

            (applicationMapper.mapApiApplicationToUi as jest.Mock).mockReturnValue(approvedApplication);
        })

        it('renders ApprovedCard when application status is APPROVED', async () => {
            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.findComponent({ name: 'ApprovedCard' }).exists()).toBe(true);
            expect(wrapper.findComponent({ name: 'RejectedCard' }).exists()).toBe(false);
        })

        it('does not render ApplicationProgress for APPROVED status', async () => {
            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.findComponent({ name: 'ApplicationProgress' }).exists()).toBe(false);
        })
    })

    describe('Application Status - REJECTED', () => {
        beforeEach(() => {
            const rejectedApplication = {
                ...mockUiApplication,
                status: 'REJECTED' as const,
                reviewedBy: 'admin@bank.com',
                reviewDate: '2026-02-12T15:00:00Z'
            };

            (applicationMapper.mapApiApplicationToUi as jest.Mock).mockReturnValue(rejectedApplication);
        })

        it('renders RejectedCard when application status is REJECTED', async () => {
            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.findComponent({ name: 'RejectedCard' }).exists()).toBe(true);
            expect(wrapper.findComponent({ name: 'ApprovedCard' }).exists()).toBe(false);
        })

        it('does not render ApplicationProgress for REJECTED status', async () => {
            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.findComponent({ name: 'ApplicationProgress' }).exists()).toBe(false);
        })
    })

    describe('Navigation', () => {
        it('navigates back when back button is clicked', async () => {
            wrapper = createWrapper();
            await flushPromises();

            const backButton = wrapper.find('button[class*="hover:bg-white"]');
            await backButton.trigger('click');

            expect(mockRouter.back).toHaveBeenCalledTimes(1);
        })
    })

    describe('Loading State', () => {
        it('shows loader when isLoading is true', async () => {
            wrapper = createWrapper();

            const loader = wrapper.findComponent({ name: 'AppLoader' });
            expect(loader.exists()).toBe(true);
            expect(loader.attributes('class')).toContain('fixed');
        })
    })

    describe('Error Handling', () => {
        it('handles API error and sets application to null', async () => {
            const toast = require('@steveyuowo/vue-hot-toast').toast;
            (getUserApplicationStatus as jest.Mock).mockRejectedValue(new Error('API Error'));

            wrapper = createWrapper();
            await flushPromises();

            expect(wrapper.vm.application).toBeNull();
            expect(toast.error).toHaveBeenCalledWith(
                'Failed to fetch application status. Please try again later.'
            );
        })

        it('stops loading even when API call fails', async () => {
            (getUserApplicationStatus as jest.Mock).mockRejectedValue(new Error('Network Error'));

            wrapper = createWrapper();
            expect(wrapper.vm.isLoading).toBe(true);

            await flushPromises();

            expect(wrapper.vm.isLoading).toBe(false);
        })
    })

    describe('Component Props', () => {
        it('passes correct props to StatusHeader', async () => {
            wrapper = createWrapper();
            await flushPromises();

            const statusHeader = wrapper.findComponent({ name: 'StatusHeader' });
            expect(statusHeader.props('application')).toEqual(mockUiApplication);
        })

        it('passes correct props to ApplicationProgress', async () => {
            wrapper = createWrapper();
            await flushPromises();

            const applicationProgress = wrapper.findComponent({ name: 'ApplicationProgress' });
            expect(applicationProgress.props('application')).toEqual(mockUiApplication);
        })

        it('passes correct props to ApplicationDetails', async () => {
            wrapper = createWrapper();
            await flushPromises();

            const applicationDetails = wrapper.findComponent({ name: 'ApplicationDetails' });
            expect(applicationDetails.props('application')).toEqual(mockUiApplication);
        })

        it('passes correct props to ApplicationTimeline', async () => {
            wrapper = createWrapper();
            await flushPromises();

            const applicationTimeline = wrapper.findComponent({ name: 'ApplicationTimeline' });
            expect(applicationTimeline.props('application')).toEqual(mockUiApplication);
        })
    })

    describe('Computed Properties', () => {
        it('returns correct FontAwesome icons', () => {
            wrapper = createWrapper();

            expect(wrapper.vm.faArrowLeft).toBeDefined();
            expect(wrapper.vm.faFileCirclePlus).toBeDefined();
        })
    })

    describe('Manual fetchApplicationStatus Call', () => {
        it('can manually call fetchApplicationStatus method', async () => {
            wrapper = createWrapper();
            await flushPromises();

            jest.clearAllMocks();

            await wrapper.vm.fetchApplicationStatus();

            expect(getUserApplicationStatus).toHaveBeenCalledTimes(1);
            expect(applicationMapper.mapApiApplicationToUi).toHaveBeenCalledWith(mockApiApplication);
        })
    })
})