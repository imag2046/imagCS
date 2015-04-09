package imag.mycrawler.basic;

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
public class TencentNewsCrawler extends BasicCrawler {

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
			String text = htmlParseData.getText(); // HTML显示的信息
			String html = htmlParseData.getHtml(); // HTML全部代码
			Set<WebURL> links = htmlParseData.getOutgoingUrls(); // 在该页面发现的全部URL地址
			strContent = text;

			/********************* get title *********************/
			strTitle = htmlParseData.getTitle();
			System.out.println("strTitle: " + strTitle);

			/********************* parse the html infor to get the text of this title *********************/
			Document doc = Jsoup.parse(html);
			// Element first = doc.select("div.bd").first();
			Element first = doc.getElementById("Cnt-Main-Article-QQ");
			if (first == null) {
				strContText = "";
			} else {
				strContText = first.text();
				if (strContText == null) {
					strContText = "";
				}
			}
			
			if(first != null){
				
				/********************* get img or video url *********************/
				/********************* 获得一个以<class="img_wrapper"节点集合  *********************/
				Elements imgLinks = first.getElementsByClass("img_wrapper");
				if(imgLinks == null){
					strImgUrl = "";
				}else{
					StringBuffer stringBuffer = new StringBuffer();
					int i = 0;
					for (i = 0; i < imgLinks.size(); i++) {
						// 遍历集合获得第一个节点元素;
						Element et = imgLinks.get(i).select("img[src]").first();
						// 获取元素的src属性;
						if (et.attr("src") != null)
							stringBuffer.append(et.attr("src") + ";");
					}
					strImgUrl = stringBuffer.toString();
				}
				
				/********************* get video url *********************/
				Elements videoLinks = first.getElementsByClass("relTxt");
				if(videoLinks ==null){
					strVideoUrl = "";
				}else{
					StringBuffer vLinkBuffer = new StringBuffer();
					int iVLink = 0;
					for (iVLink = 0; iVLink < videoLinks.size(); iVLink++) {
						// 遍历集合获得第一个节点元素;
						Element et = videoLinks.get(iVLink).select("a[href]").first();
						// 获取元素的src属性;
						if (et.attr("href") != null)
							vLinkBuffer.append(et.attr("href") + ";");
					}
					strVideoUrl = vLinkBuffer.toString();
				}
			}
			
			/*System.out.println("strContText: " + strContText);
			System.out.println("strImgUrl: " + strImgUrl);
			System.out.println("strVideoUrl: " + strVideoUrl);*/
		}

		Header[] responseHeaders = page.getFetchResponseHeaders(); // 页面服务器返回的HTML头信息
		if (responseHeaders != null) {
			// System.out.println("Response headers:");
			for (Header header : responseHeaders) {
				// System.out.println("\t" + header.getName() + ": " + header.getValue());
			}
		}

		/***************** write into file *****************/
		// String file = "F:/迅雷下载/dataCrawl/亚投行/tencent亚投行/" + String.valueOf(docid) + ".txt";
		// super.saveIntoFile(file,url,parentUrl,responseHeaders,strTitle,strContText);
		/***************** Save Into NewsDataInfor Class *****************/
		NewsDataInfor newsDataInfor = new NewsDataInfor();
		newsDataInfor.setNewsUrl(url);
		newsDataInfor.setParentUrl(parentUrl == null ? "NULL" : parentUrl);
		newsDataInfor.setSubDomain(domain);
		newsDataInfor.setDocId(docid);
		newsDataInfor.setImgUrls(strImgUrl.length() == 0 ? "NULL" : strImgUrl);
		newsDataInfor.setVideoUrls(strVideoUrl.length() == 0 ? "NULL" : strVideoUrl);
		newsDataInfor.setNewsTitle(strTitle);
		newsDataInfor.setNewsDocument(strContText);
		/***************** save into mySQL database *****************/
		super.savaIntoDatabase(newsDataInfor);

		System.out.println("=============");
	}

}