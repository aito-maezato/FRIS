package jp.co.sss.shop.controller.client.basket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.shop.bean.BasketBean;
import jp.co.sss.shop.bean.ItemBean;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.form.ItemForm;
import jp.co.sss.shop.repository.ItemRepository;

@Controller
public class ClientBasketController {
	
	final Integer DELETE_OREDER_NUM = 1;
	final Integer INCREMENT_OREDER_NUM = 1;
	final Integer NO_OREDER = 0;
	final Integer SOLDOUT = 0;
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;
	
	
	/**
	 * カート一覧表示用 & 在庫チェック
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/client/basket/list")
	public String basketList(Model model,HttpSession session) {
		List<String> itemNameListLessThan = new ArrayList<>();
		List<String> itemNameListZero = new ArrayList<>();
		List<BasketBean> basketList = new ArrayList<>();
		basketList = (List<BasketBean>)session.getAttribute("basketBeans");
		if(basketList != null) {
			
			//在庫チェック
			for(int i = 0; i < basketList.size(); i++) {
				//注文数が在庫数を超過した場合、注文数を在庫数と同数にする。商品名の名前記録
				if(basketList.get(i).getStock() < basketList.get(i).getOrderNum() && ! basketList.get(i).getStock().equals(SOLDOUT)) {
					itemNameListLessThan.add(basketList.get(i).getName());
					basketList.get(i).setOrderNum(basketList.get(i).getStock());
					
				}
				
				//在庫数0の商品名記録
				if(basketList.get(i).getStock().equals(SOLDOUT)){
					itemNameListZero.add(basketList.get(i).getName());
				}
				
			}
			
			//記録した商品名をスコープで保存
			if(! itemNameListLessThan.isEmpty()) {
				model.addAttribute("itemNameListLessThan",itemNameListLessThan);
			}else if (! itemNameListZero.isEmpty()){
				model.addAttribute("itemNameListZero",itemNameListZero);
			}
			
			
			//在庫0の商品をカートから削除
			Iterator<BasketBean> iterator = basketList.iterator();
			while (iterator.hasNext()) {
				BasketBean item = iterator.next();
		 
		        if(item.getStock().equals(SOLDOUT)) {
		            iterator.remove();
		          
		        } 
		    }
			
			//商品削除でカートが空ならスコープ削除
			if(basketList.isEmpty()) {
				session.removeAttribute("basketBeans");
			}
			
		}
		
		return "client/basket/list";
	}
	
	/**
	 * testメソッド
	 * カートへの商品追加用
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/client/basket/list/set")
	public String basketSet(HttpSession session) {
		List<BasketBean> basketList = new ArrayList<>();
		basketList = (List<BasketBean>)session.getAttribute("basketBeans");
		
		//セッションスコープが空のとき新規作成＆商品追加
		if(basketList== null) {
			List<BasketBean> newBasketList = new ArrayList<>();
			BasketBean basketBean1 = new BasketBean(5,"ごぼう",0);
			basketBean1.setOrderNum(50); 
			newBasketList.add(basketBean1);
			
			session.setAttribute("basketBeans", newBasketList);
		}
		return "redirect:/client/basket/list";
	}
	
	/**
	 * カートへ追加
	 * 
	 * @param session
	 * @param itemForm
	 * @return リダイレクト:/client/basket/list
	 */
	@RequestMapping(path = "/client/basket/add",method = RequestMethod.POST)
	public String basketAdd(HttpSession session, ItemForm itemForm) {
		//商品情報取得
		itemForm.getId();
		Item item = itemRepository.getReferenceById(itemForm.getId());
		ItemBean itemBean = new ItemBean();
		BeanUtils.copyProperties(item,itemBean); 
		
		itemBean.getId();
		BasketBean basketBean = new BasketBean();
		basketBean.setId(itemBean.getId());
		basketBean.setName(itemBean.getName());
		basketBean.setStock(itemBean.getStock());
		
		List<BasketBean> basketBeans = new ArrayList<>();
		List<BasketBean> newBasketList = new ArrayList<>();
		
		//セッションスコープが空のとき新規作成＆商品追加
		basketBeans = (List<BasketBean>)session.getAttribute("basketBeans");
		if(basketBeans == null) {
			newBasketList.add(basketBean);
			
			session.setAttribute("basketBeans", newBasketList);
			
		
		}else {
			int count = 0;
			int num = 0;
			
			//カート内の商品と同じ場合注文数を増加
			for(BasketBean selectbasketBean:basketBeans) {
				if(selectbasketBean.getId().equals(itemForm.getId() ) ) {
					count += 1;
					basketBeans.get(num).setOrderNum(basketBeans.get(num).getOrderNum() + INCREMENT_OREDER_NUM);  
				}
				num += 1;
			}
			//同名商品がない場合スコープに要素追加
			if(count <= 0) {
				basketBeans.add(basketBean);
			}
		
		}
		
		return "redirect:/client/basket/list";
	}
	
	
	/**
	 * カート内商品削除ボタン(注文数1減少、注文数0の場合商品削除)
	 * 
	 * 
	 * @param session
	 * @param basketBean
	 * @return
	 */
	@RequestMapping(path = "/client/basket/delete", method = RequestMethod.POST)
	public String basketDelete(HttpSession session,BasketBean basketBean) {
		
		
		List<BasketBean> basketBeans = new ArrayList<>();
		
		//商品個数減少
		basketBeans = (List<BasketBean>)session.getAttribute("basketBeans");
		int num = 0;
		for (BasketBean selectBasketBean:basketBeans) {
			if(selectBasketBean.getId().equals(basketBean.getId())) {
				basketBeans.get(num).setOrderNum(basketBeans.get(num).getOrderNum() - DELETE_OREDER_NUM); 
			}
			
			num += 1;
		}
		
		//商品の削除(注文数0のとき)
		Iterator<BasketBean> iterator = basketBeans.iterator();
		while (iterator.hasNext()) {
			BasketBean item = iterator.next();
	 
	        if(item.getOrderNum().equals(NO_OREDER)) {
	            iterator.remove();
	        } 
	    }
		
		//カートを空にする
		if(basketBeans.isEmpty()) {
			session.removeAttribute("basketBeans");
		}
		
		return  "redirect:/client/basket/list";
		
	}
	
	
	/**
	 * カート内の商品を空にする
	 * @param session
	 * @return　リダイレクト  : “/client/basket/list”
	 */
	@RequestMapping(path = "/client/basket/allDelete",method = RequestMethod.POST)
	public String basketAllDelete(HttpSession session) {
		session.removeAttribute("basketBeans");

		return  "redirect:/client/basket/list";
	}
	
	
}
