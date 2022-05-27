package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/admin/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Validated ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam List<MultipartFile> itemImfFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImfFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "상품이미지는 필수 입력 데이터 잆니다.");
            return "item/itemForm";
        }
        try{
            itemService.saveItem(itemFormDto, itemImfFileList);
        } catch (IOException e) {
            model.addAttribute("errorMessage", "상품등록 도중에 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }
}
