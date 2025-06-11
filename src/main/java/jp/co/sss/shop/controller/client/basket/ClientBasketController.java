package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class ClientBasketController {
	
	final Integer DELETE_OREDER_NUM = 1;
	final Integer NO_OREDER = 0;
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	
	/**
	 * カート一覧表示用
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/client/basket/list")
	public String basketList() {
		
		return "client/basket/list";
	}
	
	/**
	 * カートへの商品追加用
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/client/basket/list/set")
	public String basketSet(HttpSession session) {
		List<BasketBean> basketList = new ArrayList<>();
		BasketBean basketBean1 = new BasketBean(5,"ごぼう",5);
		basketBean1.setOrderNum(2); 
//		BasketBean basketBean2 = new BasketBean(6,"だんご",5);//
//		basketBean2.setOrderNum(2);// 
		
		basketList.add(basketBean1);
//		basketList.add(basketBean2);
		session.setAttribute("basketBeans", basketList);
		
		return "redirect:/client/basket/list";
	}
	
	/**
	 * カート追加実装途中
	 * 
	 * 
	 */
//	@RequestMapping(path = "/client/basket/list/set",method = RequestMethod.POST)
//	public String basketAdd(HttpSession session) {
//		List<BasketBean> basketBeans = new ArrayList<>();
//		basketBeans = (List<BasketBean>)session.getAttribute("basketBeans");
//		if(basketBeans.isEmpty()) {
//			session.setAttribute("basketBeans", basketBeans);
//			
//		}
//		return "redirect:client/basket/list";
//	}
	
	
	/**
	 * カート内商品削除ボタン(注文数1減少、注文数0の場合商品削除)
	 * 実装途中(リダイレクト：カート追加機能実装時に修正)
	 * 
	 * @param session
	 * @param basketBean
	 * @return
	 */
	@RequestMapping(path = "/client/basket/delete", method = RequestMethod.POST)
	public String basketDelete(HttpSession session,BasketBean basketBean) {
		
		
		List<BasketBean> basketBeans = new ArrayList<>();
		
		//商品個数減少
		basketBeans = (List<BasketBean>)session.getAttribute("basketBeans");
		int num = 0;
		for (BasketBean selectBasketBean:basketBeans) {
			if(selectBasketBean.getId().equals(basketBean.getId())) {
				basketBeans.get(num).setOrderNum(basketBeans.get(num).getOrderNum() - DELETE_OREDER_NUM); 
			}
			
			num += 1;
		}
		
		//商品の削除(注文数0のとき)
		Iterator<BasketBean> iterator = basketBeans.iterator();
		while (iterator.hasNext()) {
			BasketBean item = iterator.next();
	 
	        if(item.getOrderNum().equals(NO_OREDER)) {
	            iterator.remove();
	        } 
	    }
		
		//カートを空にする
		if(basketBeans.isEmpty()) {
			session.removeAttribute("basketBeans");
		}
		
		return  "client/basket/list";////カートへの追加メソッド出来次第リダイレクトに変更
		
	}
	
	
	/**
	 * カート内の商品を空にする
	 * @param session
	 * @return　リダイレクト  : “/client/basket/list”
	 */
	@RequestMapping(path = "/client/basket/allDelete",method = RequestMethod.POST)
	public String basketAllDelete(HttpSession session) {
		session.removeAttribute("basketBeans");

		return  "client/basket/list"; //カートへの追加メソッド出来次第リダイレクトに変更
	}
	
	
}
