package account.application.dto.salary;

import java.time.YearMonth;

public record UpsertSalaryInfoRequest(
        String employee,
        YearMonth period,
        Long salary
) {
}
