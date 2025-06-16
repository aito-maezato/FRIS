package jp.co.sss.shop.controller.client.item;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jp.co.sss.shop.entity.Reviews;
import jp.co.sss.shop.form.ReviewsForm;
import jp.co.sss.shop.repository.ReviewsRepository;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ClientItemReviewController {
	
	//口コミテーブルのリポジトリオブジェクトを作成
	@Autowired
	ReviewsRepository repository;
	
	//口コミ登録確認
	@PostMapping("/client/item/detail/Reviews")
	public String aa(ReviewsForm form) {
		//口コミテーブルエンティティのオブジェクトを作成
		Reviews reviews = new Reviews();
		//フォームの内容をオブジェクトに保存
		BeanUtils.copyProperties(form, reviews,"id");
		//オブジェクトの内容をリポジトリを使い保存
		repository.save(reviews);
		//商品詳細画面に戻る
		return "redirect:/client/item/detail/"+form.getItemId();
	}

	/*
	//口コミ登録戻る
	@PostMapping("/client/item/detail/{id}/")
	public String bb() {
		//商品詳細画面に戻る
		return "redirect:/";
	}
	*/
	
	//口コミ削除確認ボタン
	@GetMapping("/client/item/detail/Reviews/delete_check_flag")
	public String cc(HttpServletRequest request) {
		//口コミidを表示
		//System.err.println(request.getParameter("hidden-review-id"));
		//口コミを削除
		//repository.deleteById(Integer.parseInt(request.getParameter("hidden-review-id")));
		
		//口コミを論理削除
		Reviews review = repository.getReferenceById(Integer.parseInt(request.getParameter("hidden-review-id")));
		review.setDeleteFlag(1);
		repository.save(review);
		//商品詳細画面に戻る
		return "redirect:/client/item/detail/" + request.getParameter("hidden-item-id");
	}
	
	/*
	//口コミ削除戻るボタン
	@PostMapping("/client/item/detail/{id}/")
	public String dd() {
		//商品詳細画面に戻る
		return "redirect:/";
	}
	*/
}