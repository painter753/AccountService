package account.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.YearMonth;

public record UpsertSalaryInfoRequestDto(
        @Pattern(regexp = "^(.+)@acme.com$", message = "Bad request: incorrect email") String employee,
        @JsonFormat(pattern = "MM-yyyy") YearMonth period,
        @PositiveOrZero(message = "Bad Request: incorrect salary value - must be >=0 ") Long salary
) { }
