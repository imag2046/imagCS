/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package imag.crawler.mycrawler.basic;

import imag.crawler.crawler.Page;
import imag.crawler.crawler.WebCrawler;
import imag.crawler.parser.HtmlParseData;
import imag.crawler.tests.MysqlDao;
import imag.crawler.url.WebURL;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lakeside.data.*;
import com.lakeside.data.sqldb.MysqlDataSource;


/**
 * @author wxm516
 *
 */
public class BasicCrawler extends WebCrawler {

  private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
  
  private static final Pattern URL_PATTERNS = Pattern.compile(".*(\\.(shtml|html|htm))$");

  /**
   * You should implement this function to specify whether the given url
   * should be crawled or not (based on your crawling logic).
   */
  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    // Ignore the url if it has an extension that matches our defined set of image extensions.
    if (IMAGE_EXTENSIONS.matcher(href).matches()) {
      return false;
    }
    
    // filter the url to get the target url and to download;
    if (URL_PATTERNS.matcher(href).matches()) {
        return true;
    }

    // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
    //return href.startsWith("http://www.ics.uci.edu/");
   // return href.startsWith("http://");
    return false;
  }

  /**
   * This function is called when a page is fetched and ready to be processed
   * by your program.
   */
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
	        
	        
	        
	        // parse the html infor to get the text of this title;
	       /* // sohu site;
	        Document doc = Jsoup.parse(html);
	        Element first = doc.select("div.text").first();
			if(first==null){
				strContText = "";
			}else{
				strContText = first.text();
				if(strContText==null){
					strContText="";
				}
			}
			*/
	        
	     // qq news site;
//	        Document doc = Jsoup.parse(html);
//	        //Element first = doc.select("div.bd").first();
//	        Element first = doc.getElementById("Cnt-Main-Article-QQ");
//			if(first==null){
//				strContText = "";
//			}else{
//				strContText = first.text();
//				if(strContText==null){
//					strContText="";
//				}
//			}
	        
	        /*//sina site;
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
			//获得一个以<class="img_wrapper"节点集合
			Elements imgLinks = first.getElementsByClass("img_wrapper");
			StringBuffer stringBuffer = new StringBuffer();
			int i = 0;
			for (i = 0; i < imgLinks.size(); i++) {
				//遍历集合获得第一个节点元素
				Element et = imgLinks.get(i).select("img[src]").first();
				//获取元素的href属性
				if(et.attr("src") != null)
					stringBuffer.append(et.attr("src") + ";");
			}
			strImgUrl = stringBuffer.toString();*/
	        
	       /* // xinhuawang site;
	        // get the document text according to the title;
	        Document doc = Jsoup.parse(html);
	        Element first = doc.select("div#content").first();
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
						stringBuffer.append( urlPrefix + et.attr("src") + ";"); // img should add the url prefix;
				}
				strImgUrl = stringBuffer.toString();
			}*/
			
	        
	        // 163 site;
	        // get the document text according to the title;
	        Document doc = Jsoup.parse(html);
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
			

	        
	        
	      // test: write the html content;
