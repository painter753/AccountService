package account.application.dto.auth;


public record AuthRequest(
        String name,
        String lastname,
        String email,
        String password
) {
}

