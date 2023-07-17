package com.example.jisayboard.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepleDTO {

    private Long pno;

    private String text;

    private String repler;

    private Long bno;  //게시글 번호

    private LocalDateTime regDate, modDate;
}