//	      FileWriter fwriter = null;
//	  	  try {
//	  	   fwriter = new FileWriter("F:/迅雷下载/dataCrawl/htmltext.txt");
//	  	   fwriter.write(strContText);
//	  	  } catch (IOException ex) {
//	  	   ex.printStackTrace();
//	  	  } finally {
//	  	   try {
//	  	    fwriter.flush();
//	  	    fwriter.close();
//	  	   } catch (IOException ex) {
//	  	    ex.printStackTrace();
//	  	   }
//	  	  }
	  	 //System.out.println("Html : " + html);  
	      
	        System.out.println("Text length: " + text.length());  
	        //System.out.println("Text : " + text);  
	        System.out.println("Html length: " + html.length());  
	        System.out.println("Number of outgoing links: " + links.size());  
	    }  
	  
	    Header[] responseHeaders = page.getFetchResponseHeaders(); //页面服务器返回的HTML头信息  
	    if (responseHeaders != null) {  
	        //System.out.println("Response headers:");  
	        for (Header header : responseHeaders) {  
	            //System.out.println("\t" + header.getName() + ": " + header.getValue());  
	        }  
	    }  
	    
	    // write into file;
        String file = "F:/迅雷下载/dataCrawl/亚投行/sina亚投行/" + String.valueOf(docid) + ".txt";
        
        saveIntoFile(file,url,parentUrl,responseHeaders,strTitle,strContText);
        
	    System.out.println("============="); 

	/*int docid = page.getWebURL().getDocid();
    String url = page.getWebURL().getURL();
    String domain = page.getWebURL().getDomain();
    String path = page.getWebURL().getPath();
    String subDomain = page.getWebURL().getSubDomain();
    String parentUrl = page.getWebURL().getParentUrl();
    String anchor = page.getWebURL().getAnchor();

    logger.debug("Docid: {}", docid);
    logger.info("URL: {}", url);
    logger.debug("Domain: '{}'", domain);
    logger.debug("Sub-domain: '{}'", subDomain);
    logger.debug("Path: '{}'", path);
    logger.debug("Parent page: {}", parentUrl);
    logger.debug("Anchor text: {}", anchor);

    if (page.getParseData() instanceof HtmlParseData) {
      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      String html = htmlParseData.getHtml();
      Set<WebURL> links = htmlParseData.getOutgoingUrls();

      logger.debug("Text length: {}", text.length());
      logger.debug("Html length: {}", html.length());
      logger.debug("Number of outgoing links: {}", links.size());
    }

    Header[] responseHeaders = page.getFetchResponseHeaders();
    if (responseHeaders != null) {
      logger.debug("Response headers:");
      for (Header header : responseHeaders) {
        logger.debug("\t{}: {}", header.getName(), header.getValue());
      }
    }

    logger.debug("=============");*/

  }
  
  
  public void saveIntoFile(String file,String strUrl,String strParentUrl,Header[] responseHeaders,String strTitle,String strContent) {
	  
	  FileWriter fwriter = null;
	  try {
	   fwriter = new FileWriter(file);
	   if (responseHeaders != null) {  
	        System.out.println("Response headers:");  
	        fwriter.write("Response headers:");
	        for (Header header : responseHeaders) {  
	            //System.out.println("\t" + header.getName() + ": " + header.getValue());  
	            fwriter.write("\t" + header.getName() + ": " + header.getValue());
	        }  
	    }  
	   fwriter.write("\r\n"); 
	   fwriter.write("url: " + strUrl + "\r\n"); 
	   fwriter.write("parent url: " + strParentUrl + "\r\n"); 
	   fwriter.write("title : " + strTitle + "\r\n"); 
	   fwriter.write(strContent);
	  } catch (IOException ex) {
	   ex.printStackTrace();
	  } finally {
	   try {
	    fwriter.flush();
	    fwriter.close();
	   } catch (IOException ex) {
	    ex.printStackTrace();
	   }
	  }
	 }
  
   // mysql or hbase; 
  /**
 * @param filePath
 */
public void saveGroupInfoIntoSQL(String filePath) {
		// resource;
		MysqlDao mysqlDao = new MysqlDao();
		MysqlDataSource mysql = mysqlDao.getDataSource();
		// get the data in the file ;
		// img_group list;
		String sql = "INSERT INTO `dp_img_test`.`dp_all_img_group` (`id`, `group_id`, `img_id`, `pos_neg_flag`, `img_order`) VALUES (NULL, :g_id, :img_id, :flag, :order);";
		// String filePath = "/home/wxm/saveResults/getImg_Group_Info.txt";
		List<String> list = new ArrayList<String>();
		try {
			//list = getList(filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map[] maps = new Map[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				HashMap<String, Object> paramMap = new HashMap();
				String strLine[] = list.get(i).split(",");
				paramMap.put("g_id", strLine[0]);
				paramMap.put("img_id", strLine[1]);
				paramMap.put("flag", strLine[2]);
				paramMap.put("order", strLine[3]);
				maps[i] = paramMap;
			}
		}
		mysqlDao.execute(sql, maps);
	}
  
  
}
