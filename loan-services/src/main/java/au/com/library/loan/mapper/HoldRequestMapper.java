package au.com.library.loan.mapper;

import au.com.library.loan.dto.HoldRequestResultDTO;
import au.com.library.loan.entity.HoldRequest;
import au.com.library.shared.mapper.SimpleMapper;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting HoldRequest entities to HoldRequestResultDTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface HoldRequestMapper extends SimpleMapper<HoldRequest, HoldRequestResultDTO> {
}
