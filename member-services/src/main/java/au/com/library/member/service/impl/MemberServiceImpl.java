package au.com.library.member.service.impl;

import au.com.library.member.dto.MemberDTO;
import au.com.library.member.dto.MemberMapper;
import au.com.library.member.entity.Member;
import au.com.library.member.exception.DuplicateEmailAddressException;
import au.com.library.member.repository.MemberRepository;
import au.com.library.member.service.MemberService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

/**
 * The {@link MemberService} implementation.
 *
 * @see MemberRepository
 */
@AllArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository repository;

    @Override
    public MemberDTO add(MemberDTO memberDTO) throws DuplicateEmailAddressException {
        return (save(MemberMapper.toMember(memberDTO)));
    }

    @Override
    public MemberDTO find(Long id) throws ResourceNotFoundException {
        return MemberMapper.toMemberDTO(findById(id));
    }

    @Override
    public MemberDTO update(Long id, MemberDTO memberDTO) throws ResourceNotFoundException {
        Member member = findById(id);

        member.setFirstName(memberDTO.getFirstName());
        member.setLastName(memberDTO.getLastName());
        member.setEmail(memberDTO.getEmail());
        member.setPhone(memberDTO.getPhone());
        member.setAddress1(memberDTO.getAddress1());
        member.setAddress2(memberDTO.getAddress2());
        member.setCity(memberDTO.getCity());
        member.setState(memberDTO.getState());
        member.setPostcode(memberDTO.getPostcode());
        return save(member);
    }

    private MemberDTO save(Member member){
        try {
            return MemberMapper.toMemberDTO(repository.save(member));
        } catch (Exception e) {
            if (e.getCause() instanceof ConstraintViolationException){
                // Will be because of a duplicate email
                throw new DuplicateEmailAddressException(
                        String.format("The email address %s is already being used", member.getEmail()));
            }
            throw new BadRequestException(e.getMessage());
        }
    }

    private Member findById(Long id){
        return repository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException(
                        String.format("The member with the id %s could not be found", id))
                );
    }
}
