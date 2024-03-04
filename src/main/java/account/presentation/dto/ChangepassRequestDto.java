package account.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangepassRequestDto(
        @JsonProperty("new_password")
        String newPassword) {
}
