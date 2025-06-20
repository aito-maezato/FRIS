package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.shop.entity.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews,Integer>
{
	//商品の口コミをセレクト
	List<Reviews> findByItemIdOrderByIdDesc(Integer itemId);
}