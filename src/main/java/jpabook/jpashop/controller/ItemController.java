package jpabook.jpashop.controller;
;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setIsbn(form.getIsbn());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String List(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(form.getAuthor());
        form.setIsbn(form.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, @ModelAttribute("from") BookForm form) {

        /**
         * 준영속상태 : 이미 DB 에 한번 저장되어서 식별자가 존재한다.
         * 영속성 컨텍스트는 수정되면 자동으로 DB에 저장되는데 준영속상태는 수정도어도 DB에 저장되지 않음.
         * -> 변경 감지 기능 사용, 병합(merge) 사용으로 수정. // 변경 감지 기능 사용 권장
         * [스프링 부트와 JPA 활용1 pdf p.85]
         */

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
