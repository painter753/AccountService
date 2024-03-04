package account.presentation;

import account.application.AccountantService;
import account.application.dto.salary.UpsertSalaryInfoRequest;
import account.application.dto.salary.UpsertSalaryInfoResponse;
import account.domain.SalaryInfo;
import account.presentation.dto.SalaryInfoResponseDto;
import account.presentation.dto.UpsertSalaryInfoRequestDto;
import account.presentation.dto.UpsertSalaryInfoResponseDto;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
@Validated
public class AcctController {

    private final AccountantService accountantService;

    public AcctController(AccountantService accountantService) {
        this.accountantService = accountantService;
    }

    @PostMapping("/acct/payments")
    public UpsertSalaryInfoResponseDto uploadSalaryInfo(@RequestBody List<@Valid UpsertSalaryInfoRequestDto> requests) {
        return mapToDto(accountantService.addUserSalaries(mapFromDtos(requests)));
    }

    @PutMapping("/acct/payments")
    public UpsertSalaryInfoResponseDto editSalaryInfo(@RequestBody UpsertSalaryInfoRequestDto request) {
        return mapToDto(accountantService.editUserSalary(mapFromDto(request)));
    }

    @GetMapping("/empl/payment")
    public ResponseEntity<Object> getSalaries(
            @RequestParam(value = "period", required = false)
            @DateTimeFormat(pattern = "MM-yyyy") YearMonth period) {
        if (period == null) {
            return ResponseEntity.ok(accountantService.getSalaries().stream().map(this::mapToSalaryInfoResponse).toList());
        } else {
            return ResponseEntity.ok(mapToSalaryInfoResponse(accountantService.getSalaryForPeriod(period)));
        }
    }

    private List<UpsertSalaryInfoRequest> mapFromDtos(List<UpsertSalaryInfoRequestDto> request) {
        return request.stream().map(this::mapFromDto).toList();
    }

    private UpsertSalaryInfoRequest mapFromDto(UpsertSalaryInfoRequestDto dto) {
        return new UpsertSalaryInfoRequest(dto.employee(), dto.period(), dto.salary());
    }

    private UpsertSalaryInfoResponseDto mapToDto(UpsertSalaryInfoResponse req) {
        return new UpsertSalaryInfoResponseDto(req.status());
    }

    private SalaryInfoResponseDto mapToSalaryInfoResponse(SalaryInfo salaryInfo) {
        return new SalaryInfoResponseDto(
                salaryInfo.name(),
                salaryInfo.lastname(),
                convertPeriodToString(salaryInfo.period()),
                convertSalaryToString(salaryInfo.salary())
        );
    }

    private String convertPeriodToString(LocalDate period) {
        return period.format(DateTimeFormatter.ofPattern("MMMM-yyyy", Locale.ENGLISH));
    }

    private String convertSalaryToString(Long salary) {
        return (salary / 100) + " dollar(s) " + (salary % 100) + " cent(s)";
    }


}
