package account.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalaryStorage extends CrudRepository<SalaryInfoDto, String> {
    List<SalaryInfoDto> findSalaryInfoByEmployeeIgnoreCase(String email);
    SalaryInfoDto findByEmployeeIgnoreCaseAndPeriodOrderByPeriodAsc(String employee, LocalDate period);
}
