package ru.itis.skins_marketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorResponse {
    private String description;

    private String code;

    private String exceptionName;

    private String exceptionMessage;
}
