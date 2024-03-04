package account.domain.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Salary record already exists!")
public class SalaryRecordAlreadyExistsException extends RuntimeException { }
