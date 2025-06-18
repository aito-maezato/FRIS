package jp.co.sss.shop.controller.client.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.Order;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.repository.OrderItemRepository;
import jp.co.sss.shop.repository.OrderRepository;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientOrderRegistController {
	
	final Integer SOLDOUT = 0;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	HttpSession session;
	
	/**
	 * 処理１
	 * 注文入力フォーム情報の準備
	 * 
	 * @param orderForm
	 * @param session
	 * @param redirectAttributes
	 * @return リダイレクト：/client/order/address/input"
	 */
	@RequestMapping(path = "/client/order/address/input",method = RequestMethod.POST)
	public String setAddressInput(@ModelAttribute OrderForm orderForm) {
		//ユーザーセッションからはIDと名前を取得
		UserBean userBean = (UserBean) session.getAttribute("user");
		//ユーザー情報を取得
		BeanUtils.copyProperties(userRepository.getReferenceById(userBean.getId()), orderForm);
		//決済方法初期値設定
		orderForm.setPayMethod(Constant.DEFAULT_PAYMENT_METHOD);
		//セッションスコープに注文入力フォーム情報を保存
		session.setAttribute("orderForm", orderForm);

		return "redirect:/client/order/address/input";
	}
	
	/**
	 * 処理２
	 * 注文情報入力画面表示(宛先情報入力画面)
	 * 
	 * @param orderForm
	 * @param result
	 * @param session
	 * @return フォワード "client/order/address_input"
	 */
	@RequestMapping(path = "/client/order/address/input",method = RequestMethod.GET)
	public String inputAddress(@ModelAttribute OrderForm orderform,Model model) {
		//セッションスコープから注文入力フォーム情報を取得 
		OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
		
		//初期値表示用リクエストスコープ(orderForm)
		model.addAttribute("orderForm",orderForm);
		BindingResult result = (BindingResult) session.getAttribute("result");
		if (result != null) {
			//セッションにエラー情報がある場合、エラー情報をリクエストスコープに設定
			model.addAttribute("org.springframework.validation.BindingResult.orderForm", result);
			//セッションからエラー情報を削除
			session.removeAttribute("result");
		}
		
		return "client/order/address_input";
	}
	
	//処理3入力エラー時またはorderFormが空のとき
	/**
	 * OrderForm入力チェック
	 * 
	 * @param orderForm
	 * @param result
	 * @return リダイレクト："/client/order/address/input" エラー入力の場合
	 * 			リダイレクト："/client/order/payment/input" 入力チェックパスした場合
	 */
	@RequestMapping(path = "/client/order/payment/input",method = RequestMethod.POST)
	public String checkAddressInput(@Valid @ModelAttribute OrderForm orderForm,BindingResult result) {

		if (result.hasErrors()) {
			// 入力値にエラーがあった場合、エラー情報をセッションに保持
			session.setAttribute("result", result);

			//登録入力画面　表示処理へリダイレクト
			return "redirect:/client/order/address/input";
		}
		
		// 入力されたオーダー情報をセッションに保持
		session.setAttribute("orderForm",orderForm);
		return "redirect:/client/order/payment/input";
	}
	
	
	/**
	 * 処理４
	 * 決済方法選択画面表示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(path = "/client/order/payment/input",method = RequestMethod.GET)
	public String showInputPayment(Model model) {
		//セッションスコープから注文入力フォーム情報を取得 
		OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
		
		if(orderForm == null) {
			return "redirect:/client/order/address/input";
		}
		//初期入力
		model.addAttribute("payMethod",orderForm.getPayMethod());
		return "client/order/payment_input";
	
	}
	
	
	/**
	 * 処理５
	 * 決済方法をセッションスコープに反映
	 * @param payMethod
	 * @return リダイレクト："/client/order/check"
	 */
	@RequestMapping(path = "/client/order/check",method = RequestMethod.POST)
	public String SetCheckOrder(Integer payMethod) {
		OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
		orderForm.setPayMethod(payMethod);
		return "redirect:/client/order/check";
	}
	
	
	
	/**
	 * 処理６
	 * 在庫チェック
	 * 注文画面表示
	 * 
	 * @param model
	 * @return フォワード："client/order/check"
	 */
	@RequestMapping(path = "/client/order/check",method = RequestMethod.GET)
		public String checkOrder(Model model) {
		
			//在庫不足時の商品名記録よう変数
			List<String> itemNameListZero = new ArrayList<>();
			List<String> itemNameListLessThan = new ArrayList<>();
			//注文情報の取得
			OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
			
			//・セッションスコープから買い物かご情報を取得
			//バスケットの商品をOrderItemBeanに追加 AND 合計の計算
			List<OrderItemBean> orderItemBeans = new ArrayList<>();
			List<BasketBean> basketList = (List<BasketBean>) session.getAttribute("basketBeans");
			
			
			Integer total = 0;
			for(int i = 0; i < basketList.size(); i++) {
				
				//商品情報取得
				Item item = itemRepository.getReferenceById(basketList.get(i).getId());
				ItemBean itemBean = new ItemBean(); 
				BeanUtils.copyProperties(item,itemBean);
				
				//在庫処理
				
				if(itemBean.getStock().equals(SOLDOUT)) {
					//在庫０のとき
					itemNameListZero.add(basketList.get(i).getName());
					basketList.remove(i);
					i -= 1; //配列要素削除時インデックスが1ずれるため
					continue;
				
				}else if(basketList.get(i).getOrderNum() > itemBean.getStock()) {
					//注文数が多いとき
					
					itemNameListLessThan.add(basketList.get(i).getName());
					basketList.get(i).setOrderNum(itemBean.getStock());
					
				}
				
				//basketBeanの要素をorderItemBeanに設定,商品id,name,注文数
				OrderItemBean orderItemBean = new OrderItemBean();
				BeanUtils.copyProperties(basketList.get(i), orderItemBean);
				
				//Itemの要素をorderItemBeanに設定。price,imafge
				orderItemBean.setPrice(itemBean.getPrice());
				orderItemBean.setImage(itemBean.getImage());
				
				//小計の計算と保存
				Integer subtotal = itemBean.getPrice() * basketList.get(i).getOrderNum();
				orderItemBean.setSubtotal(subtotal);
				
				//orderItemBeansへの追加
				orderItemBeans.add(orderItemBean);
				total += subtotal;	
				
			}
			
			
			//各リクエストスコープ保存
			if(! orderItemBeans.isEmpty()) {
			model.addAttribute("orderItemBeans",orderItemBeans);
			model.addAttribute("total",total);
			model.addAttribute("orderForm",orderForm);
			}
			if(! itemNameListZero.isEmpty()) {
				model.addAttribute("itemNameListZero",itemNameListZero);
			}
			if(! itemNameListLessThan.isEmpty()) {
			model.addAttribute("itemNameListLessThan",itemNameListLessThan);
			}
			
			return "client/order/check";
		}
	
	/**
	 * 処理７
	 * 住所入力画面に戻る
	 * 
	 * @return リダイレクト：/client/order/address/input
	 */
	@RequestMapping(path = "/client/order/payment/back",method = RequestMethod.POST)
	public String BackOrderPayment() {
		return "redirect:/client/order/address/input";
	}
	
	
	
	/**
	 * 処理８
	 * 在庫チェック、在庫処理とDBへ注文情報を登録(Order,OrderItem)
	 * 
	 * @return  リダイレクト："/client/order/check" 在庫不足、在庫切れの場合
	 * 			リダイレクト："/client/order/complete" 在庫不足、在庫切れがない場合
	 */
	@RequestMapping(path ="/client/order/complete",method = RequestMethod.POST )//元々GETなぜかエラー
	public String saveOrder() {
		//在庫不足時の商品名記録よう変数
		List<String> itemNameListZero = new ArrayList<>();
		List<String> itemNameListLessThan = new ArrayList<>();
		//注文情報の取得
		OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
		
		//・セッションスコープから買い物かご情報を取得
		List<BasketBean> basketList = new ArrayList<>();
		basketList = (List<BasketBean>) session.getAttribute("basketBeans");
		
		//バスケットの商品をOrderItemBeanに追加 AND 合計の計算
		List<OrderItemBean> orderItemBeans = new ArrayList<>();
		Integer total = 0;
		for(int i = 0; i < basketList.size(); i++) {
			//商品情報取得
			
			Item item = itemRepository.getReferenceById(basketList.get(i).getId());
			ItemBean itemBean = new ItemBean(); 
			BeanUtils.copyProperties(item,itemBean);
			
			//在庫処理
			
			if(itemBean.getStock().equals(SOLDOUT)) {

				return "redirect:/client/order/check";
			
			}else if(basketList.get(i).getOrderNum() > itemBean.getStock()) {

				return "redirect:/client/order/check";
			}
			
			//basketBeanの要素をorderItemBeanに設定,商品id,name,注文数
			OrderItemBean orderItemBean = new OrderItemBean();
			BeanUtils.copyProperties(basketList.get(i), orderItemBean);
			
			//Itemの要素をorderItemBeanに設定。price,imafge
			orderItemBean.setPrice(itemBean.getPrice());
			orderItemBean.setImage(itemBean.getImage());
			
			//小計の計算と保存
			Integer subtotal = itemBean.getPrice() * basketList.get(i).getOrderNum();
			orderItemBean.setSubtotal(subtotal);
			
			//orderItemBeansへの追加
			orderItemBeans.add(orderItemBean);
			total += subtotal;	
		}
		
		//注文テーブルの保存
		Order order = new Order();
		//郵便番号、住所、氏名、電話番号、支払い方法のコピー
		BeanUtils.copyProperties(orderForm,order,"id");
		//会員IDの設定
		UserBean userBean = (UserBean) session.getAttribute("user");
		order.setUser(userRepository.getReferenceById(userBean.getId()));
		//日付の指定
		// 現在時刻を取得
	    Date now = new Date();
	    // java.util.Date から java.sql.Date へ変換
	    java.sql.Date sqlDate = new java.sql.Date(now.getTime());
		order.setInsertDate(sqlDate);
		//レコード保存(注文テーブル)
		order = orderRepository.save(order);
		
		//注文商品テーブル
		List<OrderItem> orderItemsList = new ArrayList<>();
		for(int j = 0; j < orderItemBeans.size(); j++) {
			Item item = itemRepository.getReferenceById(basketList.get(j).getId());
			OrderItem orderItem = new OrderItem();
			//価格の設定
			orderItem.setPrice(orderItemBeans.get(j).getPrice());
			//注文個数の設定
			orderItem.setQuantity(orderItemBeans.get(j).getOrderNum());
			//orderの設定
			orderItem.setOrder(order);
			//itemの設定
			orderItem.setItem(item);
			//注文商品テーブルレコード保存
			orderItem = orderItemRepository.save(orderItem);
			orderItemsList.add(orderItem);
			
			//在庫数を減らす
			item.setStock(item.getStock() - orderItem.getQuantity());
			item = itemRepository.save(item);
			
		}
		//orderItemListフィールドの設定
		order.setOrderItemsList(orderItemsList);
		//orderテーブルレコードの更新。
		order = orderRepository.save(order);
		//カート情報の削除
		session.removeAttribute("basketBeans");
		
		
		return "redirect:/client/order/complete";
	}
	
	/**
	 * 処理９
	 * 注文完了画面表示
	 * @return　フォワード "client/order/complete"
	 */
	@RequestMapping(path = "/client/order/complete", method = RequestMethod.GET )
	public String completeOrder() {
		return  "client/order/complete";
	}
	
}
