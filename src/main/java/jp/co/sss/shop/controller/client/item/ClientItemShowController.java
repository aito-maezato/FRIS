package jp.co.sss.shop.controller.client.item;

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

import jakarta.servlet.http.HttpServletRequest;
import jp.co.sss.shop.entity.Allergy;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.AllergyService;
import jp.co.sss.shop.service.BeanTools;
import jp.co.sss.shop.service.CategoryService;
import jp.co.sss.shop.service.ItemService;
import jp.co.sss.shop.util.Constant;

/**
 * 商品管理 一覧表示機能(一般会員用)のコントローラクラス
 *
 * @author SystemShared
 */
@Controller
public class ClientItemShowController {
	/**
	 * 商品情報
	 */
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	ItemService itemService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	AllergyService allergyService;

	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;

	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model) {

		return "index";
	}

	/**
	商品一覧表示処理
	@param model Viewとのデータ受け渡し
	@param pageable ページング情報（Springが自動設定）
	@return 商品一覧画面テンプレート（client/item/list）
		 */
	@RequestMapping(path = "/client/item/list/1", method = { RequestMethod.GET, RequestMethod.POST })
	public String showItemList(Model model, Pageable pageable) {
		// 商品情報を検索
		Page<Item> itemsPage = itemRepository.findByDeleteFlagOrderByInsertDateDescPage(Constant.NOT_DELETED, pageable);
		// ページ内の商品リストを取得
		List<Item> itemList = itemsPage.getContent();
		// View へ商品一覧とページ情報を渡す
		model.addAttribute("items", itemList);
		model.addAttribute("pages", itemsPage);
		// 商品一覧画面を表示
		return "redirect:/client/item/list/test";
	}

	//売れ筋順処理
	@RequestMapping(path = "/client/item/list/2", method = { RequestMethod.GET, RequestMethod.POST })
	public String showItemListBysell(Model model, Pageable pageable) {
		// 商品情報を検索
		Page<Item> itemsPage = itemRepository.findByDeleteFlagOrderByHotSellDescPage(Constant.NOT_DELETED, pageable);
		// ページ内の商品リストを取得
		List<Item> itemList = itemsPage.getContent();
		// View へ商品一覧とページ情報を渡す
		model.addAttribute("items", itemList);
		model.addAttribute("pages", itemsPage);
		// 商品一覧画面を表示
		return "redirect:/client/item/list/test";
	}

	/**
	 * 商品一覧表示（カテゴリ選択・アレルギー除外対応）
	 *
	 * @param categoryId カテゴリID（任意）
	 * @param allergyIds 除外アレルギーIDのリスト（任意）
	 * @param model Thymeleaf へ渡すモデル
	 * @return 商品一覧画面パス
	 */
	@GetMapping("/client/item/list/test")
	public String list(Model model) {
//		System.out.println(categoryId);
//		System.out.println(allergyIds);
		List<Item> items;
//		if (categoryId != null && allergyIds != null && !allergyIds.isEmpty()) {
//			// カテゴリ指定 & アレルギー除外
//			items = itemService.findItemsByCategoryExcludingAllergies(categoryId, allergyIds);
//			System.out.println("カテゴリ指定 & アレルギー除外");
//		} else if (categoryId != null) {
//			// カテゴリ指定のみ
//			items = itemService.findByCategoryId(categoryId);
//			System.out.println("カテゴリ指定のみ");
//
//		} else {
//			// 全件取得（削除フラグ0のみ）
			items = itemService.findItems(0, null).getContent(); // ページングなし全件
//			System.out.println("全件取得");
//
//		}
//		System.out.println("アレルギー絞り込み完了");
		// カテゴリ・アレルギー一覧を画面に渡す
//		List<Category> categories = categoryService.findAll();
		List<Allergy> allergies = allergyService.findAll();
		model.addAttribute("items", items);
//		model.addAttribute("categories", categories);
		model.addAttribute("allergyList", allergies);
//		model.addAttribute("categoryId", categoryId);
//		model.addAttribute("selectedAllergies", allergyIds);

		return "client/item/list";
	}
	@GetMapping("/client/item/list/test/{allergyIds}")
	public String list(
			@PathVariable(name = "categoryId", required = false) Long categoryId,
			@PathVariable(name = "allergyIds", required = false) List<Long> allergyIds,
			Model model,HttpServletRequest request) {
		System.out.println(categoryId);
		System.out.println(allergyIds);
		List<Item> items;
		if (categoryId != null && allergyIds != null && !allergyIds.isEmpty()) {
			// カテゴリ指定 & アレルギー除外
			items = itemService.findItemsByCategoryExcludingAllergies(categoryId, allergyIds);
			System.out.println("カテゴリ指定 & アレルギー除外");
		} else if (categoryId != null) {
			// カテゴリ指定のみ
			items = itemService.findByCategoryId(categoryId);
			System.out.println("カテゴリ指定のみ");

		} else {
			// 全件取得（削除フラグ0のみ）
			items = itemService.findItems(0, null).getContent(); // ページングなし全件
			System.out.println("全件取得");

		}
		System.out.println("アレルギー絞り込み完了");
		// カテゴリ・アレルギー一覧を画面に渡す
		List<Category> categories = categoryService.findAll();
		List<Allergy> allergies = allergyService.findAll();
		model.addAttribute("items", items);
		model.addAttribute("categories", categories);
		model.addAttribute("allergyList", allergies);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("selectedAllergies", allergyIds);

		return "client/item/list";
	}
}
