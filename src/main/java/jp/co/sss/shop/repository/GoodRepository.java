package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Good;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;
// いいね用のリポジトリ
public interface GoodRepository extends JpaRepository<Good, Integer>{
	
	
	Good findByUserAndItem(User user,Item item);
	Good findByItem(Item item);
	List <Good> findByUserAndDeleteFlg(User user, Integer deleteFlg);
	
//	Good findByDeleteFlg(Integer deleteFlg);
//	Good findbyItemIdAndDeleteFlg(int id, int deleteFlg);
//	Good findAllbyItemId(Integer ItemId);
}
