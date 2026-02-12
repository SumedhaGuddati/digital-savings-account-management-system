import {
    faBolt as faBoltSolid,
    faCrown,
    faWallet,
    faShield,
    faCheckCircle,
    faClock,
    faHeadset,
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
        { icon: faShield, text: 'RBI Regulated' },
        { icon: faCheckCircle, text: '100% Secure' },
        { icon: faClock, text: 'Instant Activation' },
        { icon: faHeadset, text: '24/7 Support' },
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
            title: 'Zero Balance Option',
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
            name: 'Everyday Smart',
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
            name: 'Prime Advantage',
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
            name: 'Aurora Elite',
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
            question: 'How quickly can I open a savings account?',
            answer:
                "You can open a savings account in under 5 minutes! Just fill our online application, complete e-KYC with Aadhaar and PAN, and your account will be activated instantly. You'll receive your virtual debit card immediately."
        },
        {
            question: 'What documents do I need to open an account?',
            answer:
                'You need your PAN Card and Aadhaar Card for e-KYC. If your address is different from Aadhaar, you may need additional address proof like passport, driving license, or utility bill.'
        },
        {
            question: 'Is there any maintenance fee?',
            answer:
                'No, we believe in transparent banking. There are no hidden charges or monthly maintenance fees. You only pay for specific services like demand drafts or statement requests.'
        },
        {
            question: 'Can I upgrade my account plan later?',
            answer:
                'Absolutely! You can upgrade your account anytime through our mobile app or website. As your balance grows, you can move to higher-tier plans with more benefits.'
        },
        {
            question: 'How do I get airport lounge access?',
            answer:
                "Prime Advantage customers get 4 complimentary lounge accesses per year, while Aurora Elite customers get 12 accesses. You'll receive a Priority Pass that can be used at 1000+ lounges worldwide."
        },
        {
            question: 'Is my money safe with ZetaFi?',
            answer:
                'Yes, your deposits are insured up to ₹5 lakhs by DICGC. We use bank-grade 256-bit encryption and are fully regulated by RBI. Your security is our top priority.'
        }
    ]

    const productLinks: FooterLink[] = [
        { text: 'Savings Account', url: '#' },
        { text: 'Fixed Deposits', url: '#' },
        { text: 'Recurring Deposits', url: '#' },
        { text: 'NRI Accounts', url: '#' },
        { text: 'Senior Citizen', url: '#' }
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
