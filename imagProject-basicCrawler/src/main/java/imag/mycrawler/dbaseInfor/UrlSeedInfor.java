
package imag.mycrawler.dbaseInfor;

/**
 * @author xmwang
 * @ 2015
 */
public class UrlSeedInfor {
	
	protected long id;
	
	protected String urlSeed;
	
	protected String pageFormat;
	
	protected String domain;
	
	
	public long getId(){
		return id;
	}
	public void setId(long id){
		this.id = id;
	}
	
	public String getUrlSeed(){
		return urlSeed;
	}
	public void setUrlSeed(String urlSeed){
		this.urlSeed = urlSeed;
	}
	
	public String getPageFormat(){
		return pageFormat;
	}
	public void setPageFormat(String pageFormat){
		this.pageFormat = pageFormat;
	}
	
}