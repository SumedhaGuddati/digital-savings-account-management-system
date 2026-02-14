import { mount } from '@vue/test-utils';
import ApplicationDetails from '@/components/application-status/ApplicationDetails.vue';

describe('ApplicationDetails.vue', () => {
  const mockApplication = {
    id: 101,
    status: 'PENDING',
    accountType: 'ZETA_PRIME',
    applicationDate: '2026-02-14T15:00:00' 
  };

  it('verifies application data is correctly inserted into the DOM', () => {
    const wrapper = mount(ApplicationDetails, {
      props: { application: mockApplication }
    });

    expect(wrapper.text()).toContain('ZETA_PRIME');
    expect(wrapper.text()).toContain('25,000');
    expect(wrapper.text()).toContain('14 February 2026');
  });

  it('handles empty application prop gracefully', () => {
    const wrapper = mount(ApplicationDetails, {
      props: { application: {} }
    });
    expect(wrapper.exists()).toBe(true);
  });
});