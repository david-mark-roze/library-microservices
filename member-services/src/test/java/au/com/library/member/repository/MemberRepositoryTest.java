package au.com.library.member.repository;

import au.com.library.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Handles testing of the main repository layer functions, many of which are provided out of the box by Spring JPA.
 * This is mainly an intended as an exercise in writing DAO level JUnit tests in a Spring context.
 *
 * @author David Roze
 */
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    private Member member;


    /**
     * Test setup for each test before execution.
     */
    @BeforeEach
    public void setup(){
        member = buildMember(
                "David",
                "Roze",
                "7/42-44 Old Barrenjoey Road",
                "Avalon Beach",
                "NSW",
                "2107",
                "david.mark.roze@gmail.com",
                "0402458134");
    }
    /**
     * Tests that a Member is saved correctly.
     */
    @Test
    @DisplayName(("testCreateMember"))
    public void givenNewMember_whenCreatingMember_thenNewMemberReturned(){
        Member saved = repository.save(member);
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isEqualTo(member.getId());
    }

    /**
     * Tests that an attempt to save a Member with no required values fails.
     */
    @DisplayName("testSaveWithoutRequiredValues")
    @Test
    public void givenEmptyMember_whenCreatingMember_thenCreateFails(){
        Member member = Member.builder().build();
        Assertions.assertThatThrownBy(() -> repository.save(member));
    }

    /**
     * Tests that an update of existing Member occurs correctly.
     */
    @DisplayName("testUpdateMember")
    @Test
    public void givenExistingMember_whenMemberUpdated_thenUpdatedMemberReturned(){
        Member saved = repository.save(member);
        saved.setCity("Avalon");

        Member updated = repository.save(saved);
        Assertions.assertThat(updated.getCity()).isEqualTo(saved.getCity());
    }

    /**
     * Tests that an update of an existing Member fails when a required value (in this case 'city')
     * is null.
     */
    @DisplayName("testUpdateWithoutRequiredValue")
    @Test
    public void givenExistingMemberWithEmptyRequiredValue_whenMemberUpdated_thenUpdateFails(){
        Member saved = repository.save(member);
        saved.setCity(null);
        Assertions.assertThatThrownBy(() -> {
            repository.save(saved);
            // Force commit which should result in an exception
            repository.flush();
        });
    }


    /**
     * Tests that an existing Member is successfully found by its id.
     */
    @DisplayName("testFindById")
    @Test
    public void givenExistingMember_whenFindById_thenMemberFound(){
        Member saved = repository.save(member);
        Member found = repository.findById(saved.getId()).
                orElseThrow(()-> new IllegalStateException("Member not found"));
        Assertions.assertThat(found).isNotNull();
    }

    private Member buildMember(String firstName,
                               String lastName,
                               String address1,
                               String city,
                               String state,
                               String postcode,
                               String email,
                               String phone){
        return Member.builder().
                firstName(firstName).lastName(lastName).
                address1(address1).city(city).
                state(state).postcode(postcode).
                email(email).phone(phone).
                build();
    }
}
