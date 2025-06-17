package jp.co.sss.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 削除フラグで商品一覧を登録日降順で取得（ページング）
	 */
	@Override
	public Page<Item> findItems(int deleteFlag, Pageable pageable) {
		return itemRepository.findByDeleteFlagOrderByInsertDateDescPage(deleteFlag, pageable);
	}

	/**
	 * 商品IDと削除フラグで1件取得
	 */
	@Override
	public Item findByIdAndDeleteFlag(Integer id, int deleteFlag) {
		return itemRepository.findByIdAndDeleteFlag(id, deleteFlag);
	}

	/**
	 * 商品名と削除フラグで1件取得
	 */
	@Override
	public Item findByNameAndDeleteFlag(String name, int deleteFlag) {
		return itemRepository.findByNameAndDeleteFlag(name, deleteFlag);
	}

	/**
	 * :チェックマーク_緑: アレルギーIDに含まれるアレルゲンを含まない商品一覧を取得
	 */
	@Override
	public List<Item> findItemsExcludingAllergies(List<Long> allergyIds) {
		return itemRepository.findItemsNotContainingAllergies(allergyIds);
	}
}