package imag.mycrawler.dbaseInfor;

/**
 * @author wxm516
 *
 */
public class CrawlQrywordInfor {
	
	protected long      id;
	
	protected String    qryWord;
	
	protected int       startPage;
	
	protected int       endPage;
	
	
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
	
	public int getStartPage(){
		return startPage;
	}
	public void setStartPage(int startPage){
		this.startPage = startPage;
	}
	
	public int getEndPage(){
		return endPage;
	}
	public void setEndPage(int endPage){
		this.endPage = endPage;
	}

}
