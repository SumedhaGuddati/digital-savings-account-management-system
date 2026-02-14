import { mount } from '@vue/test-utils';
import DetailRow from '@/components/application-status/DetailRow.vue';

describe('DetailRow.vue', () => {
  it('checks if label and value are visible in the DOM', () => {
    const wrapper = mount(DetailRow, {
      props: { label: 'Annual Income' },
      slots: {
        default: '800000' 
      }
    });

    expect(wrapper.text()).toContain('Annual Income');
    expect(wrapper.text()).toContain('800000');
  });
});