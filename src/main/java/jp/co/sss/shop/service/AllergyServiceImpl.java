package jp.co.sss.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.shop.entity.Allergy;
import jp.co.sss.shop.repository.AllergyRepository;

@Service
public class AllergyServiceImpl implements AllergyService {
	@Autowired
	private AllergyRepository allergyRepository;

	@Override
	public List<Allergy> findAll() {
		return allergyRepository.findAll();
	}
}
