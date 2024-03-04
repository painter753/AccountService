package account.domain;

public record User(
         Long id,
         String name,
         String lastname,
         String email,
         String password
) {}
