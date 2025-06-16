package jp.co.sss.shop.controller.client.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.OrderBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.form.UserForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.service.PriceCalc;

/**
 * 注文管理 一覧表示機能(一般会員者用)のコントローラクラス
 *
 * @author SystemShared
 * 
 */
@Controller
public class ClientOrderShowController {

	/**
	 * 注文情報
	 */
	@Autowired
	OrderRepository orderRepository;
	/**
	 * 商品在庫情報
	 */
	@Autowired
	ItemRepository itemRepository;
	/**
	 * セッション
	 */
	@Autowired
	HttpSession session;

	/**
	 * 合計金額計算サービス
	 */
	@Autowired
	PriceCalc priceCalc;

	/**
	 * Entity、Form、Bean間のデータ生成、コピーサービス
	 */
	@Autowired
	BeanTools beanTools;

	/**
	 * 一覧取得、一覧画面表示　処理
	 *
	 * @param model Viewとの値受渡し
	 * @param pageable ページング情報
	 * @return "client/order/list" 注文情報 一覧画面へ
	 */
	@RequestMapping(path = "/client/order/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String showOrderList(Model model, Pageable pageable, UserForm form) {

		// すべての注文情報を取得(注文日降順)
		//表示画面でページングが必要なため、ページ情報付きの検索を行う
		Page<Order> orderList = orderRepository.findAllOrderByInsertdateDescIdDesc(pageable);

		// 注文情報リストを生成
		List<OrderBean> orderBeanList = new ArrayList<OrderBean>();
		for (Order order : orderList) {
			// BeanToolsクラスのcopyEntityToOrderBeanメソッドを使用して表示する注文情報を生成
			OrderBean orderBean = beanTools.copyEntityToOrderBean(order);
			//orderレコードから紐づくOrderItemのListを取り出す
			List<OrderItem> orderItemList = order.getOrderItemsList();
			//PriceCalcクラスのorderItemPriceTotalメソッドを使用して合計金額を算出
			int total = priceCalc.orderItemPriceTotal(orderItemList);

			//合計金額のセット
			orderBean.setTotal(total);

			orderBeanList.add(orderBean);
		}

		// 注文情報リストをViewへ渡す
		model.addAttribute("pages", orderList);
		model.addAttribute("orders", orderBeanList);

		return "client/order/list";

	}

	/**
	 * 詳細表示処理
	 *
	 * @param id 詳細表示対象ID
	 * @param model Viewとの値受渡し
	 * @return "client/order/detail" 詳細画面　表示
	 */
	@RequestMapping(path = "/client/order/detail/{id}")
	public String showOrder(@PathVariable int id, Model model) {

		// 選択された注文情報に該当する情報を取得
		Order order = orderRepository.getReferenceById(id);

		// 表示する注文情報を生成
		OrderBean orderBean = beanTools.copyEntityToOrderBean(order);

		// 注文商品情報を取得
		List<OrderItemBean> orderItemBeanList = beanTools.generateOrderItemBeanList(order.getOrderItemsList());

		// 合計金額を算出
		int total = priceCalc.orderItemBeanPriceTotalUseSubtotal(orderItemBeanList);

		// 注文情報をViewへ渡す
		model.addAttribute("order", orderBean);
		model.addAttribute("orderItemBeans", orderItemBeanList);
		model.addAttribute("total", total);

		return "client/order/detail";
	}

	//注文確認画面表示処理
	@GetMapping("/client/order/check")
	public String aa(@PathVariable int id,Model model) {
		Integer SOLDOUT = 0;

			// 選択された注文情報に該当する情報を取得
			Order order = orderRepository.getReferenceById(id);
			
		//・セッションスコープから注文情報を取得
		session.getAttribute("");
		//・セッションスコープから買い物かご情報を取得
		List<BasketBean> basketList = new ArrayList<>();
		basketList = (List<BasketBean>) session.getAttribute("basketBeans");
		
		Iterator<BasketBean> iterator = basketList.iterator();
		
		//・注文商品の最新情報をDBから取得し、の在庫チェックをする

		//・在庫不足、在庫切れ商品がある場合
		String aa = "a";
		if (aa == "aa") {
			//- 注文警告メッセージをリクエストスコープに保存
			
			while (iterator.hasNext()) {
				BasketBean item = iterator.next();
				
				//- 在庫数にあわせて、買い物かご情報を更新（注文数、在庫数)
				if (item.getId().equals(aa)) {
					
				}
				//- 在庫切れの商品は、買い物かごか情報ら削除
				if (item.getStock().equals(SOLDOUT)) {
					iterator.remove();
				}
			}
		}
		//・在庫状況を反映した買い物かご情報をセッションに保存

		//・買い物かご情報から、商品ごとの金額小計を算出し、注文商品情報リストに保存
		while (iterator.hasNext()) {
			BasketBean item = iterator.next();
			item.getOrderNum();
		}
		//・注文商品情報リストから合計金額を算出する
		// 注文商品情報を取得
		List<OrderItemBean> orderItemBeanList = beanTools.generateOrderItemBeanList(order.getOrderItemsList());
		// 合計金額を算出
		int total = priceCalc.orderItemBeanPriceTotalUseSubtotal(orderItemBeanList);

		//・合計金額をリクエストスコープに設定
		model.addAttribute("total", total);

		//・注文商品情報リストをリクエストスコープに設定
		model.addAttribute("orderItemBeans", orderItemBeanList);

		//・注文入力フォーム情報をリクエストスコープに設定
		OrderBean orderBean = beanTools.copyEntityToOrderBean(order);
		model.addAttribute("order", orderBean);
		//・注文確認画面表示
		return "forward:/";
	}
}
