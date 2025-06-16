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
	private String userId;
	
	@Column
	private String itemId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String string) {
		this.userId = string;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String string) {
		this.itemId = string;
	}
	
//	private Boolean GoodFlg;
//
//	public Boolean getGoodFlg() {
//		return GoodFlg;
//	}
//
//	public void setGoodFlg(Boolean goodFlg) {
//		GoodFlg = goodFlg;
//	}


}
