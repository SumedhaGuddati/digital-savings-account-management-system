import { mount } from '@vue/test-utils';
import ApplyAccount from '@/components/ApplyAccount.vue';

describe('ApplyAccount.vue', () => {
  let wrapper;

  beforeEach(() => {
    wrapper = mount(ApplyAccount, {
      global: { stubs: ['TermsAndConditions', 'font-awesome-icon'] }
    });
  });

  it('triggers all account selection methods', async () => {
    const cards = wrapper.findAll('.account-card');
    
    await cards.at(0).trigger('click');
    expect(wrapper.vm.formData.accountType).toBe('ZETA_BASIC');
    
    await cards.at(1).trigger('click');
    expect(wrapper.vm.formData.accountType).toBe('ZETA_PRIME');
    
    await cards.at(2).trigger('click');
    expect(wrapper.vm.formData.accountType).toBe('ZETA_ULTRA');
  });

  it('covers modal open/close methods', async () => {
    await wrapper.find('.terms-link').trigger('click');
    expect(wrapper.vm.showTerms).toBe(true);

    wrapper.vm.closeTermsModal();
    expect(wrapper.vm.showTerms).toBe(false);
  });

  it('covers checkbox toggle method', async () => {
    await wrapper.find('.terms-checkbox-row').trigger('click');
    expect(wrapper.vm.termsChecked).toBe(true);
  });
});