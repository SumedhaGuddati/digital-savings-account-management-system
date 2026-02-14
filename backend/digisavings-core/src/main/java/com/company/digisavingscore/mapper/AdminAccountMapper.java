package com.company.digisavingscore.mapper;

import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountMapper {

    public AdminApplicationViewResponse toAdminApplicationView(AccountApplication application) {

        if (application == null) {
            throw new IllegalArgumentException("AccountApplication cannot be null");
        }

        User user = application.getUser();
        if (user == null) {
            throw new IllegalStateException(
                    "AccountApplication " + application.getId() + " has no associated User"
            );
        }

        return AdminApplicationViewResponse.builder()
                .applicationId(application.getId())
                .applicantName(user.getName())
                .applicantEmail(user.getEmail())
                .applicationDate(application.getApplicationDate())
                .status(application.getStatus().name())
                .reviewedBy(application.getReviewedBy())
                .reviewDate(application.getReviewDate())
                .build();
    }
}