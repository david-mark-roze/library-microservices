package au.com.library.member.service.impl;

import au.com.library.member.dto.MemberDTO;
import au.com.library.member.entity.Member;
import au.com.library.member.exception.DuplicateEmailAddressException;
import au.com.library.member.mapper.MemberMapper;
import au.com.library.member.repository.MemberRepository;
import au.com.library.member.service.MemberService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ResourceNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

/**
 * The {@link MemberService} implementation.
 *
 * @see MemberRepository
 */

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final MemberMapper mapper;

    public MemberServiceImpl(MemberRepository repository, MemberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public MemberDTO add(MemberDTO memberDTO) throws DuplicateEmailAddressException {
        return (save(mapper.toEntity(memberDTO)));
    }

    @Override
    public MemberDTO find(Long id) throws ResourceNotFoundException {
        return mapper.toDTO(findById(id));
    }

    @Override
    public MemberDTO update(Long id, MemberDTO memberDTO) throws ResourceNotFoundException {
        Member member = findById(id);

        member.setFirstName(memberDTO.firstName());
        member.setLastName(memberDTO.lastName());
        member.setEmail(memberDTO.email());
        member.setPhone(memberDTO.phone());
        member.setAddress1(memberDTO.address1());
        member.setAddress2(memberDTO.address2());
        member.setCity(memberDTO.city());
        member.setState(memberDTO.state());
        member.setPostcode(memberDTO.postcode());
        return save(member);
    }

    private MemberDTO save(Member member){
        try {
            return mapper.toDTO(repository.save(member));
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
