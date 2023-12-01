package account.domain;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(
        @NotEmpty String name,
        @NotEmpty String lastname,
        @NotEmpty @Pattern(regexp = "^(.+)@acme.com$") String email,
        @NotEmpty String password
) { }

