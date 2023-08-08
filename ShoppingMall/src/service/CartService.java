package service;

import Entity.CartItem;
import dto.CartItemDto;
import dto.CartKey;
import repo.CartRepository;
import java.util.List;

public class CartService {
    CartRepository cartRepository = new CartRepository();

    public List<CartItemDto> selectCart(){
        return cartRepository.selectCart();
    }
    public CartKey createCartKey(Long itemId){
        return CartKey.builder().itemId(itemId).consumerId(1L).build(); // 1L은 고객 번호로 바꿔야함
    }

    public void delete(Long itemId){ // null 이면 하지말자
        cartRepository.delete(createCartKey(itemId));
    }

    public void addCart(Long itemId){
        CartItem cartItem = cartRepository.selectOne(createCartKey(itemId));
        if(cartItem==null){ // insert
            cartRepository.insert(CartItem.builder().itemId(itemId).consumerId(1L).itemQuantity(1L).build()); // 1L은 고객 번호로 바궈야함
        }else{ // update
            cartRepository.updateQuantity(createCartKey(itemId),1);
        }
    }

    public void updateCart(int code, Long itemId){
        if(code==1){
            cartRepository.updateQuantity(createCartKey(itemId),1);
        }
        if(code==2) { // 빼기
            cartRepository.updateQuantity(createCartKey(itemId),-1);
            CartItem cartItem = cartRepository.selectOne(createCartKey(itemId));
            if(cartItem.getItemQuantity()<=0L){ // 0보다 작으면 delete
                cartRepository.delete(createCartKey(itemId));
            }
        }
    }
}