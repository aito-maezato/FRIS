package jp.co.sss.shop.controller.client.item;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientItemGoodController {
	
	@RequestMapping(path = "client/item/detail/{id}/")
	public String showList() {
		return "client/item/detail";
	}

}
