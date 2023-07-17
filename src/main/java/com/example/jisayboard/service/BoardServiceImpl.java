package com.example.jisayboard.service;

import com.example.jisayboard.dto.BoardDTO;
import com.example.jisayboard.dto.PageRequestDTO;
import com.example.jisayboard.dto.PageResultDTO;
import com.example.jisayboard.entity.Board;
import com.example.jisayboard.entity.Member;
import com.example.jisayboard.repository.BoardRepository;
import com.example.jisayboard.repository.MemberRepository;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Function;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberRepository memberRepository;

    String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

    @Override
    public Page<Board> getList2(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }


    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[], BoardDTO> fn = (en -> entityToDto((Board) en[0], (Member) en[1], (Long)en[2]));

        //PageResult 객체의 파라미터로 보낼 Page객체 생성합니다
        Page<Object[]> result = boardRepository.getBoardWithRepleCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        //PageResult 객체 생성 후 리턴시킵니다
        return new PageResultDTO<>(result, fn);
    }
    @Override
    public PageResultDTO<BoardDTO, Object[]> boardSearch(String search,PageRequestDTO pageRequestDTO) {
        Function<Object[], BoardDTO> fn = (en -> entityToDto((Board) en[0], (Member) en[1], (Long)en[2]));
        //PageResult 객체의 파라미터로 보낼 Page객체 생성합니다
        Page<Object[]> result = boardRepository.findByTitleContaining(search,pageRequestDTO.getPageable(Sort.by("bno").descending()));
        PageResultDTO dto = new PageResultDTO<>(result, fn);
        //PageResult 객체 생성 후 리턴시킵니다
        return dto;
    }

    @Override
    public BoardDTO read(Long bno) {
        Object ob = boardRepository.getBoardByBno(bno);
        Object[] arr  = (Object[]) ob;

        return entityToDto((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {

        Board board = boardRepository.getOne(boardDTO.getBno());

        if(board != null) {

            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());

            boardRepository.save(board);
        }
    }

    @Override
    public Long register(BoardDTO dto, MultipartFile[] multipartFile) throws Exception{

        System.out.println(dto);


        for(MultipartFile uploadFile : multipartFile) {
            String orgName = uploadFile.getOriginalFilename();
            String fileName = orgName.substring(orgName.lastIndexOf("\\")+1);


            //이미지만 업로드 하도록 제어합니다.
            //header 정보의 contentType에는 전송되는 데이터의 타입이 있습니다.
            //이를 근거로 업로드 파일의 타입을 조건 분리해서 image 가 아닌 경우엔
            //error를 전송시킵니다

            //업로드될 폴더 경로를 얻어옵니다.
            String folderPath = makeFolder();

            //UUID를 이용한 Unique File Name 생성
            String uuid  = UUID.randomUUID().toString();

            String saveName = projectPath  + folderPath  + uuid + "_" + fileName;

            //위 saveName을 MultipartFile가 수행하는 transterTo()에 맞게
            //File or Path 객체로 변환해줘야 합니다. 아래는 그 작업 수행
            Path savePath = Paths.get(saveName);

            //업로드 처리
            uploadFile.transferTo(savePath);

            //썸네일 이미지 생성
            String thumbnailName = projectPath + folderPath + "s_" + uuid + fileName;

            File thumbFile = new File(thumbnailName);

            //추가된 썸네일 이미지 생성 API응용해서 이미지 생성하기
            Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);

            String changePath = saveName.replace(projectPath + File.separator,"");

            System.out.println("dto 확인---"+dto.getWritereEmail());
            Member member = memberRepository.findByMemberEmail(dto.getWritereEmail());
            System.out.println("repository result ==="+member);

            //여기까지가 dto에서 사용자가 입력한 email설정 작업와료
            //위 멤버객체를 Board에 writer로 참조(ref)를 걸어야합니다
            Board board = Board.builder()
                    .bno(dto.getBno())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .writer(member)
                    .filePath("/files/"+changePath)
                    .fileName(fileName)
                    .build();
            boardRepository.save(board);
            return board.getBno();
        }


        return null;
    }

//    @Override
//    public Long register(BoardDTO dto, MultipartFile[] multipartFile) throws Exception{
//
//        System.out.println("file check=========="+multipartFile);
//        System.out.println("dto확인 -=============="+dto);
//        /*우리의 프로젝트경로를 담아주게 된다 - 저장할 경로를 지정*/
//        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//
//        /*식별자 . 랜덤으로 이름 만들어줌*/
//        UUID uuid = UUID.randomUUID();
//
//        /*랜덤식별자_원래파일이름 = 저장될 파일이름 지정*/
//        String fileName = uuid + "_" + multipartFile.getOriginalFilename();
//
//        /*빈 껍데기 생성*/
//        /*File을 생성할건데, 이름은 "name" 으로할거고, projectPath 라는 경로에 담긴다는 뜻*/
//        File saveFile = new File(projectPath, fileName);
//
//        multipartFile.transferTo(saveFile);
//
//        Member member = Member.builder().email(dto.getWritereEmail()).build();
//        //여기까지가 dto에서 사용자가 입력한 email설정 작업와료
//        //위 멤버객체를 Board에 writer로 참조(ref)를 걸어야합니다
//        Board board = Board.builder()
//                .bno(dto.getBno())
//                .title(dto.getTitle())
//                .content(dto.getContent())
//                .writer(member)
//                .filePath(saveFile.getAbsolutePath())
//                .fileName(fileName)
//                .build();
//        boardRepository.save(board);
//        return board.getBno();
//    }

    private String makeFolder() {

        //저장되는 파일의 경로를 / --> 플랫폼에 종속적인 경로 패스로 변경
        String folderPath = File.separator;

        File uploadPathFolder = new File(projectPath, folderPath);

        System.out.println("업로드된 파일 패스 --> " + projectPath);

        //mkDirs()를 호출해서 지정된 패스 경로에 맞게 동적으로 폴더 생성합니다
        if(!uploadPathFolder.exists()) {
            System.out.println("생성된 폴더 ---> " + uploadPathFolder.mkdirs());
        }
        return folderPath;

    }
}
