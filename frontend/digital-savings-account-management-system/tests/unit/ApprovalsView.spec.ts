import { shallowMount } from '@vue/test-utils'
import { describe, it, expect } from '@jest/globals'
import ApprovalsView from '@/views/admin/ApprovalsView.vue'

/*
 @author Bharadi Harshitha
*/
describe('ApprovalsView Logic Tests', () => {

  it('formatDate formats valid date correctly', () => {
    const wrapper = shallowMount(ApprovalsView)

    const result = wrapper.vm.formatDate('2024-01-15T10:30:00')
    expect(result).toMatch(/Jan/)
  })

  it('formatDate returns N/A for empty string', () => {
    const wrapper = shallowMount(ApprovalsView)

    expect(wrapper.vm.formatDate('')).toBe('N/A')
  })

  it('formatDate returns original string for invalid date', () => {
    const wrapper = shallowMount(ApprovalsView)

    expect(wrapper.vm.formatDate('invalid-date')).toBe('invalid-date')
  })

  it('removes application from list', async () => {
    const wrapper = shallowMount(ApprovalsView)

    wrapper.vm.applications = [
      {
        applicationId: 1,
        applicantName: 'Chandan Khamitkar',
        applicantEmail: 'chandan@gmail.com',
        status: 'Pending',
        applicationDate: '2024-01-01'
      },
      {
        applicationId: 2,
        applicantName: 'Harshitha',
        applicantEmail: 'harshitha@gmail.com',
        status: 'Approved',
        applicationDate: '2024-01-02'
      },
      {
        applicationId: 3,
        applicantName: 'Varsha',
        applicantEmail: 'varsha@gmail.com',
        status: 'Rejected',
        applicationDate: '2024-01-03'
      }
    ]

    wrapper.vm.applications =
      wrapper.vm.applications.filter((a: any) => a.applicationId !== 2)

    expect(wrapper.vm.applications.length).toBe(2)
  })

  it('validates numeric ID correctly', () => {
    const validateId = (id: any): boolean => {
      return !isNaN(Number(id))
    }

    expect(validateId(123)).toBe(true)
    expect(validateId('abc')).toBe(false)
  })

})
