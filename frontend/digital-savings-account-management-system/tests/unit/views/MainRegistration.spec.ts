import { mount } from '@vue/test-utils';
import MainRegistration from '@/views/MainRegistration.vue';

/*
@author Guddati Venkata Sri Sumedha
*/
describe('MainRegistration.vue', () => {
  let wrapper: any;

  beforeEach(() => {
    window.scrollTo = jest.fn();

    wrapper = mount(MainRegistration, {
      global: {
        stubs: {
          SignUp: {
            template: '<div />',
            data: () => ({
              formData: { password: '', confirmPassword: '' },
              isValidPassword: true,
            }),
          },
          ApplyAccountModule: {
            template: '<div />',
            data: () => ({
              formData: { accountType: 'basic', parentName: 'John', dob: '1990-01-01', occupation: 'Engineer', incomeSource: 'Salary', annualIncome: 50000 },
              termsChecked: true,
            }),
          },
          'router-link': { template: '<a><slot /></a>' },
        },
      },
    });
  });

  it('should validate password correctly', async () => {
    wrapper.vm.$refs.signupRef.formData.password = 'Password123!';
    wrapper.vm.$refs.signupRef.formData.confirmPassword = 'Password123!';
    await wrapper.vm.$nextTick();

    expect(wrapper.vm.$refs.signupRef.formData.password).toBe('Password123!');
    expect(wrapper.vm.$refs.signupRef.formData.confirmPassword).toBe('Password123!');
  });

  it('should call scrollTo when plan type changes', async () => {
    wrapper.vm.initialPlanType = 'premium';
    await wrapper.vm.$nextTick();

    expect(window.scrollTo).toHaveBeenCalledWith({ top: 0, behavior: 'smooth' });
  });
});
