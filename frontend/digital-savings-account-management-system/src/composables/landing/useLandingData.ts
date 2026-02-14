// @author Khamitkar Sai Chandan

import {
    faBolt as faBoltSolid,
    faCrown,
    faWallet,
    faShield,
    faCheckCircle,
    faClock,
    faChartLine,
    faGauge,
    faPercent,
    faPlane
} from '@fortawesome/free-solid-svg-icons'
import type {
    TrustBadge,
    Benefit,
    AccountPlan,
    FAQ,
    FooterLink
} from '@/types/app/landing'

export function useLandingData() {
    const trustBadges: TrustBadge[] = [
        { icon: faShield, text: 'Secured Platform' },
        { icon: faCheckCircle, text: 'Verified Accounts' },
        { icon: faClock, text: 'Quick Approval' },
        { icon: faChartLine, text: 'Track Transactions' },
        { icon: faGauge, text: 'Zero Hidden Fees' }
    ]

    const benefits: Benefit[] = [
        {
            icon: faPercent,
            title: 'High Interest Rate',
            description: 'Earn up to 6.5% p.a. on your savings - one of the highest in the industry.',
            bgColor: 'bg-emerald-100',
            iconColor: 'text-emerald-600'
        },
        {
            icon: faPlane,
            title: 'Lifestyle Benefits',
            description: 'Airport lounge access, concierge services, and exclusive merchant offers.',
            bgColor: 'bg-blue-100',
            iconColor: 'text-blue-600'
        },
        {
            icon: faWallet,
            title: 'Minimum Balance Option',
            description: 'Start saving with zero minimum balance requirements on select plans.',
            bgColor: 'bg-purple-100',
            iconColor: 'text-purple-600'
        },
        {
            icon: faBoltSolid,
            title: 'Instant Banking',
            description: 'Open account in 5 minutes, get virtual debit card instantly.',
            bgColor: 'bg-amber-100',
            iconColor: 'text-amber-600'
        }
    ]

    const accountPlans: AccountPlan[] = [
            {
                name: 'ZETA_BASIC',
                code : 'ZETA_BASIC',
                description: 'Perfect for daily banking needs',
                minBalance: 10000,
                interest: 4.5,
                recommended: false,
                icon: faWallet,
                iconBgClass: 'bg-emerald-100',
                iconClass: 'text-emerald-600',
                headerClass: 'bg-white',
                features: [
                    { name: '4.5% Interest Rate', included: true },
                    { name: 'Free ATM Withdrawals (5/month)', included: true },
                    { name: 'Mobile Banking', included: true },
                    { name: 'Airport Lounge Access', included: false },
                    { name: 'Personal Relationship Manager', included: false },
                    { name: 'Worldwide ATM Fee Waiver', included: false }
                ]
            },
            {
                name: 'ZETA_PRIME',
                code : 'ZETA_PRIME',
                description: 'Most popular with extra benefits',
                minBalance: 15000,
                interest: 5.5,
                recommended: true,
                icon: faBoltSolid,
                iconBgClass: 'bg-blue-100',
                iconClass: 'text-blue-600',
                headerClass: 'bg-gradient-to-b from-white to-blue-50/30',
                features: [
                    { name: '5.5% Interest Rate', included: true },
                    { name: 'Free ATM Withdrawals (10/month)', included: true },
                    { name: 'Mobile Banking', included: true },
                    { name: 'Airport Lounge Access (4/year)', included: true },
                    { name: 'Personal Relationship Manager', included: false },
                    { name: 'Worldwide ATM Fee Waiver', included: false }
                ]
            },
            {
                name: 'ZETA_ULTRA',
                code : 'ZETA_ULTRA',
                description: 'Premium banking with luxury benefits',
                minBalance: 25000,
                interest: 6.5,
                recommended: false,
                icon: faCrown,
                iconBgClass: 'bg-amber-100',
                iconClass: 'text-amber-600',
                headerClass: 'bg-gradient-to-b from-white to-amber-50/30',
                features: [
                    { name: '6.5% Interest Rate', included: true },
                    { name: 'Free ATM Withdrawals (Unlimited)', included: true },
                    { name: 'Mobile Banking', included: true },
                    { name: 'Airport Lounge Access (12/year)', included: true },
                    { name: 'Personal Relationship Manager', included: true },
                    { name: 'Worldwide ATM Fee Waiver', included: true }
                ]
            }
        ]

    const faqs: FAQ[] = [
        {
            question: 'How do I apply for a savings account?',
            answer:
                "Simply click 'Apply Now' and fill out the online application form with your personal details. Our verification team will review your application, and you'll be notified of the status via email."
        },
        {
            question: 'What information do I need to provide?',
            answer:
                'You need to provide your full name, date of birth, email, occupation, income source, annual income, and parent/guardian name. All fields are required for application processing.'
        },
        {
            question: 'How long does application approval take?',
            answer:
                'Our verification team typically reviews applications within 24-48 hours. You can track your application status anytime from your dashboard once you log in.'
        },
        {
            question: 'What are the different account types?',
            answer:
                'We offer three account tiers: Zeta Basic (standard savings), Zeta Prime (enhanced benefits with higher balance), and Zeta Ultra (premium features with maximum interest rates). Choose based on your savings goals.'
        },
        {
            question: 'Can I deposit and withdraw money?',
            answer:
                'Yes! Once your account is approved and activated, you can deposit and withdraw funds directly from your dashboard. All transactions are recorded and visible in your transaction history.'
        },
        {
            question: 'Is my information secure?',
            answer:
                'Absolutely! We use industry-standard security practices including JWT authentication, encrypted passwords, and secure API endpoints. Your data is protected and never shared with third parties.'
        }
    ]

    const productLinks: FooterLink[] = [
        { text: 'Savings Account', url: '/apply' },
    ]

    const companyLinks: FooterLink[] = [
        { text: 'About Us', url: '#' },
        { text: 'Careers', url: '#' },
        { text: 'Press Room', url: '#' },
        { text: 'Investor Relations', url: '#' },
        { text: 'Blog', url: '#' }
    ]

    const supportLinks: FooterLink[] = [
        { text: 'Help Center', url: '#' },
        { text: 'Contact Us', url: '#' },
        { text: 'Report Fraud', url: '#' },
        { text: 'Grievance Redressal', url: '#' },
        { text: 'Download Forms', url: '#' }
    ]

    return {
        trustBadges,
        benefits,
        accountPlans,
        faqs,
        productLinks,
        companyLinks,
        supportLinks
    }
}
