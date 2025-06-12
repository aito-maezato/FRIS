package jp.co.sss.shop.controller.client.item.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ClientOrderRegistController {
    
	
	//支払い方法選択画面 表示処理
	@GetMapping("/payment/input")
	    public String showPaymentInput(@ModelAttribute("orderForm") OrderForm orderForm, Model model) {
	//注文分のお手続きボタン
	@RequestMapping(path = "/client/order/address/input", method = RequestMethod.POST)
    public String OrderProceduRebutton(){
    	
		return "redirect:/client/order/address/input";
    }
	//次へボタンの押下//
	@RequestMapping (path = "client/order/payment/input", method = RequestMethod.POST)
	public String Next() {
		
		return  "redirect: /client/order/check";
	}
	//戻るボタン押下
	@RequestMapping(path = "/client/order/payment/back",method = RequestMethod.POST)
	public String StrungBack() {
		
	return "/client/order/address/input";
	}
	
}
