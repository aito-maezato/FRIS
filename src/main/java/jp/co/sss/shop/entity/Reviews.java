package jp.co.sss.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "Reviews")
public class Reviews {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reviews_gen") 
    @SequenceGenerator(name = "seq_reviews_gen", sequenceName = "seq_reviews", allocationSize = 1) 
	private Integer id;
	
	//ユーザ情報を外部参照して名前を表示
//	@ManyToOne
//	@JoinColumn(name="user_id",referencedColumnName="id")
	@Column
	private Integer userId;
	@Column
	private Integer itemId;
	@Column
	private String Reviews;
	@Column
	private Integer deleteFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getReviews() {
		return Reviews;
	}

	public void setReviews(String reviews) {
		Reviews = reviews;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}