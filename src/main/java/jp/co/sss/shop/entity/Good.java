package jp.co.sss.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(name = "likes")
public class Good {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_likes_gen")
	@SequenceGenerator(name = "seq_likes_gen", sequenceName = "seq_likes", allocationSize = 1)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name ="user_id")
	private User user; // ユーザーID
	
	@ManyToOne
	@JoinColumn(name ="item_id")
	private Item item; // アイテムID
	
//	@Column
//	private String userId;
//	
//	@Column
//	private String itemId;

	@Column
	private Integer deleteFlg;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String string) {
//		this.userId = string;
//	}
//
//	public String getItemId() {
//		return itemId;
//	}
//
//	public void setItemId(String string) {
//		this.itemId = string;
//	}
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	

	public Integer getDeleteFlg() {
		return deleteFlg;
	}

	public void setDeleteFlg(Integer deleteFlg) {
		this.deleteFlg = deleteFlg;
	}

	

	


}
