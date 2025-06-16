package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Good;
// いいね用のリポジトリ
public interface GoodRepository extends JpaRepository<Good, Integer>{

	
}
