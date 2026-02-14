import { mount } from '@vue/test-utils';
import MainRegistration from '@/views/MainRegistration.vue';

describe('MainRegistration.vue', () => {
  beforeAll(() => {
    window.scrollTo = jest.fn();
  });

  it('renders the registration view and data fields', () => {
    const wrapper = mount(MainRegistration, {
      global: {
        stubs: ['router-link', 'router-view', 'font-awesome-icon', 'Toaster']
      }
    });

    expect(wrapper.exists()).toBe(true);
    expect(wrapper.text().toLowerCase()).toContain('account application');
    expect(wrapper.text().toLowerCase()).toContain('submit application');
  });
});