package au.com.library.loan.mapper;

import au.com.library.loan.dto.LoanDTO;
import au.com.library.loan.entity.Loan;
import au.com.library.shared.mapper.SimpleMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between {@link Loan} entities and {@link LoanDTO} data transfer objects.
 * This interface extends {@link SimpleMapper} to provide basic mapping functionality.
 * The MapStruct framework will generate the implementation of this interface at compile time.
 * The @Mapper annotation indicates that this is a MapStruct mapper and specifies that the generated implementation should be a Spring component.
 */
@Mapper(componentModel = "spring")
public interface LoanMapper extends SimpleMapper<Loan, LoanDTO> {
}
