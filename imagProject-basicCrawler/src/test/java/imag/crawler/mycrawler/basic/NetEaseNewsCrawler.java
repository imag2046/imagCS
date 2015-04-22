
package imag.crawler.mycrawler.basic;

import imag.crawler.crawler.Page;
import imag.crawler.parser.HtmlParseData;
import imag.crawler.url.WebURL;
import imag.mycrawler.dbaseInfor.NewsDataInfor;

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

public class NetEaseNewsCrawler extends BasicCrawler{
	  @Override
	  public void visit(Page page) {
		  
		  	int    docid;
			String url;
			String domain;
			String path;
			String subDomain;
			String parentUrl;
			String anchor;
			String strContent = "";
			String strContText = "";
			String strTitle = "";
			String strImgUrl = "";
			String strVideoUrl = "";
			String strPubTime = ""; // time format: "2015-04-15 00:10"
			String html = "";
		  
			docid = page.getWebURL().getDocid(); // 这是程序定义的ID
			url = page.getWebURL().getURL(); // URL地址
			domain = page.getWebURL().getDomain(); // 域名，如baidu.com
			path = page.getWebURL().getPath(); // 路径，不包含URL参数
			subDomain = page.getWebURL().getSubDomain(); // 子域名，如www,
			parentUrl = page.getWebURL().getParentUrl(); // 父页面，即从哪个页面发现的该URL的
			anchor = page.getWebURL().getAnchor(); // 锚，即HTML显示的信息，如<a href="***">锚</a>
			
			System.out.println("domain: " + domain);
			System.out.println("subDomain: " + subDomain);
		      
		    if (page.getParseData() instanceof HtmlParseData) {  
		        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();  
		        String text = htmlParseData.getText(); //HTML显示的信息  
		        html = htmlParseData.getHtml(); //HTML全部代码  
		        Set<WebURL> links = htmlParseData.getOutgoingUrls(); //在该页面发现的全部URL地址 
		        strContent = text;
		        
		        // get title;
		        strTitle = htmlParseData.getTitle();
		        System.out.println("strTitle: " + strTitle);  
		        
		        // Override;
		        // 163 site;
		        // get the document text according to the title;
		        Document doc = Jsoup.parse(html);
		        /**********************     get the public time                        **********************/
				//Elements timeLabel = doc.select("#"); // 没有网易自己特定的新闻网页,基本上都是从别的网站上来的链接;
				//strPubTime = timeLabel.get(0).text();
				//System.out.println("strPubTime: " + strPubTime);
		        
				/**********************  get the document text  **********************/
		        Element first = doc.select("div#endText").first();
				if(first==null){
					first = doc.select("div#article").first();
					if(first == null){
						first = doc.select("div#Content").first();
						if(first == null){
							strContText = "";
						}else{
							strContText = first.text();
						}
					}else{
						strContText = first.text();
						if(strContText==null){
							strContText="";
						}
					}
				}else{
					strContText = first.text();
					if(strContText==null){
						strContText="";
					}
				}
				// get img or video url of this article;
				// 对于新华网来说,图片的url得到的是缺省的,需要把前面部分补齐;
				// 比如url,http://www.gs.xinhuanet.com/2015-04/01/1114836692_14278597555641n.jpg; 最后一个'/'前面部分是缺省的;
				// 网页url :http://www.gs.xinhuanet.com/2015-04/01/c_1114836692.htm;
				// 要到成自动的分析是否需要添加 url 前缀;
				// 获得一个以<class="img_wrapper"节点集合;
				String urlPrefix = url.substring(0, url.lastIndexOf('/')+1);
				if(first != null){
					Elements imgLinks = first.getElementsByTag("img");
					StringBuffer stringBuffer = new StringBuffer();
					int i = 0;
					for (i = 0; i < imgLinks.size(); i++) {
						//遍历集合获得第一个节点元素
						Element et = imgLinks.get(i).select("img[src]").first();
						//获取元素的href属性
						if(et.attr("src") != null)
							stringBuffer.append(  et.attr("src") + ";"); // img should add the url prefix;
					}
					strImgUrl = stringBuffer.toString();
				}
				
		        
				System.out.println("strContText: " + strContText);  
				System.out.println("strImgUrl: " + strImgUrl);  
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
		    /***************** write into file *****************/
			// String file = "F:/迅雷下载/dataCrawl/亚投行/tencent亚投行/" + String.valueOf(docid) + ".txt";
			// super.saveIntoFile(file,url,parentUrl,responseHeaders,strTitle,strContText);
			/***************** Save Into NewsDataInfor Class *****************/
		    NewsDataInfor newsDataInfor = new NewsDataInfor();
			newsDataInfor.setNewsUrl(url);
			newsDataInfor.setQryWord(super.getQryWord().length()==0 ? "NULL" : super.getQryWord());
			newsDataInfor.setPubTime(strPubTime == null ? "NULL" : strPubTime);
			newsDataInfor.setParentUrl(parentUrl == null ? "NULL" : parentUrl);
			newsDataInfor.setSubDomain(domain);
			newsDataInfor.setImgUrls(strImgUrl.length() == 0 ? "NULL" : strImgUrl);
			newsDataInfor.setVideoUrls(strVideoUrl.length() == 0 ? "NULL" : strVideoUrl);
			newsDataInfor.setNewsTitle(strTitle.length()==0 ? "NULL" : strTitle);
			newsDataInfor.setNewsDocument(strContText.length()==0 ? "NULL" :strContText );
			newsDataInfor.setWebCache(html.length()==0 ? "NULL" : html);
			/***************** save into mySQL database *****************/
			super.savaIntoDatabase(newsDataInfor);
	        
		    System.out.println("============="); 
	  }
	
}
