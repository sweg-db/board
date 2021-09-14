package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {
     private final BoardService boardService;

    @GetMapping({"","/","/list"})
    public String boardList(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("result", boardService.getList(pageRequestDTO));

        return "/board/list";
    }

    @GetMapping("/register")
    public void register(){
        log.info("register...");
    }



    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes){
        log.info("registerPost...");
        Long bno = boardService.register(dto);
        redirectAttributes.addFlashAttribute("msg", bno);
        redirectAttributes.addFlashAttribute("noti","등록");
        return "redirect:/board/list";
    }


    @GetMapping({"/read","/modify"})
    public void read(Long bno, Model model,
                     @ModelAttribute("requestDTO") PageRequestDTO requestDTO) {
        BoardDTO boardDTO = boardService.get(bno);
        model.addAttribute("dto", boardDTO);
    }



    @PostMapping("/modify")
    public String modify(BoardDTO dto, RedirectAttributes redirectAttributes,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO){
        log.info("post modify.............................");
        log.info("dto: " + dto);
        boardService.modify(dto);
        redirectAttributes.addAttribute("page", requestDTO.getPage());
        redirectAttributes.addAttribute("bno", dto.getBno());
        redirectAttributes.addAttribute("type", requestDTO.getType());
        redirectAttributes.addAttribute("keyword", requestDTO.getKeyword());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {
        log.info("bno: "+bno);
        boardService.removeWithReplies(bno);
        redirectAttributes.addFlashAttribute("msg",bno);
        redirectAttributes.addFlashAttribute("noti","삭제");

        return "redirect:/board/list";
//      추가
//        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
//        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
//        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
    }
}
