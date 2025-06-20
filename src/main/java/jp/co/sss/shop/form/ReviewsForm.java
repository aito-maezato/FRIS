package jp.co.sss.shop.form;

import java.io.Serializable;

import jakarta.validation.constraints.Size;

public class ReviewsForm implements Serializable {
	
	private Integer id;
	private Integer userId;
	private Integer itemId;
	
	@Size(min = 1,max=200)
	private String reviews;
	private Integer deleteFlag = 0;
	
	
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
		return reviews;
	}
	public void setReviews(String reviews) {
		this.reviews = reviews;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
}
