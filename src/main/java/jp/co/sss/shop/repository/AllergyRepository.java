package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Allergy;

@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Long> {
	
}
