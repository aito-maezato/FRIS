package jp.co.sss.shop.controller.client.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.ReviewsRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientItemGoodController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	//口コミ情報
	@Autowired
	ReviewsRepository reviewsRepository;
	
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
		
		//商品の口コミをリクエストスコープに保存
		model.addAttribute("reviews",reviewsRepository.findByItemId(item.getId()));
		
		return "client/item/detail";
	}
	
	
	
}
