// @author Khamitkar Sai Chandan

import type {
    Application as ApiApplication,
} from '@/types/api/application'

import type {
    Application as UiApplication
} from '@/types/app/application'

export const mapApiApplicationToUi = (api: ApiApplication): UiApplication => {
    return {
        id: api.id,
        userId: api.userId,
        accountType: api.accountType,
        applicationDate: api.applicationDate,
        status: api.status,
        reviewedBy: api.reviewedBy,
        reviewDate: api.reviewDate
    }
}

export const mapApiApplicationsToUi = (apiList: ApiApplication[]): UiApplication[] => {
    return apiList.map(mapApiApplicationToUi)
}
