
package imag.crawler.examples.basic;

import imag.crawler.crawler.Page;
import imag.crawler.parser.HtmlParseData;
import imag.crawler.url.WebURL;
import imag.databaseSql.ImagSQLDao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author xmwang
 * @ 2015
 */
public class TencentNewsCrawler extends BasicCrawler{

	@Override
	  public void visit(Page page) {
		
		    int docid = page.getWebURL().getDocid(); //这是程序定义的ID  
		    String url = page.getWebURL().getURL(); //URL地址  
		    String domain = page.getWebURL().getDomain(); //域名，如baidu.com  
		    String path = page.getWebURL().getPath(); //路径，不包含URL参数  
		    String subDomain = page.getWebURL().getSubDomain(); //子域名，如www,  
		    String parentUrl = page.getWebURL().getParentUrl(); //父页面，即从哪个页面发现的该URL的  
		    String anchor = page.getWebURL().getAnchor(); //锚，即HTML显示的信息，如<a href="***">锚</a>  
		  
		    System.out.println("Docid: " + docid);  
		    System.out.println("URL: " + url);  
		    System.out.println("Domain: '" + domain + "'");  
		    System.out.println("Sub-domain: '" + subDomain + "'");  
		    System.out.println("Path: '" + path + "'");  
		    System.out.println("Parent page: " + parentUrl);  
		    System.out.println("Anchor text: " + anchor);  
		    
		    String strContent = "";
		    String strContText = ""; 
		    String strTitle = "";
		    String strImgUrl = "";
		    String strVideoUrl = "";
		      
		    if (page.getParseData() instanceof HtmlParseData) {  
		        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();  
		        String text = htmlParseData.getText(); //HTML显示的信息  
		        String html = htmlParseData.getHtml(); //HTML全部代码  
		        Set<WebURL> links = htmlParseData.getOutgoingUrls(); //在该页面发现的全部URL地址 
		        strContent = text;
		        
		        // get title;
		        strTitle = htmlParseData.getTitle();
		        System.out.println("strTitle: " + strTitle);  
		        
		        
		        // qq news site;
		        // parse the html infor to get the text of this title;
		        Document doc = Jsoup.parse(html);
		        // Element first = doc.select("div.bd").first();
		        Element first = doc.getElementById("Cnt-Main-Article-QQ");
				if(first==null){
					strContText = "";
				}else{
					strContText = first.text();
					if(strContText==null){
						strContText="";
					}
				}
				// get img or video url;
				// 获得一个以<class="img_wrapper"节点集合;
				Elements imgLinks = first.getElementsByClass("img_wrapper");
				StringBuffer stringBuffer = new StringBuffer();
				int i = 0;
				for (i = 0; i < imgLinks.size(); i++) {
					//遍历集合获得第一个节点元素;
					Element et = imgLinks.get(i).select("img[src]").first();
					//获取元素的src属性;
					if(et.attr("src") != null)
						stringBuffer.append(et.attr("src") + ";");
				}
				strImgUrl = stringBuffer.toString();
		        
				System.out.println("strContText: " + strContText);  
				System.out.println("strImgUrl: " + strImgUrl);  
		      
		        //System.out.println("Text length: " + text.length());  
		        //System.out.println("Text : " + text);  
		        //System.out.println("Html length: " + html.length());  
		        //System.out.println("Number of outgoing links: " + links.size());  
		    }  
		  
		    Header[] responseHeaders = page.getFetchResponseHeaders(); //页面服务器返回的HTML头信息  
		    if (responseHeaders != null) {  
		        //System.out.println("Response headers:");  
		        for (Header header : responseHeaders) {  
		            //System.out.println("\t" + header.getName() + ": " + header.getValue());  
		        }  
		    }  
		    
		    // write into file;
		    // next to write into database;
	        String file = "F:/迅雷下载/dataCrawl/亚投行/tencent亚投行/" + String.valueOf(docid) + ".txt";
	       
	        super.saveIntoFile(file,url,parentUrl,responseHeaders,strTitle,strContText);
	        
	        // save into sql;
	        ImagSQLDao mysqlDao = new ImagSQLDao();
	        MysqlDataSource mysql = mysqlDao.getDataSource();
			String sql = "INSERT INTO `imagdata`.`newsdatatest` (`id`, `news_url`, `parent_url`, `sub_domain`, `docid`,`img_urls`,`video_urls`,`title`,`document`) VALUES (NULL, :newsUrl, :parentUrl, :subDomain, :docId,:imgUrls,:videoUrls,:newsTitle,:newsDocument);";
			Map[] maps = new Map[1];
			for (int i = 0; i < 1; i++) {
					HashMap<String, Object> paramMap = new HashMap();
					paramMap.put("newsUrl", url);
					paramMap.put("parentUrl", parentUrl==null?"NULL":parentUrl);
					paramMap.put("subDomain", domain);
					paramMap.put("docId", docid);
					paramMap.put("imgUrls", strImgUrl.length()==0?"NULL":strImgUrl);
					paramMap.put("videoUrls", strVideoUrl.length()==0?"NULL":strVideoUrl);
					paramMap.put("newsTitle", strTitle);
					paramMap.put("newsDocument", strContText);
					maps[i] = paramMap;
			}
			mysqlDao.execute(sql, maps);
	        
		    System.out.println("============="); 
	  }
	
	
	
	
	
	
}
