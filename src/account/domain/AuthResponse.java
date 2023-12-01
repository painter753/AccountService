package account.domain;

public record AuthResponse(
        String name,
        String lastname,
        String email
) { }
