package com.example.jisayboard.entity;

import lombok.*;

import javax.persistence.*;

/*
 * 이 클래스는 게시판의 글들을 저장하는 클래스입니다
 * 저장 컬럼으로는 글넘버, 제목, 내용 정도만 저장합니다
 * 관계는 멤버테이블과 M<many> : 1으로 설정합니다 이건 좀 있다가 보여줄게요
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;
	
	private String title;
	
	private String content;

	private String filePath;

	private String fileName;
	
	//멤버와 관계설정
	//DB 처럼 필드로 하는게 아니라 어노테이션을 이용해서 객체를 선언만 해주면 
	//해당 객체의 key를 찾아서 자동으로 테이블 구성을 합니다. 
	@ManyToOne(fetch = FetchType.LAZY)
	private Member writer;
	
	public void changeTitle(String title) {
		this.title = title;
	}
	
	public void changeContent(String content) {
		this.content = content;
	}
	
	
}
