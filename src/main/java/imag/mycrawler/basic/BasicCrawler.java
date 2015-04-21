
package imag.mycrawler.basic;

import imag.crawler.crawler.Page;
import imag.crawler.crawler.WebCrawler;
import imag.crawler.parser.HtmlParseData;
import imag.crawler.url.WebURL;
import imag.databaseSql.dao.ImagSQLDao;
import imag.mycrawler.dbaseInfor.NewsDataInfor;

import java.io.FileWriter;
import java.io.IOException;
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

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author wxm516
 *
 */
public class BasicCrawler extends WebCrawler {
	
//	private static final Pattern IMAGE_EXTENSIONS = Pattern
//			.compile(".*\\.(bmp|gif|jpg|png)$");
//
//	private static final Pattern URL_PATTERNS = Pattern
//			.compile(".*(\\.(shtml|html|htm))$");
	
	private static String strQryWord;
	
	public static void configure(String qryWord) {
		strQryWord = qryWord;
	}
	
	public String getQryWord(){
		return strQryWord;
	}
	public void setQryWord(String strQryWord){
		this.strQryWord = strQryWord;
	}

	

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid(); // 这是程序定义的ID
		String url = page.getWebURL().getURL(); // URL地址
		String domain = page.getWebURL().getDomain(); // 域名，如baidu.com
		String path = page.getWebURL().getPath(); // 路径，不包含URL参数
		String subDomain = page.getWebURL().getSubDomain(); // 子域名，如www,
		String parentUrl = page.getWebURL().getParentUrl(); // 父页面，即从哪个页面发现的该URL的
		String anchor = page.getWebURL().getAnchor(); // 锚，即HTML显示的信息，如<a
														// href="***">锚</a>

		/*System.out.println("Docid: " + docid);
		System.out.println("URL: " + url);
		System.out.println("Domain: '" + domain + "'");
		System.out.println("Sub-domain: '" + subDomain + "'");
		System.out.println("Path: '" + path + "'");
		System.out.println("Parent page: " + parentUrl);
		System.out.println("Anchor text: " + anchor);*/

		String strContent = "";
		String strContText = "";
		String strTitle = "";
		String strImgUrl = "";

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText(); // HTML显示的信息
			String html = htmlParseData.getHtml(); // HTML全部代码
			Set<WebURL> links = htmlParseData.getOutgoingUrls(); // 在该页面发现的全部URL地址
			strContent = text;

			// get title;
			strTitle = htmlParseData.getTitle();
			System.out.println("strTitle: " + strTitle);

			// 163 site;
			// get the document text according to the title;
			Document doc = Jsoup.parse(html);
			Element first = doc.select("div#endText").first();

			if (first == null) {
				first = doc.select("div#article").first();
				if (first == null) {
					first = doc.select("div#Content").first();
					if (first == null) {
						strContText = "";
					} else {
						strContText = first.text();
					}
				} else {
					strContText = first.text();
					if (strContText == null) {
						strContText = "";
					}
				}
			} else {
				strContText = first.text();
				if (strContText == null) {
					strContText = "";
				}
			}
			// get img or video url of this article;
			// 对于新华网来说,图片的url得到的是缺省的,需要把前面部分补齐;
			// 比如url,http://www.gs.xinhuanet.com/2015-04/01/1114836692_14278597555641n.jpg;
			// 最后一个'/'前面部分是缺省的;
			// 网页url :http://www.gs.xinhuanet.com/2015-04/01/c_1114836692.htm;
			// 要到成自动的分析是否需要添加 url 前缀;
			// 获得一个以<class="img_wrapper"节点集合;
			String urlPrefix = url.substring(0, url.lastIndexOf('/') + 1);
			if (first != null) {
				Elements imgLinks = first.getElementsByTag("img");
				StringBuffer stringBuffer = new StringBuffer();
				int i = 0;
				for (i = 0; i < imgLinks.size(); i++) {
					// 遍历集合获得第一个节点元素
					Element et = imgLinks.get(i).select("img[src]").first();
					// 获取元素的href属性
					if (et.attr("src") != null)
						stringBuffer.append(et.attr("src") + ";"); // img should
																	// add the
																	// url
																	// prefix;
				}
				strImgUrl = stringBuffer.toString();
			}

