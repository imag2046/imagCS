
package imag.mycrawler.dbaseInfor;

/**
 * @author xmwang
 * @ 2015
 */
/**
 *description: table "newsdata" 
 */
public class NewsDataInfor {

	protected long id;
	
	protected String qryWord;
	
	protected String newsUrl;
	
	protected String pubTime;

	protected String parentUrl;
	
	protected String subDomain;
	
	protected String imgUrls;
	
	protected String videoUrls;
	
	protected String newsTitle;
	
	protected String newsDocument;
	
	protected String webCache;
	
	
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	
	public String getQryWord(){
		return qryWord;
	}
	public void setQryWord(String qryWord){
		this.qryWord = qryWord;
	}
	
	public String getNewsUrl(){
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl){
		this.newsUrl = newsUrl;
	}
	
	public String getPubTime(){
		return pubTime;
	}
	public void setPubTime(String pubTime){
		this.pubTime = pubTime;
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
	
	public String getWebCache(){
		return webCache;
	}
	public void setWebCache(String webCache){
		this.webCache = webCache;
	}

}
