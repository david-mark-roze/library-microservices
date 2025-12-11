package au.com.library.member.dto;

import au.com.library.member.entity.Member;

public class MemberMapper {

    public static Member toMember(MemberDTO memberDTO){
        return Member.builder().
                id(memberDTO.getId()).
                firstName(memberDTO.getFirstName()).
                lastName(memberDTO.getLastName()).
                city(memberDTO.getCity()).
                address1(memberDTO.getAddress1()).
                address2(memberDTO.getAddress2()).
                state(memberDTO.getState()).
                postcode(memberDTO.getPostcode()).
                phone(memberDTO.getPhone()).email(memberDTO.getEmail()).
                build();
    }

    public static MemberDTO toMemberDTO(Member member){
        return MemberDTO.builder().
            id(member.getId()).
            firstName(member.getFirstName()).
            lastName(member.getLastName()).
            city(member.getCity()).
            address1(member.getAddress1()).
            address2(member.getAddress2()).
            state(member.getState()).
            postcode(member.getPostcode()).
            phone(member.getPhone()).email(member.getEmail()).
            build();
    }
}
