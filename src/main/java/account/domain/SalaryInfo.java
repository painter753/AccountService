package account.domain;

import java.time.LocalDate;

public record SalaryInfo(
        String name,
        String lastname,
        LocalDate period,
        Long salary
) { }
