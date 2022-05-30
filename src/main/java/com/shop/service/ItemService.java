package com.shop.service;


import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repo.ItemImgRepository;
import com.shop.repo.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws IOException {
        //상품등록
        Item item = itemFormDto.createItem();
//        Item item = Item.builder()
//                .itemNm(itemFormDto.getItemNm())
//                .itemDetail(itemFormDto.getItemDetail())
//                .itemSellStatus(itemFormDto.getItemSellStatus())
//                .price(itemFormDto.getPrice())
//                .stockNumber(itemFormDto.getStockNumber())
//                .build();


        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.changeItem(item);
            if (i == 0) {
                itemImg.changRrepimgYn("Y");
            } else {
                itemImg.changRrepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

}
