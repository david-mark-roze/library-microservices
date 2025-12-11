package au.com.library.member.repository;

import au.com.library.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@link JpaRepository} implementation for {@link Member} data access operations.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
