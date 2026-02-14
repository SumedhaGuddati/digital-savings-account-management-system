import { IconDefinition } from '@fortawesome/fontawesome-svg-core'

// @author Khamitkar Sai Chandan
export interface TrustBadge {
    icon: IconDefinition
    text: string
}

// @author Khamitkar Sai Chandan
export interface Benefit {
    icon: IconDefinition
    title: string
    description: string
    bgColor: string
    iconColor: string
}

// @author Khamitkar Sai Chandan
export interface PlanFeature {
    name: string
    included: boolean
}

// @author Karanam Hima Varsha
export interface AccountPlan {
    name: string;
    code: string; 
    description: string;
    minBalance: number;
    interest: number;
    recommended: boolean;
    icon: IconDefinition;
    iconBgClass: string;
    iconClass: string;
    headerClass: string;
    features: PlanFeature[];
}

// @author Khamitkar Sai Chandan
export interface FAQ {
    question: string
    answer: string
}

// @author Khamitkar Sai Chandan
export interface FooterLink {
    text: string
    url: string
}
