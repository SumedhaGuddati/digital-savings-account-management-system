import { IconDefinition } from '@fortawesome/fontawesome-svg-core'

export interface TrustBadge {
    icon: IconDefinition
    text: string
}

export interface Benefit {
    icon: IconDefinition
    title: string
    description: string
    bgColor: string
    iconColor: string
}

export interface PlanFeature {
    name: string
    included: boolean
}

export interface AccountPlan {
    name: string
    description: string
    minBalance: number
    interest: number
    recommended: boolean
    icon: IconDefinition
    iconBgClass: string
    iconClass: string
    headerClass: string
    features: PlanFeature[]
}

export interface FAQ {
    question: string
    answer: string
}

export interface FooterLink {
    text: string
    url: string
}
