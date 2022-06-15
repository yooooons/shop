package com.shop.repo;

import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndItemId(Long cartId, Long itemId);
}
