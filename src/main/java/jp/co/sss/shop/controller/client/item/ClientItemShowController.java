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
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Allergy;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.OrderItem;
import jp.co.sss.shop.repository.ItemRepository;
<<<<<<< HEAD
import jp.co.sss.shop.repository.ReviewsRepository;
=======
import jp.co.sss.shop.service.AllergyService;
import jp.co.sss.shop.repository.OrderItemRepository;
>>>>>>> 1b6073adb5c002a32edb1913d356eaf19624b339
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
	

    /**
     * Entity、Form、Bean間のデータコピーサービス
     */
    @Autowired
    BeanTools beanTools;

    /**
     * トップ画面 表示処理
     * 
     * @param model Viewとの値受渡し
     * @return "index" トップ画面
     */
    @RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String index(Model model) {
        return "index";
    }

   
    /**
     * カテゴリー検索・一覧表示処理
     * 
     * @param sortType  並び替えタイプ
     * @param categoryId カテゴリID（オプション）
     * @param model Viewとのデータ受け渡し
     * @param pageable ページング情報
     * @return 商品一覧画面テンプレート（client/item/list）
     */
    
    //カテゴリー検索
   /**@RequestMapping(path = "/client/item/list/{sortType}", method = { RequestMethod.GET, RequestMethod.POST })
    public String itemList(@PathVariable("sortType") Integer sortType,
                           @RequestParam(value = "categoryId", required = false) Integer categoryId,
                           Model model, Pageable pageable) {

    	if (categoryId != null && categoryId != 0) {
            // カテゴリー検索処理
            Page<Item> itemsPage = itemRepository.findByCategoryIdAndDeleteFlagOrderByInsertDateDesc(categoryId, Constant.NOT_DELETED, pageable);
            List<Item> itemList = itemsPage.getContent();
            //viewに検索結果を渡す
            model.addAttribute("items", itemList);
            model.addAttribute("pages", itemsPage);

        }
        
        else {
            // 商品一覧表示処理
            Page<Item> itemsPage = itemRepository.findByDeleteFlagOrderByInsertDateDesc(Constant.NOT_DELETED, pageable);
            List<Item> itemList = itemsPage.getContent();
             //viewへ商品一覧とページ情報を渡す
            model.addAttribute("items", itemList);
            model.addAttribute("pages", itemsPage);
        }
        //商品一覧画面を表示
        return "client/item/list";
    }
   */
    @RequestMapping(path = "/client/item/list/{sortType}", method = { RequestMethod.GET, RequestMethod.POST })
    public String itemList(@PathVariable("sortType") Integer sortType,
                           @RequestParam(value = "categoryId", required = false) Integer categoryId,
                           Model model, Pageable pageable) {

        Page<Item> itemsPage;

        
        if (categoryId != null && categoryId != 0) {
            if (sortType == 2) {
                // 売れ筋カテゴリ検索
                itemsPage = itemRepository.findByDeleteFlagAndCategoryIdOrderByHotSellDescPage(Constant.NOT_DELETED, categoryId, pageable);
            } else {
                //新着順カテゴリ検索
                itemsPage = itemRepository.findByCategoryIdAndDeleteFlagOrderByInsertDateDesc(categoryId, Constant.NOT_DELETED, pageable);
            }
        } else {
            if (sortType == 2) {
                // 売れ筋全体検索
                itemsPage = itemRepository.findByDeleteFlagOrderByHotSellDescPage(Constant.NOT_DELETED, pageable);
            } else {
                //新着順全体検索
                itemsPage = itemRepository.findByDeleteFlagOrderByInsertDateDesc(Constant.NOT_DELETED, pageable);
            }
        }
        // Viewへ商品一覧とページ情報を渡す
        List<Item> itemList = itemsPage.getContent();
        model.addAttribute("items", itemList);
        model.addAttribute("pages", itemsPage);

        // 商品一覧画面を表示
        return "client/item/list";
    }

    
   
    //
  //売れ筋順処理
  	/**	@RequestMapping(path = "/client/item/list/2", method = { RequestMethod.GET, RequestMethod.POST })
  		public String showItemListBysell(Model model, Pageable pageable) {
  			// 商品情報を検索
  			Page<Item> itemsPage = itemRepository.findByDeleteFlagOrderByHotSellDescPage(Constant.NOT_DELETED, pageable);
  			// ページ内の商品リストを取得
  			List<Item> itemList = itemsPage.getContent();
  			// View へ商品一覧とページ情報を渡す
  			model.addAttribute("items", itemList);
  			model.addAttribute("pages", itemsPage);
  			// 商品一覧画面を表示
  			return "client/item/list";  
  		}	 	
*/
  		
  		}
  		




	@Autowired
<<<<<<< HEAD
	ItemRepository itemRepository;
	
	@Autowired
	ReviewsRepository reviewsRepository;
=======
	ItemService itemService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	AllergyService allergyService;

>>>>>>> 1b6073adb5c002a32edb1913d356eaf19624b339
	/**
	 * Entity、Form、Bean間のデータコピーサービス
	 */
	@Autowired
	BeanTools beanTools;
	@Autowired
	OrderItemRepository orderItemRepository;
	/**
	 * トップ画面 表示処理
	 *
	 * @param model    Viewとの値受渡し
	 * @return "index" トップ画面
	 */
	@RequestMapping(path = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(Model model, Pageable pageable) {
		
		List<OrderItem>orderItemList = orderItemRepository.findAll();
		    if (orderItemList.size() == 0) {
			    model.addAttribute("items", itemRepository.findByDeleteFlagOrderByInsertDateDescPage(Constant.NOT_DELETED, pageable));
			    model.addAttribute("sortType",1);
		    } else  {
			    model.addAttribute("items", itemRepository.findByDeleteFlagOrderByHotSellDescPage(Constant.NOT_DELETED, pageable));
			    
		    }
		    return "index";
		    }


	/**
	商品一覧表示処理
	@param model Viewとのデータ受け渡し
	@param pageable ページング情報（Springが自動設定）
	@return 商品一覧画面テンプレート（client/item/list）
		 */
<<<<<<< HEAD
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
			return "client/item/list";  
		}
=======
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
>>>>>>> 1b6073adb5c002a32edb1913d356eaf19624b339
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

	//	/**
	//	 * 商品一覧表示（カテゴリ選択・アレルギー除外対応）
	//	 *
	//	 * @param categoryId カテゴリID（任意）
	//	 * @param allergyIds 除外アレルギーIDのリスト（任意）
	//	 * @param model Thymeleaf へ渡すモデル
	//	 * @return 商品一覧画面パス
	//	 */
	@GetMapping("/client/item/list/test")
	public String list(
			@RequestParam(name = "allergyIds", required = false) List<Long> allergyIds,
			Model model) {
		List<Item> items;
		if (allergyIds != null && !allergyIds.isEmpty()) {
			items = itemService.findItemsExcludingAllergies(allergyIds);
			System.out.println("アレルギー除外検索");
		} else {
			items = itemService.findItems(0, null).getContent(); // 全件
			System.out.println("全件取得");
		}
		List<Allergy> allergies = allergyService.findAll();
		model.addAttribute("items", items);
		model.addAttribute("allergyList", allergies);
		model.addAttribute("selectedAllergies", allergyIds);
		return "client/item/list";
	}
}
