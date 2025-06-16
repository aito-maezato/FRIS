package jp.co.sss.shop.controller.client.order;

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
import jp.co.sss.shop.bean.UserBean;
import jp.co.sss.shop.entity.User;
import jp.co.sss.shop.form.OrderForm;
import jp.co.sss.shop.repository.UserRepository;
import jp.co.sss.shop.util.Constant;

@Controller
public class ClientOrderRegistController {
	
	@Autowired
	UserRepository userRepository;
	
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
	 * 動作テスト用(処理１テスト用)
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
		System.out.println(orderForm.getPostalCode());
		 System.out.println(orderForm.getName());
		 System.out.println(orderForm.getPhoneNumber());
		model.addAttribute("payMethod",orderForm.getPayMethod());
		return "client/order/payment_input";
	}
	
	
	//処理５
	@RequestMapping(path = "/client/order/check",method = RequestMethod.POST)
	public String SetCheckOrder(HttpSession session) {
		return "redirect:/client/order/check";
	}
	//処理7
	
}
