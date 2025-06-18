package jp.co.sss.shop.controller.client.item;

import java.util.ArrayList;
import java.util.List;

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
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Good;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.GoodForm;
import jp.co.sss.shop.repository.GoodRepository;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.UserRepository;
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
	
	@Autowired
	UserRepository userRepository;
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

	@RequestMapping(path = "/client/item/detail/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String showItem(@PathVariable Integer id, Model model) {

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
		// stringに変換
		
		// ItemIdに一致する行を探して保存する
//		model.addAttribute("good",goodRepository.findByItemId(ItemId));
		model.addAttribute("good",goodRepository.findByItem(item));
		
		return "client/item/detail";
	}

	// いいね押されたとき
	@SuppressWarnings("null")
	@RequestMapping(path = "/client/item/detail/like/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public String Good(@PathVariable Integer id, Model model, Good good, HttpServletRequest request,
			GoodForm goodForm) {
//		//servletrequest:htmlでhiddenで送ったやつを持ってくる
//
//		// 対象の商品情報を取得
//		Item item = itemRepository.findByIdAndDeleteFlag(id, Constant.NOT_DELETED);
//		//Itemエンティティの各フィールドの値をItemBeanにコピー
//		ItemBean itemBean = beanTools.copyEntityToItemBean(item);
//		// 商品情報をViewへ渡す
//		model.addAttribute("item", itemBean);
//		System.out.println("いいね押した");
//
//		Good goods = new Good();
//		// goodsにコピー
//		BeanUtils.copyProperties(goodForm, goods);
//		//		ItemBean itemBean1 = new ItemBean();
//		//		BeanUtils.copyProperties(goods, itemBean1);
//		//		model.addAttribute("good",itemBean1);
//
//		if (goodRepository.existsById(id) == false) {
//			// テーブルに存在しない場合
//			System.out.println("テーブルにないね");
//			goods.setDeleteFlg(Constant.NOT_DELETED);
//			goods = goodRepository.save(goods);
//			System.out.println("deleteフラグの中身:" + goods.getDeleteFlg());
//
//		}
//
////		System.out.println("中身:" + goods.getDeleteFlg());
//
//		if (goods.getDeleteFlg() == 0) {
//			goods.setDeleteFlg(Constant.NOT_DELETED);
//			goods = goodRepository.save(goods);
//
//			//model.addAllAttributes("aaa", findByUserIdAndItemId());
//			System.out.println("いいねしたよ");
//		} else {
//			goods.setDeleteFlg(Constant.DELETED);
//			goods = goodRepository.save(goods);
//			System.out.println("いいね取り消した！！！！");
//		}
//
//		
		String uId = request.getParameter("userId");
		int uIdInt = Integer.parseInt(uId);
		String iId = request.getParameter("itemId");
		int iIdInt = Integer.parseInt(iId);
		//デリートフラグを更新する行を選ぶ
//		Good good2 = new Good();
		Good good2 = goodRepository.findByUserAndItem(
				userRepository.getReferenceById(uIdInt),
				itemRepository.getReferenceById(iIdInt));
		
		
		if (good2 == null) {
			Good InTable = new Good();
			// ユーザーIDをセット
			
			InTable.setUser(userRepository.getReferenceById(uIdInt));
			// アイテムIDをセット
			
			InTable.setItem(itemRepository.getReferenceById(iIdInt));
			
			//InTable.setItemId(request.getParameter("itemId"));
			InTable.setDeleteFlg(0);
			//いいねしたことないならテーブルに保存
			good2 = goodRepository.save(InTable);
			System.out.println("列を追加しました");
		} else {
			//レコードが存在するならフラグの切り替え
			//デリートフラグの更新
			System.out.println("hey");
			if (good2.getDeleteFlg() == 0) {
				good2.setDeleteFlg(1);
			} else {
				good2.setDeleteFlg(0);
			}
			//更新結果を保存
			good2 = goodRepository.save(good2);
			System.out.println("列を更新しました");

		}

		return "redirect:/client/item/detail/{id}";
	}
	
	@RequestMapping(path = "/client/item/list/like", method = { RequestMethod.GET, RequestMethod.POST })
	public String LikeList(Model model, HttpServletRequest request) {
		//Integer uId = String.valueOf(request.getParameter("userId"));
		// 対象の商品情報を取得
//		Item items = itemRepository.findByIdAndDeleteFlag(, Constant.NOT_DELETED);
//		User user = ((User) session.getAttribute("user")).getId();
//		goodRepository.findByUserAndDeleteFlag(, Constant.NOT_DELETED)
		UserBean userBean = (UserBean) session.getAttribute("user");
		List <Good>goodlist = goodRepository.findByUserAndDeleteFlg(
				userRepository.getReferenceById(userBean.getId()), 
				Constant.NOT_DELETED) ;
		
		List <Item>itemlist = new ArrayList<>();
		for(int i = 0; i < goodlist.size(); i++) {
			itemlist.add(goodlist.get(i).getItem());
			
		}
		
		// 商品情報をViewへ渡す
		model.addAttribute("items", itemlist);
		return "client/item/list";
	}
}
