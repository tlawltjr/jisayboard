package com.example.jisayboard.controller;

import com.example.jisayboard.dto.BoardDTO;
import com.example.jisayboard.dto.MemberAdapter;
import com.example.jisayboard.dto.PageRequestDTO;
import com.example.jisayboard.dto.PageResultDTO;
import com.example.jisayboard.service.BoardService;
import com.sun.xml.bind.v2.runtime.output.FastInfosetStreamWriterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private BoardService boardService;

    @GetMapping({"/list","","/index.html"})
    public String list(PageRequestDTO pageRequestDTO, Model model) {

        PageResultDTO a = boardService.getList(pageRequestDTO);

        System.out.println("페이지 확인 board ======" + a);

            model.addAttribute("PageResObj", a);
        return "board";
    }

    @GetMapping("/search")
    public String sear(PageRequestDTO pageRequestDTO, Model model,
                       String search) {

        PageResultDTO a = boardService.boardSearch(search, pageRequestDTO);

        System.out.println("페이지 확인 search ======" + a);
        model.addAttribute("search", search);
        model.addAttribute("PageResObj", a);


        return "search";
    }

    @GetMapping({"/read","/modify"})
    public String read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {

        BoardDTO boardDTO = boardService.read(bno);
        model.addAttribute("dto",boardDTO);

        return "read";
    }

    @GetMapping("/register")
    public String register(Authentication authentication){

        if(authentication == null || !authentication.isAuthenticated()){
            return "loginPage";
        }else{
            return "register";
        }

    }

    @PostMapping("/register")
    public String register(BoardDTO boardDTO, RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile[] multipartFile) throws Exception {
        Long bno = boardService.register(boardDTO, multipartFile);
        redirectAttributes.addFlashAttribute("msg", bno);
        return "redirect:list";
    }
    @GetMapping("/loginPage")
    public String login(){
        return "loginPage";
    }

}