			System.out.println("strContText: " + strContText);
			System.out.println("strImgUrl: " + strImgUrl);


			System.out.println("Text length: " + text.length());
			// System.out.println("Text : " + text);
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
		}

		Header[] responseHeaders = page.getFetchResponseHeaders(); // 页面服务器返回的HTML头信息
		if (responseHeaders != null) {
			// System.out.println("Response headers:");
			for (Header header : responseHeaders) {
				// System.out.println("\t" + header.getName() + ": " +
				// header.getValue());
			}
		}

		System.out.println("=============");

	}

	public void saveIntoFile(String file, String strUrl, String strParentUrl,
			Header[] responseHeaders, String strTitle, String strContent) {

		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(file);
			if (responseHeaders != null) {
				System.out.println("Response headers:");
				fwriter.write("Response headers:");
				for (Header header : responseHeaders) {
					// System.out.println("\t" + header.getName() + ": " +
					// header.getValue());
					fwriter.write("\t" + header.getName() + ": "
							+ header.getValue());
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

	public void  savaIntoDatabase(NewsDataInfor newsDataInfor) {
		ImagSQLDao imagSQLDao = new ImagSQLDao();
		MysqlDataSource mysql = imagSQLDao.getDataSource();
		/***************** 添加之前要先判断当前要保存的url是否在数据库中已经下载过,没有下载记录然后才保存  *****************/
		//List<NewsDataInfors> newsInforsList = newsInforsdao.qryNewsBySubDomain(newsDataInfor.getSubDomain());
		//List<NewsDataInfor> newsInforsList = mysqlDao.qryNewsBySubDomain(newsDataInfor.getSubDomain());
		List<String> newsUrlsList = imagSQLDao.qryNewsUrlBySubDomain(newsDataInfor.getSubDomain());
		int    nSize = newsUrlsList.size();
		/***************** 查找是否在数据库中已经存在相同url的news信息 *****************/
		int    nFlag = 1;
		for(int iIndex=0;iIndex<nSize;iIndex++){
			String newsUrl = newsUrlsList.get(iIndex);
			if(newsUrl.equals(newsDataInfor.getNewsUrl())){
				// there is already has a same url in the database;
				nFlag = 2;
				break ;
			}
		}
		if(nFlag == 1){ 
			// there is no  same 'news_url' in the database;
			String sql = "INSERT INTO `imagdata`.`newsdata` (`id`, `qry_word`, `news_url`, `pub_time`,  `parent_url`, `sub_domain`, `img_urls`,`video_urls`,`title`,`document`,`web_cache`) VALUES (NULL, :qryWord, :newsUrl, :pubTime, :parentUrl, :subDomain, :imgUrls,:videoUrls,:newsTitle,:newsDocument, :webCache);";
			Map[] maps = new Map[1];
			for (int i = 0; i < 1; i++) {
				HashMap<String, Object> paramMap = new HashMap();
				paramMap.put("qryWord", newsDataInfor.getQryWord());
				paramMap.put("newsUrl", newsDataInfor.getNewsUrl());
				paramMap.put("pubTime", newsDataInfor.getPubTime());
				paramMap.put("parentUrl", newsDataInfor.getParentUrl());
				paramMap.put("subDomain", newsDataInfor.getSubDomain());
				paramMap.put("imgUrls", newsDataInfor.getImgUrls());
				paramMap.put("videoUrls", newsDataInfor.getVideoUrls());
				paramMap.put("newsTitle", newsDataInfor.getNewsTitle());
				paramMap.put("newsDocument", newsDataInfor.getNewsDocument());
				paramMap.put("webCache", newsDataInfor.getWebCache());
				maps[i] = paramMap;
			}
			imagSQLDao.saveIntoBase(sql, maps);
		}
		
	}

}
