package com.example.jisayboard.dto;

import com.example.jisayboard.entity.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

@Getter
public class MemberAdapter extends User {

    private Member member;

    public MemberAdapter(Member member) {
        super(member.getMemberEmail(),member.getMemberPassword(), List.of(new SimpleGrantedAuthority(member.getRole())));
        this.member = member;
    }
}
