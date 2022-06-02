package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDataType;

    private ItemSellStatus itemSellStatus;

    private String searchBy;

    private String searchQuery = "";

}
