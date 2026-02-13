package au.com.library.member.mapper;

import au.com.library.member.dto.MemberDTO;
import au.com.library.member.entity.Member;
import au.com.library.shared.mapper.SimpleMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper extends SimpleMapper<Member, MemberDTO> {
}
