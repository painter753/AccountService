package account.infrastructure.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity(name = "salaryInfo")
public class SalaryInfoDto {
    @Id
    private String id;
    private String employee;
    private LocalDate period;
    private Long salary;

    public SalaryInfoDto() {

    }

    public SalaryInfoDto(String id, String employee, LocalDate period, Long salary) {
        this.id = id;
        this.employee = employee;
        this.period = period;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
