
package imag.mycrawler.basic;

import imag.crawler.crawler.Page;
import imag.crawler.parser.HtmlParseData;
import imag.crawler.url.WebURL;

import java.util.Set;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author xmwang
 * @ 2015
 */
public class SinaNewsCrawler extends BasicCrawler{

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
		      
		    if (page.getParseData() instanceof HtmlParseData) {  
		        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();  
		        String text = htmlParseData.getText(); //HTML显示的信息  
		        String html = htmlParseData.getHtml(); //HTML全部代码  
		        Set<WebURL> links = htmlParseData.getOutgoingUrls(); //在该页面发现的全部URL地址 
		        strContent = text;
		        
		        // get title;
		        strTitle = htmlParseData.getTitle();
		        System.out.println("strTitle: " + strTitle);  
		        
		        // Override;
		        // sina site;
		        Document doc = Jsoup.parse(html);
		        Element first = doc.select("div.BSHARE_POP").first();
				if(first==null){
					strContText = "";
				}else{
					strContText = first.text();
					if(strContText==null){
						strContText="";
					}
				}
				// get img or video url;
				//获得一个以<class="img_wrapper"节点集合;
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
		      
		    }  
		  
		    Header[] responseHeaders = page.getFetchResponseHeaders(); //页面服务器返回的HTML头信息 ; 
		    if (responseHeaders != null) {  
		        //System.out.println("Response headers:");  
		        for (Header header : responseHeaders) {  
		            //System.out.println("\t" + header.getName() + ": " + header.getValue());  
		        }  
		    }  
		    
		    // write into file;
		    // next to write into database;
	        String file = "F:/迅雷下载/dataCrawl/亚投行/sina亚投行/" + String.valueOf(docid) + ".txt";
	       
	        super.saveIntoFile(file,url,parentUrl,responseHeaders,strTitle,strContText);
	        
		    System.out.println("============="); 
	  }
}
