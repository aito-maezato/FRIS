package jp.co.sss.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "likes")
public class Good {
	
	@Id
	private Integer id;
	
//	@ManyToOne
//	@JoinColumn(name ="user_id")
//	private User user; // ユーザーID
//	
//	@ManyToOne
//	@JoinColumn(name ="item_id")
//	private Item item; // アイテムID
	
	@Column
	private Integer userId;
	
	@Column
	private Integer itemId;

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

}
