package com.example.jisayboard.dto;

import com.example.jisayboard.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MemberDTO {

	private String memberId;

	private String memberPassword;
	private String memberName;
	private String memberEmail;
	private String memberPhoneNumber;
	private String memberRegDate;
	private String memberAddress;
	private String memberDetailAddress;
	private String role;

	public static MemberDTO MemberDTOtoEntity(Member member) {
		return MemberDTO.builder()
				.memberId(member.getMemberId())
				.memberPassword(member.getMemberPassword())
				.memberName(member.getMemberName())
				.memberEmail(member.getMemberEmail())
				.memberPhoneNumber(member.getMemberPhoneNumber())
				.memberAddress(member.getMemberAddress())
				.memberDetailAddress(member.getMemberDetailAddress())
				.build();
						
	}
}
