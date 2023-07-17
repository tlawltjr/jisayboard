package com.example.jisayboard.repository;

import com.example.jisayboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberRepository extends JpaRepository<Member, String> {

	@Query("select m from Member m where m.memberId =:memberId")
	Member findByMemberId(@Param("memberId") String memberId);

	@Query("select m from Member m where m.memberEmail =:memberEmail")
	Member findByMemberEmail(@Param("memberEmail") String memberEmail);


}
