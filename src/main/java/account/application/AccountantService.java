package account.application;

import account.application.dto.salary.UpsertSalaryInfoRequest;
import account.application.dto.salary.UpsertSalaryInfoResponse;
import account.domain.SalaryInfo;

import java.time.YearMonth;
import java.util.List;

public interface AccountantService {
    UpsertSalaryInfoResponse addUserSalaries(List<UpsertSalaryInfoRequest> requests);

    UpsertSalaryInfoResponse editUserSalary(UpsertSalaryInfoRequest request);

    List<SalaryInfo> getSalaries();

    SalaryInfo getSalaryForPeriod(YearMonth period);
}
