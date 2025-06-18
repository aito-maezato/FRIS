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
import jp.co.sss.shop.entity.OrderItem;
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


  		
  		}
  		




