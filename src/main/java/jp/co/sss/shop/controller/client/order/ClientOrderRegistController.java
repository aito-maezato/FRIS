package jp.co.sss.shop.controller.client.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.bean.OrderItemBean;
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.ItemRepository;
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
	
	/**
	 * 処理１
	 * 
	 * @param orderForm
	 * @param session
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(path = "/client/order/address/input",method = RequestMethod.POST)
	public String setAddressInput(HttpSession session,RedirectAttributes redirectAttributes) {
		//ユーザーセッションからはIDと名前が取れる
		OrderForm checkOrderForm = (OrderForm)session.getAttribute("orderForm");
		System.out.println("\n処理１です");
		if(checkOrderForm == null) {
			OrderForm neworderForm = new OrderForm();
			System.out.println("空です");
			UserBean userBean = (UserBean) session.getAttribute("user");
			User user = userRepository.getReferenceById(userBean.getId());
			BeanUtils.copyProperties(user, neworderForm);
			neworderForm.setPayMethod(Constant.DEFAULT_PAYMENT_METHOD);
			
			redirectAttributes.addFlashAttribute("orderForm", neworderForm);
			session.setAttribute("orderForm", neworderForm);
		
		}else if(checkOrderForm != null) {
			System.out.println("空じゃないよ");
			 System.out.println(checkOrderForm.getPostalCode());
			 System.out.println(checkOrderForm.getName());
			 System.out.println(checkOrderForm.getPhoneNumber());
			redirectAttributes.addFlashAttribute("orderForm", checkOrderForm);
		}

		return "redirect:/client/order/address/input";
	}
	
	/**
	 * 動作テスト用(処理１テスト用)testMethod
	 * 
	 * @param orderForm
	 * @param session
	 * @return
	 */
	//RedirectAttributes redirectAttributes
	@RequestMapping(path = "/client/order/address/input/test",method = {RequestMethod.POST,RequestMethod.GET})
	public String testSetAddressInput(@ModelAttribute OrderForm orderForm,Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		//ユーザーセッションからはIDと名前が取れる
		
		orderForm.setId(1);
		orderForm.setName("ごさまる");
		orderForm.setPhoneNumber("0123456789012");
		orderForm.setPayMethod(Constant.DEFAULT_PAYMENT_METHOD);
		
		orderForm.setPostalCode("123456789");
		redirectAttributes.addFlashAttribute("orderForm", orderForm);
//		model.addAttribute("orderForm",orderForm);
		session.setAttribute("orderForm", orderForm);
		return "redirect:/client/order/address/input";
	}
	
	
	/**
	 * 処理２
	 * 
	 * @param orderForm
	 * @param result
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/client/order/address/input",method = RequestMethod.GET)
	public String inputAddress(@ModelAttribute("orderForm") @Valid OrderForm orderForm, BindingResult result, HttpSession session) {
		orderForm = (OrderForm)session.getAttribute("orderForm");
		 if (result.hasErrors()) {
			 if(result.hasFieldErrors("postalCode")) {
//				 orderForm.setPostalCode("");
//				 System.out.println(orderForm.getPostalCode());
//				 System.out.println(orderForm.getName());
//				 System.out.println(orderForm.getPhoneNumber());
			 }
			 if(result.hasFieldErrors("phoneNumber")) {
//				 orderForm.setPhoneNumber("");
			 }
			 if(result.hasFieldErrors("name")) {
//				 orderForm.setName("");
			 }
			 if(result.hasFieldErrors("address")) {
//				 orderForm.setAddress("");
			 }

		 }
		
		return "client/order/address_input";
	}
	
	//処理3
	@RequestMapping(path = "/client/order/payment/input",method = RequestMethod.POST)
	public String checkAddressInput(@ModelAttribute @Valid OrderForm orderForm,BindingResult result,HttpSession session) {
		//orderForm = (OrderForm)session.getAttribute("orderForm");
		OrderForm sessionOrderForm = (OrderForm)session.getAttribute("orderForm");
		System.out.println("ここは処理３");
		BeanUtils.copyProperties(orderForm, sessionOrderForm);
		
		 System.out.println(sessionOrderForm.getPostalCode());
		 System.out.println(sessionOrderForm.getName());
		 System.out.println(sessionOrderForm.getPhoneNumber());
		if (result.hasErrors()) {
			System.out.println(orderForm.getName());
			
				 System.out.println(orderForm.getPostalCode());
				 System.out.println(orderForm.getName());
				 System.out.println(orderForm.getPhoneNumber());

			
			return "client/order/address_input"; //本来はリダイレクト
		}else {
//		 System.out.println(sessionOrderForm.getPostalCode());
//		 System.out.println(sessionOrderForm.getName());
//		 System.out.println(sessionOrderForm.getPhoneNumber());
		return "redirect:/client/order/payment/input";
		}
	}
	
	//処理４
	@RequestMapping(path = "/client/order/payment/input",method = RequestMethod.GET)
	public String showInputPayment(Model model,HttpSession session) {
		OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
		if(orderForm == null) {
			System.out.println("からだよ４");
		}else if(orderForm != null) {
			System.out.println("からじゃないよ４");
		}
		System.out.println("\nここは処理４");
		System.out.println(orderForm.getId());
		System.out.println(orderForm.getPostalCode());
		 System.out.println(orderForm.getName());
		 System.out.println(orderForm.getPhoneNumber());
		model.addAttribute("payMethod",orderForm.getPayMethod());
		return "client/order/payment_input";
	}
	
	
	//処理５
	@RequestMapping(path = "/client/order/check",method = RequestMethod.POST)
	public String SetCheckOrder(Integer payMethod,HttpSession session) {
		OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
		orderForm.setPayMethod(payMethod);
		return "redirect:/client/order/check";
	}
	
	
	
	//処理6
	//注文確認画面表示処理
	@RequestMapping(path = "/client/order/check",method = RequestMethod.GET)
		public String aa(HttpSession session, Model model) {
			//注文情報の取得
			OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
			
			//・セッションスコープから買い物かご情報を取得
			List<BasketBean> basketList = new ArrayList<>();
			basketList = (List<BasketBean>) session.getAttribute("basketBeans");
			
			//在庫チェック
//			for(int i = 0; i < basketList.size(); i++) {
//				//注文数が在庫数を超過した場合、注文数を在庫数と同数にする。商品名の名前記録
//				if(basketList.get(i).getStock() < basketList.get(i).getOrderNum() && ! basketList.get(i).getStock().equals(SOLDOUT)) {
//					itemNameListLessThan.add(basketList.get(i).getName());
//					basketList.get(i).setOrderNum(basketList.get(i).getStock());
//					
//				}
			
				List<OrderItemBean> orderItemBeans = new ArrayList<>();
				for(int i = 0; i < basketList.size(); i++) {
					OrderItemBean orderItemBean = new OrderItemBean();
					BeanUtils.copyProperties(basketList.get(i), orderItemBean);
					Item item = itemRepository.getReferenceById(basketList.get(i).getId());
					ItemBean itemBean = new ItemBean(); 
					BeanUtils.copyProperties(item,itemBean);
							//basketList.get(i).getId()
				}
//			Order order = orderRepository.getReferenceById(orderForm.getId());
			
			return "client/order/check";
		}
	
	
//	//処理6
//	//注文確認画面表示処理
//	@RequestMapping(path = "/client/order/check",method = RequestMethod.GET)
//		public String aa(HttpSession session, Model model) {
//			OrderForm orderForm = (OrderForm)session.getAttribute("orderForm");
//			// 選択された注文情報に該当する情報を取得
//			Order order = orderRepository.getReferenceById(orderForm.getId());
//				
//			//・セッションスコープから注文情報を取得
//			session.getAttribute("");
//			//・セッションスコープから買い物かご情報を取得
//			List<BasketBean> basketList = new ArrayList<>();
//			basketList = (List<BasketBean>) session.getAttribute("basketBeans");
//			
//			Iterator<BasketBean> iterator = basketList.iterator();
//			
//			//・注文商品の最新情報をDBから取得し、の在庫チェックをする
//
//			//・在庫不足、在庫切れ商品がある場合
//			String aa = "a";
//			if (aa == "aa") {
//				//- 注文警告メッセージをリクエストスコープに保存
//				
//				while (iterator.hasNext()) {
//					BasketBean item = iterator.next();
//					
//					//- 在庫数にあわせて、買い物かご情報を更新（注文数、在庫数)
//					if (item.getId().equals(aa)) {
//						
//					}
//					//- 在庫切れの商品は、買い物かごか情報ら削除
//					if (item.getStock().equals(SOLDOUT)) {
//						iterator.remove();
//					}
//				}
//			}
//			//・在庫状況を反映した買い物かご情報をセッションに保存
//
//			//・買い物かご情報から、商品ごとの金額小計を算出し、注文商品情報リストに保存
//			while (iterator.hasNext()) {
//				BasketBean item = iterator.next();
//				item.getOrderNum();
//			}
//			//・注文商品情報リストから合計金額を算出する
//			// 注文商品情報を取得
//			List<OrderItemBean> orderItemBeanList = beanTools.generateOrderItemBeanList(order.getOrderItemsList());
//			// 合計金額を算出
//			int total = priceCalc.orderItemBeanPriceTotalUseSubtotal(orderItemBeanList);
//
//			//・合計金額をリクエストスコープに設定
//			model.addAttribute("total", total);
//
//			//・注文商品情報リストをリクエストスコープに設定
//			model.addAttribute("orderItemBeans", orderItemBeanList);
//
//			//・注文入力フォーム情報をリクエストスコープに設定
//			OrderBean orderBean = beanTools.copyEntityToOrderBean(order);
//			model.addAttribute("order", orderBean);
//			//・注文確認画面表示
//			return "forward:/";
//		}
//	}
//	
	
	
}
