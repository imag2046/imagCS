package imag.crawler.crawler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dp_all_img_group")
public class DPImgGroup {
	@Id
	@GeneratedValue
	protected long id;
	
	@Column(name="group_id")
	protected long groupId;
	
	@Column(name="img_id")
	protected long imgId;
	
	@Column(name="pos_neg_flag")
	protected int flag;
	
	@Column(name="img_order")
	protected int imgOrder;
	
	
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	
	public long getGrouoId(){
		return groupId;
	}
	public void setGroupId(long groupId){
		this.groupId = groupId;
	}
	
	public long getImgId(){
		return imgId;
	}
	public void setImgId(long imgId){
		this.imgId = imgId;
	}
	
	public int getFlag(){
		return flag;
	}
	public void setFlag(int flag){
		this.flag = flag;
	}
	
	public int getOrder(){
		return imgOrder;
	}
	public void setOrder(int imgOrder){
		this.imgOrder = imgOrder;
	}
}