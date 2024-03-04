package account.presentation.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record AuthRequestDto(
        @NotEmpty String name,
        @NotEmpty String lastname,
        @NotEmpty @Pattern(regexp = "^(.+)@acme.com$") String email,
        @NotEmpty String password
) { }

