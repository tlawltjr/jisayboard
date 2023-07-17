package com.example.jisayboard.repository;

import com.example.jisayboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SNSMemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByMemberEmail(String email);
}
