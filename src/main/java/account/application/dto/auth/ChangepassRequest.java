package account.application.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChangepassRequest(
        @JsonProperty("new_password")
        String newPassword) {
}
