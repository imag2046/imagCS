package imag.crawler.crawler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="dp_all_img")
public class DPImg {
	
	@Id
	@GeneratedValue
	protected long id;
	
	@Column(name="url")
	protected String imgurl;
	
	
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	
	public String getimgurl(){
		return imgurl;
	}
	public void setimgurl(String imgurl){
		this.imgurl = imgurl;
	}
	
	
	
	
	
	

}