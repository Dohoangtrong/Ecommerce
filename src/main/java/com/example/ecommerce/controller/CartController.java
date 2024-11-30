package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.repository.CartItemRepository;

import java.util.ArrayList;
import java.util.List;


@Controller
public class CartController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
            List<CartItem> cartItems = cartItemRepository.findByCart(cart);
            model.addAttribute("cartItems", cartItems);

            double totalPrice = cartItems.stream()
                    .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                    .sum();
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
            model.addAttribute("totalPrice", totalPrice);
        } else {

            List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
            model.addAttribute("cartItems", cartItems);

            double totalPrice = cartItems.stream()
                    .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                    .sum();
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
            model.addAttribute("totalPrice", totalPrice);
        }

        return "cart";
    }



    @PostMapping("/cart/add")
    public ResponseEntity<Integer> addToCart(@RequestParam("itemId") Long itemId, @RequestParam("quantity") int quantity, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");
        int cartItemCount = 0;
        if (loggedInUser != null) {
            Item item = itemRepository.findById(itemId).orElse(null);
            if (item != null) {
                Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
                if (cart == null) {
                    cart = new Cart(loggedInUser);
                    cartRepository.save(cart);
                }
                CartItem existingCartItem = cartItemRepository.findByCartAndItem(cart, item);
                if (existingCartItem != null) {
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
                    cartItemRepository.save(existingCartItem);
                } else {
                    CartItem cartItem = new CartItem(cart, item, quantity);
                    cartItemRepository.save(cartItem);
                }
                cartItemCount = cart.getCartItems().size();
            }
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }
            boolean itemExists = false;
            for (CartItem cartItem : cart) {
                if (cartItem.getItem().getId().equals(itemId)) {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                Item item = itemRepository.findById(itemId).orElse(null);
                if (item != null) {
                    CartItem cartItem = new CartItem(null, item, quantity);
                    cart.add(cartItem);
                }
            }
            session.setAttribute("cart", cart);
            cartItemCount = cart.size();
        }

        return ResponseEntity.ok(cartItemCount);
    }

    @PostMapping("/cart/increase")
    public String increaseCart(@RequestParam("itemId") Long itemId, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {

            Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
            Item item = itemRepository.findById(itemId).orElse(null);

            if (cart != null && item != null) {
                CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item);
                if (cartItem != null) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartItemRepository.save(cartItem);
                }
            }
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }

            for (CartItem cartItem : cart) {
                if (cartItem.getItem().getId().equals(itemId)) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/decrease")
    public String decreaseCart(@RequestParam("itemId") Long itemId, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {

            Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
            Item item = itemRepository.findById(itemId).orElse(null);

            if (cart != null && item != null) {
                CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item);
                if (cartItem != null) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    if ( cartItem.getQuantity() == 0 )
                        cartItemRepository.delete(cartItem);
                    else
                        cartItemRepository.save(cartItem);

                }
            }
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }

            for (CartItem cartItem : cart) {
                if (cartItem.getItem().getId().equals(itemId)) {
                    cartItem.setQuantity(cartItem.getQuantity() - 1);
                    break;
                }
            }
            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }



    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("itemId") Long itemId, HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            Cart cart = cartRepository.findByCustomerId(loggedInUser.getId());
            Item item = itemRepository.findById(itemId).orElse(null);
            CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item);
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            }
        } else {
            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart != null) {
                cart.removeIf(cartItem -> cartItem.getItem().getId().equals(itemId));
                session.setAttribute("cart", cart);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        Customer loggedInUser = (Customer) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {
            cartItemRepository.deleteAll();
        } else {
            session.removeAttribute("cart");
        }

        return "redirect:/cart";
    }

}