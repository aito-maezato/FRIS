package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Item;

/**
 * itemsテーブル用リポジトリ
 * 
 * @author System
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    /**
     * 商品情報を登録日付順に取得 管理者機能で利用
     * 
     * @param deleteFlag 削除フラグ
     * @param pageable ページング情報
     * @return 商品エンティティのページオブジェクト
     */
    @Query("SELECT i FROM Item i WHERE i.deleteFlag = :deleteFlag ORDER BY i.insertDate DESC")
    Page<Item> findByDeleteFlagOrderByInsertDateDesc(@Param(value = "deleteFlag") int deleteFlag, Pageable pageable);

    /**
     * 商品IDと削除フラグを条件に検索（管理者機能で利用）
     * 
     * @param id 商品ID
     * @param deleteFlag 削除フラグ
     * @return 商品エンティティ
     */
    public Item findByIdAndDeleteFlag(Integer id, int deleteFlag);

    /**
     * 商品名と削除フラグを条件に検索 (ItemValidatorで利用)
     * 
     * @param name 商品名
     * @param notDeleted 削除フラグ
     * @return 商品エンティティ
     */
    public Item findByNameAndDeleteFlag(String name, int notDeleted);

    // 名前検索（LIKE）と削除フラグで検索
    List<Item> findByNameLikeAndDeleteFlag(String name, Integer deleteFlag);

    // カテゴリー検索
    Page<Item> findByCategoryIdAndDeleteFlagOrderByInsertDateDesc(Integer categoryId, int deleteFlag, Pageable pageable);

    @Query("SELECT i FROM Item i INNER JOIN i.category c INNER JOIN i.orderItemList oil WHERE i.deleteFlag =:deleteFlag GROUP BY i ORDER BY SUM(oil.quantity) DESC")
	Page<Item> findByDeleteFlagOrderByHotSellDescPage(
	        @Param(value = "deleteFlag") int deleteFlag, Pageable pageable);
  
    //売れ筋カテゴリー検索
    Page<Item> findByDeleteFlagAndCategoryId(Integer deleteFlag, Integer categoryId, Pageable pageable);

    Page<Item> findByDeleteFlag(Integer deleteFlag, Pageable pageable);
	
    @Query("SELECT i FROM Item i INNER JOIN i.category c INNER JOIN i.orderItemList oil "
    	     + "WHERE i.deleteFlag = :deleteFlag AND i.category.id = :categoryId "+ "GROUP BY i ORDER BY SUM(oil.quantity) DESC")
    	Page<Item> findByDeleteFlagAndCategoryIdOrderByHotSellDescPage(
    	        @Param("deleteFlag") int deleteFlag,
    	        @Param("categoryId") int categoryId,
    	        Pageable pageable);

}
