package au.com.library.member.repository;

import au.com.library.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository repository;

    /**
     * Tests that a Member is saved correctly.
     */
    @Test
    @DisplayName(("testCreateMember"))
    public void givenNewMember_whenCreatingMember_thenNewMemberReturned(){
        Member member = buildMember(
                "David",
                "Roze",
                "7/42-44 Old Barrenjoey Road",
                "Avalon Beach",
                "NSW",
                "2107",
                "david.mark.roze@gmail.com",
                "0402458134");
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
    @Test
    public void givenExistingMember_whenMemberUpdated_thenUpdatedMemberReturned(){
        Member member = buildMember(
                "David",
                "Roze",
                "7/42-44 Old Barrenjoey Road",
                "Avalon Beach",
                "NSW",
                "2107",
                "david.mark.roze@gmail.com",
                "0402458134");
        Member saved = repository.save(member);
        saved.setCity("Avalon");

        Member updated = repository.save(saved);
        Assertions.assertThat(updated.getCity()).isEqualTo(saved.getCity());
    }

    @Test
    public void givenExistingMemberWithEmptyRequiredValue_whenMemberUpdated_thenUpdateFails(){
        Member member = buildMember(
                "David",
                "Roze",
                "7/42-44 Old Barrenjoey Road",
                "Avalon Beach",
                "NSW",
                "2107",
                "david.mark.roze@gmail.com",
                "0402458134");
        Member saved = repository.save(member);
        saved.setCity(null);
        Assertions.assertThatThrownBy(() -> {
            repository.save(saved);
            // Force commit which should result in an exception
            repository.flush();
        });
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
