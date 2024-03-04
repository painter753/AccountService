package account.application;

import account.application.dto.salary.UpsertSalaryInfoRequest;
import account.application.dto.salary.UpsertSalaryInfoResponse;
import account.domain.exceptions.SalaryRecordAlreadyExistsException;
import account.domain.exceptions.UnknownUserException;
import account.infrastructure.db.SalaryInfoDto;
import account.infrastructure.db.SalaryStorage;
import account.infrastructure.db.UserIdentityDto;
import account.infrastructure.db.UserIdentityStorage;
import account.domain.SalaryInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class AccountantServiceImpl implements AccountantService {

    private final UserIdentityStorage userIdentityStorage;
    private final CurrentUserProvider currentUserProvider;
    private final SalaryStorage salaryStorage;

    public AccountantServiceImpl(UserIdentityStorage userIdentityStorage, CurrentUserProvider currentUserProvider, SalaryStorage salaryStorage) {
        this.userIdentityStorage = userIdentityStorage;
        this.currentUserProvider = currentUserProvider;
        this.salaryStorage = salaryStorage;
    }

    @Override
    public UpsertSalaryInfoResponse addUserSalaries(List<UpsertSalaryInfoRequest> requests) {
        List<SalaryInfoDto> salaryInfoDtos = requests.stream().map(this::mapToEntity).toList();

        checkUserExistence(salaryInfoDtos.stream().map(SalaryInfoDto::getEmployee).map(String::toLowerCase).toList());
        checkSalaryRecordsExistence(salaryInfoDtos.stream().map(SalaryInfoDto::getId).toList());

        salaryStorage.saveAll(salaryInfoDtos);

        return new UpsertSalaryInfoResponse("Added successfully!");
    }

    @Override
    public UpsertSalaryInfoResponse editUserSalary(UpsertSalaryInfoRequest request) {
        checkUserExistence(List.of(request.employee().toLowerCase()));

        SalaryInfoDto salaryInfoDto = mapToEntity(request);
        salaryStorage.save(salaryInfoDto);

        return new UpsertSalaryInfoResponse("Updated successfully!");

    }

    private void checkUserExistence(List<String> emails) {
        boolean isUserNotFound = emails.stream()
                .map(userIdentityStorage::existsUserIdentityByEmailIgnoreCase)
                .anyMatch(it -> !it);

        if (isUserNotFound) {
            throw new UnknownUserException();
        }
    }

    private void checkSalaryRecordsExistence(List<String> recordIds) {
        if (recordIds.stream().distinct().count() != recordIds.size()) {
            throw new SalaryRecordAlreadyExistsException();
        }

        boolean isAnyRecordExists = recordIds.stream()
                .anyMatch(salaryStorage::existsById);

        if (isAnyRecordExists) {
            throw new SalaryRecordAlreadyExistsException();
        }
    }

    private SalaryInfoDto mapToEntity(UpsertSalaryInfoRequest request) {
        return new SalaryInfoDto(
                UUID.nameUUIDFromBytes((request.employee().toLowerCase() + "|" + request.period().toString()).getBytes()).toString(),
                request.employee().toLowerCase(),
                request.period().atDay(1),
                request.salary()
        );
    }

    @Override
    public SalaryInfo getSalaryForPeriod(YearMonth period) {
        UserDetails currentUser = currentUserProvider.getCurrentUser();
        UserIdentityDto user = userIdentityStorage.findUserIdentityByEmailIgnoreCase(currentUser.getUsername()).orElseThrow(
                () -> new RuntimeException("Error while getting user")
        );

        SalaryInfoDto salaryInfo = salaryStorage.findByEmployeeIgnoreCaseAndPeriodOrderByPeriodAsc(currentUser.getUsername(), convertPeriodToLocalDate(period));
        return new SalaryInfo(
                user.getName(),
                user.getLastname(),
                convertPeriodToLocalDate(period),
                salaryInfo.getSalary()
        );
    }

    @Override
    public List<SalaryInfo> getSalaries() {
        UserDetails currentUser = currentUserProvider.getCurrentUser();
        UserIdentityDto user = userIdentityStorage.findUserIdentityByEmailIgnoreCase(currentUser.getUsername()).orElseThrow(
                () -> new RuntimeException("Error while getting user")
        );

        return salaryStorage.findSalaryInfoByEmployeeIgnoreCase(currentUser.getUsername()).stream()
                .map(salaryInfoDto ->
                        new SalaryInfo(
                                user.getName(),
                                user.getLastname(),
                                salaryInfoDto.getPeriod(),
                                salaryInfoDto.getSalary()
                        )

                )
                .sorted((p1, p2) -> -1 * p1.period().compareTo(p2.period()))
                .toList();

//        List<SalaryInfoDto> salaryInfoDtos = salaryStorage.findSalaryInfoByEmployeeIgnoreCase(currentUser.getUsername());
//        return salaryInfoDtos.stream()
//                .sorted((p1, p2) -> -1 * p1.getPeriod().compareTo(p2.getPeriod()))
//                .toList();
    }

    private LocalDate convertPeriodToLocalDate(YearMonth period) {
        return period.atDay(1);
    }


}
