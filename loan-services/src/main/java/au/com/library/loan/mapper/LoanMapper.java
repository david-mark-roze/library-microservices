package au.com.library.loan.mapper;

import au.com.library.loan.dto.LoanDTO;
import au.com.library.loan.entity.Loan;
import au.com.library.shared.mapper.SimpleMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper extends SimpleMapper<Loan, LoanDTO> {
}
