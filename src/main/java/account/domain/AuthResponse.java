package account.domain;

public record AuthResponse(
        Long id,
        String name,
        String lastname,
        String email
) { }
