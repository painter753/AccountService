package account.presentation.dto;

public record AuthResponseDto(
        Long id,
        String name,
        String lastname,
        String email
) { }
