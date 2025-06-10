package jp.co.sss.shop.controller.client.basket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class ClientBasketController {
	
	
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	@RequestMapping(path = "/client/basket/list")
	public String addBasket(HttpSession session) {
		BasketBean basketBean = new BasketBean(5,"ごぼう",5);
		session.setAttribute("basketBeans", basketBean);
		return "client/basket/list";
	}
	
	@RequestMapping(path = "/client/basket/delete", method = RequestMethod.POST)
	public String itemList(BasketBean basketBean) {
		
		return  "redirect:/client/basket/list";
	}
	
	
}
