package imag.mycrawler.dbaseInfor;

/**
 * @author wxm516
 * 
 */
/*
 * 对应数据库表 news_imgs_data,保存含有img的news链接对应的图片二进制数据;
 */
public class NewsImgsInfor {

	protected long    id;
	
	protected String  qryWord;
	
	protected String  newsUrl;
	
	protected String  imgUrl;
	
	protected byte[]  imgData;
	
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
	
	public String getImgUrl(){
		return imgUrl;
	}
	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}
	
	public byte[] getImgData(){
		return imgData;
	}
	public void setImgData(byte[] imgData){
		this.imgData = imgData;
	}
	
	
}
