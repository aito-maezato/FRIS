package jp.co.sss.shop.controller.client.order;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping(path = "/client/order/address/input",method = RequestMethod.POST)
	public String orderAddressInputShow(@ModelAttribute OrderForm orderForm, HttpSession session, RedirectAttributes redirectAttributes) {
		//ユーザーセッションからはIDと名前が取れる
		
		UserBean userBean = (UserBean) session.getAttribute("user");
		
		User user = userRepository.getReferenceById(userBean.getId());
		BeanUtils.copyProperties(user, orderForm);
		orderForm.setPayMethod(Constant.DEFAULT_PAYMENT_METHOD);
		
		redirectAttributes.addFlashAttribute("orderForm", orderForm);
		session.setAttribute("orderForm", orderForm);
		return "redirect:/client/order/address/input";
	}
	
	/**
	 * 動作テスト用
	 * 
	 * @param orderForm
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/client/order/address/input/test",method = {RequestMethod.POST,RequestMethod.GET})
	public String testOrderAddressInputShow(@ModelAttribute OrderForm orderForm, HttpSession session, RedirectAttributes redirectAttributes) {
		//ユーザーセッションからはIDと名前が取れる
		
		orderForm.setId(1);
		orderForm.setName("ごさまる");
		orderForm.setPhoneNumber("0123456789");
		orderForm.setPayMethod(Constant.DEFAULT_PAYMENT_METHOD);
		
		orderForm.setPostalCode("123456789");
		redirectAttributes.addFlashAttribute("orderForm", orderForm);
		session.setAttribute("orderFormSession", orderForm);
		return "redirect:/client/order/address/input";
	}
	
	
	@RequestMapping(path = "/client/order/address/input",method = RequestMethod.GET)
	public String orederAddressInput(@Valid @ModelAttribute("orderForm") OrderForm orderForm, BindingResult result, HttpSession session) {
		
		 if (result.hasErrors()) {
//			 List<ObjectError> errors = result.getAllErrors();
//			 for (ObjectError error : errors) {
//		            System.out.println(error.getDefaultMessage());
//		        }
			 
			 return "client/order/address_input";
		 }
		
		return "client/order/address_input";
	}
	
}
