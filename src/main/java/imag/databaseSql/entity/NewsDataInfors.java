/**
 * @author xmwang
 * @ 2015
 */
package imag.databaseSql.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wxm516
 *
 */

@Entity
@Table(name="newsdatatest")
public class NewsDataInfors {
	@Id
	@GeneratedValue
	protected long id;
	
	@Column(name="news_url")
	protected String newsUrl;
	
	@Column(name="parent_url")
	protected String parentUrl;
	
	@Column(name="sub_domain")
	protected String subDomain;
	
	@Column(name="docid")
	protected long docId;
	
	@Column(name="img_urls")
	protected String imgUrls;
	
	@Column(name="video_urls")
	protected String videoUrls;
	
	@Column(name="title")
	protected String newsTitle;
	
	@Column(name="document")
	protected String newsDocument;
	
	
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	
	public String getNewsUrl(){
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl){
		this.newsUrl = newsUrl;
	}
	
	public String getParentUrl(){
		return parentUrl;
	}
	public void setParentUrl(String parentUrl){
		this.parentUrl = parentUrl;
	}
	
	public String getSubDomain(){
		return subDomain;
	}
	public void setSubDomain(String subDomain){
		this.subDomain = subDomain;
	}
	
	public long getDocId(){
		return docId;
	}
	public void setDocId(long docId){
		this.docId = docId;
	}
	
	public String getImgUrls(){
		return imgUrls;
	}
	public void setImgUrls(String imgUrls){
		this.imgUrls = imgUrls;
	}
	
	public String getVideoUrls(){
		return videoUrls;
	}
	public void setVideoUrls(String videoUrls){
		this.videoUrls = videoUrls;
	}
	
	public String getNewsTitle(){
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle){
		this.newsTitle = newsTitle;
	}

	public String getNewsDocument(){
		return newsDocument;
	}
	public void setNewsDocument(String newsDocument){
		this.newsDocument = newsDocument;
	}

}
