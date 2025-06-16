package jp.co.sss.shop.controller.client.item;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.shop.entity.Allergy;
import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.service.AllergyService;
import jp.co.sss.shop.service.CategoryService;
import jp.co.sss.shop.service.ItemService;
/**
 * 商品一覧画面コントローラー（一般ユーザー、非ログイン向け）
 * アレルギー除外機能付き
 *
 * @author
 */
@Controller
public class ClientItemFilter {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AllergyService allergyService;
    /**
     * 商品一覧表示（カテゴリ選択・アレルギー除外対応）
     *
     * @param categoryId カテゴリID（任意）
     * @param allergyIds 除外アレルギーIDのリスト（任意）
     * @param model Thymeleaf へ渡すモデル
     * @return 商品一覧画面パス
     */
    @GetMapping("/client/item/list")
    public String list(
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "allergyIds", required = false) List<Long> allergyIds,
            Model model) {
        List<Item> items;
        if (categoryId != null && allergyIds != null && !allergyIds.isEmpty()) {
            // カテゴリ指定 & アレルギー除外
            items = itemService.findItemsByCategoryExcludingAllergies(categoryId, allergyIds);
        } else if (categoryId != null) {
            // カテゴリ指定のみ
            items = itemService.findByCategoryId(categoryId);
        } else {
            // 全件取得（削除フラグ0のみ）
            items = itemService.findItems(0, null).getContent(); // ページングなし全件
        }
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