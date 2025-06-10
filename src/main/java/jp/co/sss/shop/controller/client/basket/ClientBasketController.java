package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
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
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	
	/**
	 * カート一覧表示用
	 * 商品カート追加実装時、スコープ初期化は消す 削除部分://
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/client/basket/list")
	public String basketList(HttpSession session) {
		List<BasketBean> basketBeans = new ArrayList<>();//
		BasketBean basketBean1 = new BasketBean(5,"ごぼう",5);//
		basketBean1.setOrderNum(2);// 
		BasketBean basketBean2 = new BasketBean(6,"だんご",5);//
		basketBean2.setOrderNum(2);// 
		basketBeans.add(basketBean1);//
		basketBeans.add(basketBean2);//
		session.setAttribute("basketBeans", basketBeans);//
		return "client/basket/list";
	}
	
	
	/**
	 * カート内商品削除ボタン(注文数1減少、注文数0の場合商品削除)
	 * 実装途中
	 * 
	 * @param session
	 * @param basketBean
	 * @return
	 */
	@RequestMapping(path = "/client/basket/delete", method = RequestMethod.POST)
	public String basketDelete(HttpSession session,BasketBean basketBean) {
		basketBean.setOrderNum((Integer)basketBean.getOrderNum()-DELETE_OREDER_NUM);
//		BasketBean newBasketBean = new BasketBean();
		
		List<BasketBean> basketBeans = new ArrayList<>();
		
		basketBeans = (List<BasketBean>)session.getAttribute("basketBeans");
		int num = 0;
		for (BasketBean selectBasketBean:basketBeans) {
			if(selectBasketBean.getId().equals(basketBean.getId())) {
				basketBeans.set(num,basketBean); 
			}
			num += 1;
		}

		return  "client/basket/list";////カートへの追加メソッド出来次第リダイレクトに変更
	}
	
	
	/**
	 * カートを
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/basket/allDelete")
	public String basketAllDelete(HttpSession session) {
		session.removeAttribute("basketBeans");
		return  "client/basket/list"; //カートへの追加メソッド出来次第リダイレクトに変更
	}
	
	
}
