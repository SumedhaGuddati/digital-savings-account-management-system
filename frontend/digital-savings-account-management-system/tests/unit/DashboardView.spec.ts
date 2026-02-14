import { shallowMount, VueWrapper } from '@vue/test-utils';
import DashboardView from '@/views/admin/DashboardView.vue';
import * as adminApi from '@/api/endpoints/admin/admin.api';

/**
 * MODULE MOCKING
 * We abstract the API layer to ensure the unit test remains isolated
 * from network dependencies.
 */
jest.mock('@/api/endpoints/admin/admin.api');

const mockedGetStats = adminApi.getDashboardStats as jest.MockedFunction<typeof adminApi.getDashboardStats>;
const mockedGetApproved = adminApi.getApprovedApplications as jest.MockedFunction<typeof adminApi.getApprovedApplications>;

/**
 * ASYNCHRONOUS UTILITY
 * Flushes the microtask queue to ensure reactive updates and
 * resolved promises are processed before assertions.
 */
const flushPromises = () => new Promise(resolve => setTimeout(resolve, 0));

// Mock Data Definitions (Fixtures)
const mockStats = {
  managedAssets: 2000000,
  managedAssetsGrowth: 5.2,
  approvedAppsCount: 12,
  activeUsersCount: 50,
  newUsersToday: 3,
  averageYield: 4.5,
  pendingAppsCount: 2,
  adminCount: 5,
  customerCount: 45
};

const mockApprovedApps = [
  {
    applicationId: 1,
    applicantName: 'John Doe',
    applicantEmail: 'john@example.com',
    status: 'APPROVED',
    accountType: 'Premium',
    applicationDate: '2024-01-10T10:00:00',
    reviewDate: '2024-01-12T10:00:00',
    reviewedBy: 'Admin'
  }
];

describe('DashboardView.vue - Unit Tests', () => {
  
  beforeEach(() => {
    jest.clearAllMocks();
    
    // Default Stubbing: Define successful behavior for primary data fetch
    mockedGetStats.mockResolvedValue(mockStats);
    mockedGetApproved.mockResolvedValue(mockApprovedApps);
  });

  /**
   * TEST CASE: Component Initialization
   * Verifies that the component mounts and triggers the initial data fetch.
   */
  it('should initialize successfully and trigger data ingestion', async () => {
    const wrapper = shallowMount(DashboardView);
    await flushPromises();

    expect(wrapper.exists()).toBe(true);
    expect(mockedGetStats).toHaveBeenCalled();
    expect(mockedGetApproved).toHaveBeenCalled();
  });

  /**
   * TEST CASE: State Mutation
   * Verifies that the internal component state is correctly populated after 
   * a successful API response.
   */
  it('should correctly map API response data to the internal state', async () => {
    const wrapper = shallowMount(DashboardView) as any;
    await flushPromises();

    expect(wrapper.vm.stats.managedAssets).toBe(2000000);
    expect(wrapper.vm.approvedApplications).toHaveLength(1);
    expect(wrapper.vm.loading).toBe(false);
  });

  /**
   * TEST CASE: Error Boundary / Exception Handling
   * Verifies that the component enters an error state when the API returns
   * a non-200 status code.
   */
  it('should gracefully manage exception states during API failures', async () => {
    // Intercept console.error to maintain clean test output
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});
    
    // Simulate Service Failure
    mockedGetStats.mockRejectedValue({ response: { status: 500 } });
    mockedGetApproved.mockRejectedValue({ response: { status: 500 } });

    const wrapper = shallowMount(DashboardView) as any;
    await flushPromises();

    // Assertions for error state management
    expect(wrapper.vm.error).toMatch(/Server error/i);
    expect(wrapper.vm.loading).toBe(false);
    
    // Cleanup: Restore original console behavior
    consoleSpy.mockRestore();
  });

  /**
   * TEST CASE: Pure Helper Logic
   * Validates the formatting utility methods within the component.
   */
  it('should accurately format currency values using the internal utility', () => {
    const wrapper = shallowMount(DashboardView) as any;
    
    expect(wrapper.vm.formatCurrency(1500)).toBe('$1.5K');
    expect(wrapper.vm.formatCurrency(0)).toBe('$0');
  });
});