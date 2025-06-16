package jp.co.sss.shop.controller.client.item;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Good;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.GoodForm;
import jp.co.sss.shop.repository.GoodRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientItemGoodController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	/**
	 * いいね
	 */
	@Autowired
	GoodRepository goodRepository;
	

	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;
	
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;
	
	@RequestMapping(path = "/client/item/detail/{id}" , method = { RequestMethod.GET, RequestMethod.POST })
	public String showItem(@PathVariable int id, Model model) {
		
		// 対象の商品情報を取得
		Item item = itemRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		
		if (item == null) {
			// 対象が無い場合、エラー
			return "redirect:/syserror";
		}

		//Itemエンティティの各フィールドの値をItemBeanにコピー
		ItemBean itemBean = beanTools.copyEntityToItemBean(item);

		// 商品情報をViewへ渡す
		model.addAttribute("item", itemBean);
		//商品登録・変更・削除用のセッションスコープを初期化
		session.removeAttribute("itemForm");
		
		return "client/item/detail";
	}
	
	// いいね押されたとき
	@RequestMapping(path = "/client/item/detail/like/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String Good(@PathVariable Integer id, Model model, Good good, HttpServletRequest request, GoodForm goodForm) {
		//servletrequest:htmlでhiddenで送ったやつを持ってくる
		
		// 対象の商品情報を取得
		Item item = itemRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
		//Itemエンティティの各フィールドの値をItemBeanにコピー
		ItemBean itemBean = beanTools.copyEntityToItemBean(item);
		// 商品情報をViewへ渡す
		model.addAttribute("item", itemBean);
		System.out.println("いいね押した" );

//		if(good.getGoodFlg() == false) {
//			good.setGoodFlg(true);
//			System.out.println("いいね押した" + good.getGoodFlg());
//			System.out.println("ユーザーidは" + request.getParameter("user-id"));
//			System.out.println("アイテムidは" + request.getParameter("item-id"));
//			System.out.println("アイテムidは" + id);
//		}else {
//			good.setGoodFlg(false);
//			System.out.println("いいね取り消した" + good.getGoodFlg());
//		}
//		good.setGoodFlg(good.getGoodFlg());
		
		System.out.println(good.getItemId());
		System.out.println(good.getUserId());
		
		if(goodRepository.existsById(id)) {
			// テーブルに存在する場合、デリートする
			goodRepository.deleteById(id);
			System.out.println("いいね取り消した！！！");
		}else {
			Good goods = new Good();
			// goodsにコピー
			BeanUtils.copyProperties(goodForm, goods);
			
			goods = goodRepository.save(goods);
			ItemBean itemBean1 = new ItemBean();
			BeanUtils.copyProperties(goods, itemBean1);
			model.addAttribute("good",itemBean1);
			
			System.out.println("いいねしました！！！！！！！！");
			
			good.setItemId(request.getParameter("userId"));
			good.setUserId(request.getParameter("itemId"));
			
		}
		
		
		return "redirect:/client/item/detail/{id}";
	}
}
