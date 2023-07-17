package com.example.jisayboard.service;

import com.example.jisayboard.dto.BoardDTO;
import com.example.jisayboard.dto.PageRequestDTO;
import com.example.jisayboard.dto.PageResultDTO;
import com.example.jisayboard.entity.Board;
import com.example.jisayboard.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
public interface BoardService {

    Page<Board> getList2(Pageable pageable);

    PageResultDTO<BoardDTO, Object[]> boardSearch(String search,PageRequestDTO pageRequestDTO);

    default BoardDTO entityToDto(Board board, Member member, Long repleCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .filePath(board.getFilePath())
                .fileName(board.getFileName())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writereEmail(member.getMemberEmail())
                .writereName(member.getMemberName())
                .repleCount(repleCount.intValue())
                .build();
        return boardDTO;

    }

    BoardDTO read(Long bno);

    void modify(BoardDTO boardDTO);

    Long register(BoardDTO boardDTO, MultipartFile[] multipartFile) throws Exception;

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO dto) {
        Member member = Member.builder().memberEmail(dto.getWritereEmail()).build();
        //여기까지가 dto에서 사용자가 입력한 email설정 작업와료
        //위 멤버객체를 Board에 writer로 참조(ref)를 걸어야합니다
        Board board = Board.builder().bno(dto.getBno()).title(dto.getTitle()).content(dto.getContent()).writer(member).build();
        return board;
    }
}

