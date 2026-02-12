import { ref, reactive, computed } from 'vue'

export function useProfile() {
    const isEditing = ref(false)
    const copied = ref(false)

    const userProfile = reactive({
        fullName: 'Rahul Sharma',
        email: 'rahul.sharma@email.com',
        phone: '+91 98765 43210',
        dateOfBirth: '1990-08-15',
        gender: 'Male',
        panCard: 'ABCDE1234F',
        memberSince: '2024-01-12',
        accountTier: 'ELITE',
        address: {
            line1: '42, Greenfield Apartments',
            line2: 'Andheri East',
            city: 'Mumbai',
            state: 'Maharashtra',
            pincode: '400069'
        }
    })

    const userAccount = reactive({
        accountNumber: '9876543212345678',
        ifscCode: 'SBIN0012345',
        accountType: 'ELITE Savings Account',
        balance: 126540.75,
        interestRate: 5.5,
        openedDate: '2024-01-15'
    })

    const userStats = reactive({
        accounts: 1,
        transactions: 47,
        rewards: 1250
    })

    const editableProfile = reactive({
        fullName: userProfile.fullName,
        email: userProfile.email,
        phone: userProfile.phone,
        dateOfBirth: userProfile.dateOfBirth,
        gender: userProfile.gender,
        panCard: userProfile.panCard,
        address: { ...userProfile.address }
    })

    const userInitials = computed(() =>
        userProfile.fullName
            .split(' ')
            .map(n => n[0])
            .join('')
            .toUpperCase()
            .slice(0, 2)
    )

    const formatDate = (date: string, format = 'DD MMM YYYY') => {
        const d = new Date(date)
        const day = d.getDate().toString().padStart(2, '0')
        const month = d.toLocaleString('default', { month: 'short' })
        const year = d.getFullYear()

        if (format === 'DD MMM YYYY') return `${day} ${month} ${year}`

        return d.toLocaleDateString('en-IN', {
            day: 'numeric',
            month: 'long',
            year: 'numeric'
        })
    }

    const copyToClipboard = async (text: string) => {
        await navigator.clipboard.writeText(text)
        copied.value = true
        setTimeout(() => (copied.value = false), 2000)
    }

    return {
        isEditing,
        copied,
        userProfile,
        userAccount,
        userStats,
        editableProfile,
        userInitials,
        formatDate,
        copyToClipboard
    }
}
