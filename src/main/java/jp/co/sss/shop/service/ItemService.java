package jp.co.sss.shop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jp.co.sss.shop.entity.Item;

/**
 * 商品情報操作サービスのインターフェース
 * 商品情報の検索や取得を行うサービス定義
 */
public interface ItemService {
	/**
	 * 商品情報を削除フラグで絞り、登録日降順でページ取得する
	 *
	 * @param deleteFlag 削除フラグ（0:未削除、1:削除済）
	 * @param pageable ページング情報
	 * @return 商品エンティティのページオブジェクト
	 */
	Page<Item> findItems(int deleteFlag, Pageable pageable);

	/**
	 * 商品IDと削除フラグを指定して1件の商品を取得
	 *
	 * @param id 商品ID
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ（存在しない場合はnull）
	 */
	Item findByIdAndDeleteFlag(Integer id, int deleteFlag);

	/**
	 * 商品名と削除フラグを指定して商品を1件取得
	 *
	 * @param name 商品名
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ（存在しない場合はnull）
	 */
	Item findByNameAndDeleteFlag(String name, int deleteFlag);

	/**
	 * :チェックマーク_緑: 指定アレルギーを含まない商品一覧を取得（カテゴリは使わない）
	 *
	 * @param allergyIds 除外するアレルギーIDのリスト
	 * @return 商品リスト
	 */
	List<Item> findItemsExcludingAllergies(List<Long> allergyIds);
}