package au.com.library.member.controller;

import au.com.library.member.dto.MemberDTO;
import au.com.library.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A REST API controller class handling REST APIs to library member operations.
 *
 * @see MemberService
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private MemberService service;

    /**
     * Handles the creation of a new library member.
     *
     * @param memberDTO A {@link MemberDTO} object containing details of a new {@link au.com.library.member.entity.Member member}.
     * @return A {@link ResponseEntity} object that references a {@link MemberDTO} object containing the new member's details.
     */
    @PostMapping
    public ResponseEntity<MemberDTO> add(@RequestBody MemberDTO memberDTO){
        return new ResponseEntity<MemberDTO>(service.add(memberDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> find(@PathVariable Long id){
        return ResponseEntity.ok(service.find(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> update(@PathVariable Long id, @RequestBody MemberDTO memberDTO){
        return ResponseEntity.ok(service.update(id, memberDTO));
    }
}
