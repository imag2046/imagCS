
package imag.mycrawler.basic;

/**
 * @author xmwang
 * @ 2015
 */
public class NewsDataInfor {

	protected long id;
	
	protected String newsUrl;

	protected String parentUrl;
	
	protected String subDomain;
	
	protected long docId;
	
	protected String imgUrls;
	
	protected String videoUrls;
	
	protected String newsTitle;
	
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
