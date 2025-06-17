package jp.co.sss.shop.controller.client.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
import jp.co.sss.shop.service.BeanTools;
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
  		




