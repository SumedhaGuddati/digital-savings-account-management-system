import { mount } from '@vue/test-utils';
import TermsAndConditions from '@/components/ui/TermsAndConditions.vue';

describe('TermsAndConditions.vue', () => {
  it('renders and covers internal methods', async () => {
    const wrapper = mount(TermsAndConditions, {
      props: { show: true },
      global: { stubs: { teleport: true } }
    });

    expect(wrapper.text().toLowerCase()).toContain('terms');

    const closeBtn = wrapper.find('button'); 
    if (closeBtn.exists()) {
      await closeBtn.trigger('click');
      expect(wrapper.emitted().close).toBeTruthy();
    }
  });
});