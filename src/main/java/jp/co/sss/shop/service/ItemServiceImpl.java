package jp.co.sss.shop.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.repository.ItemRepository;
/**
 * 商品情報操作サービスの実装クラス
 * ItemRepository を利用して DB からデータ取得を行う
 *
 * @author
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Item> findItems(int deleteFlag, Pageable pageable) {
        return itemRepository.findByDeleteFlagOrderByInsertDateDescPage(deleteFlag, pageable);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Item findByIdAndDeleteFlag(Integer id, int deleteFlag) {
        return itemRepository.findByIdAndDeleteFlag(id, deleteFlag);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Item findByNameAndDeleteFlag(String name, int deleteFlag) {
        return itemRepository.findByNameAndDeleteFlag(name, deleteFlag);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> findByCategoryId(Long categoryId) {
        return itemRepository.findByCategoryId(categoryId);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Item> findItemsByCategoryExcludingAllergies(Long categoryId, List<Long> allergyIds) {
        return itemRepository.findItemsNotContainingAllergies(categoryId, allergyIds);
    }
}