package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
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
    public String itemNew(@Validated @ModelAttribute ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam List<MultipartFile> itemImgFile) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFile.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "상품이미지는 필수 입력 데이터 잆니다.");
            return "item/itemForm";
        }
        try {
            itemService.saveItem(itemFormDto, itemImgFile);
        } catch (IOException e) {
            model.addAttribute("errorMessage", "상품등록 도중에 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }


    @GetMapping("/admin/item/{itemId}")
    public String itemDto(@PathVariable Long itemId, Model model) {

        try {
            ItemFormDto itemFormDto = itemService.getItemDto(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Validated @ModelAttribute ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             @RequestParam List<MultipartFile> itemImgFile, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImgFile.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFile);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    //paging
    @GetMapping({"/admin/items", "/admin/items/{page}"})
    public String itemManage(@ModelAttribute ItemSearchDto itemSearchDto,
                            @PathVariable Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        log.info("items.getTotalPages()={},{}",items.getTotalPages(),items.getContent());
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 3);
        return "item/itemMng";

    }



}
