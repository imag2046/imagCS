
package imag.crawler.mycrawler.basic;

import imag.crawler.crawler.CrawlConfig;
import imag.crawler.crawler.CrawlController;
import imag.crawler.fetcher.PageFetcher;
import imag.crawler.robotstxt.RobotstxtConfig;
import imag.crawler.robotstxt.RobotstxtServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wxm516
 * @description This file is used for debug, not for maven package to a execute jar file;
 *              The same file in src/main/java is used for maven package to a execute jar file;
 */
public class BasicCrawlController {
  private static final Logger logger = LoggerFactory.getLogger(BasicCrawlController.class);
  
 
  public static void main(String[] args) throws Exception {
	  if (args.length != 3) {
	    	logger.info("Needed parameters: ");
	    	logger.info("\t the query keyword used to crawl ");
	    	logger.info("\t the start page to crawl ");
	    	logger.info("\t the end   page to crawl ");
	    	//return;
	    }
	    
	    //System.out.println("query keyword : " + args[0]);
	    //System.out.println("start page : " + args[1]);
	    //System.out.println("end   page : " + args[2]);
	    System.out.println("news domain : " + "all sites in dbase. ");
    
	    
    /*
     * crawlStorageFolder is a folder where intermediate crawl data is
     * stored.
     */
    String crawlStorageFolder = "src/main/resources/dataCrawl";  

    CrawlConfig config = new CrawlConfig();

    config.setCrawlStorageFolder(crawlStorageFolder);

    /*
     * Be polite: Make sure that we don't send more than 1 request per
     * second (1000 milliseconds between requests).
     */
    config.setPolitenessDelay(1000);

    /*
     * You can set the maximum number of pages to crawl. The default value
     * is -1 for unlimited number of pages
     */
    config.setMaxPagesToFetch(-1);

    /**
     * Do you want crawler4j to crawl also binary data ?
     * example: the contents of pdf, or the metadata of images etc
     */
    config.setIncludeBinaryContentInCrawling(false);

    /*
     * Do you need to set a proxy? If so, you can use:
     * config.setProxyHost("proxyserver.example.com");
     * config.setProxyPort(8080);
     *
     * If your proxy also needs authentication:
     * config.setProxyUsername(username); config.getProxyPassword(password);
     */

    /*
     * This config parameter can be used to set your crawl to be resumable
     * (meaning that you can resume the crawl from a previously
     * interrupted/crashed crawl). Note: if you enable resuming feature and
     * want to start a fresh crawl, you need to delete the contents of
     * rootFolder manually.
     */
    config.setResumableCrawling(false);

    /*
     * Instantiate the controller for this crawl.
     */
    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    /*
     * For each crawl, you need to add some seed urls. These are the first
     * URLs that are fetched and then the crawler starts following links
     * which are found in these pages
     */
   // controller.addSeed("http://news.qq.com/dc_column_article/TagsList.htm?tags=%E5%8D%97%E4%BA%AC");  // 南京;
   // controller.addSeed("http://news.qq.com/dc_column_article/TagsList.htm?tags=反腐");  // 中科院;
   // controller.addSeed("http://news.qq.com/dc_column_article/TagsList.htm?tags=环保");  // 中科院;
   // controller.addSeed("http://news.qq.com/dc_column_article/TagsList.htm?tags=南京理工大学");  // 中科院;
    
    
    /*******************下面的两个参数是需要修改的1-numberOfCrawlers 2-setMaxDepthOfCrawling********************************************/
    /*
     * numberOfCrawlers shows the number of concurrent threads that should
     * be initiated for crawling.
     */
    //int numberOfCrawlers = Integer.parseInt(args[1]);
    int numberOfCrawlers = 1;
    /*
     * You can set the maximum crawl depth here. The default value is -1 for
     * unlimited depth
     */
    config.setMaxDepthOfCrawling(1);
    
    // 获得对应的site domain上的urlSeed格式,然后根据页码进行拼接url;
    //ImagSQLDao imagSQLDao = new ImagSQLDao();
	//MysqlDataSource mysql = imagSQLDao.getDataSource();
	
	//List<String> newsUrlsList = imagSQLDao.qryNewsUrlBySubDomain("qq.com");
	//System.out.println("newsUrlsList : " + newsUrlsList.size());
    String strQryWord = "马化腾 深圳";
    for(int iPage=1;iPage<2;iPage++){
    	String urlSeed;
    	// 163;
    	//urlSeed =   "http://news.yodao.com/search?q=" + "亚投行" + "&start=" + String.valueOf((iPage-1)*10) + "&s=rank&tr=no_range&keyfrom=search.page&suser=user163&site=163.com";
    	// 163;
    	//urlSeed = "http://news.china.com.cn/rollnews/news/live/2015-04/16/content_32314835.htm";
    	
    	
    	// sohu;
    	// http://news.sogou.com/news?mode=1&manual=true&query=site:sohu.com +亚投行&sort=0&page=3;
    	//urlSeed =  "http://news.sogou.com/news?mode=1&manual=true&query=" + "亚投行" + "&sort=0&page=" + String.valueOf(iPage);
    	// sohu test;
    	//urlSeed = "http://business.sohu.com/20150416/n411380356.shtml";
    	
    	// qq.news;
    	urlSeed =  "http://www.sogou.com/sogou?site=news.qq.com&query=" + strQryWord + "&pid=sogou-wsse-b58ac8403eb9cf17-0004&idx=f&page=" + String.valueOf(iPage);
    	// qq.news test;
    	//urlSeed = "http://news.qq.com/a/20111205/001338.htm";
    	// 新华网;
    	//urlSeed =  "http://info.search.news.cn/result.jspa?pno=" + String.valueOf(iPage) + "&rp=10&t1=0&btn=&t=1&n1=" + "亚投行" + "&np=1&ss=2";
    	// xinhuawang test;
    	//urlSeed = "http://ent.163.com/15/0401/16/AM4KQRKO00031H2L.html";
    	// sina;
    	//urlSeed =  "http://search.sina.com.cn/?c=news&q=" + "亚投行" + "&range=all&num=20&col=1_3&source=&from=&country=&size=&time=&a=&page=" + String.valueOf(iPage) ;
    	// sina test;
    	//urlSeed = "http://finance.sina.com.cn/money/future/20150331/202821857172.shtml";
    	//bing;
    	//urlSeed =  "http://cn.bing.com/search?q=" + "亚投行" + "&first=" + String.valueOf((iPage-1)*10+1) ;
    	//baidu;
    	//urlSeed =  "http://news.baidu.com/ns?word=" + "亚投行" + "&cl=2&ct=1&tn=news&rn=20&ie=utf-8&bt=0&et=0&pn=" + String.valueOf((iPage-1)*20);
    	
    	
    	//test;
    	//urlSeed = "http://news.xinhuanet.com/comments/2015-04/07/c_1114881561.htm";
    	controller.addSeed(urlSeed);
    	//controller.addSeed("http://news.yodao.com/search?q=%E6%9D%8E%E5%85%89%E8%80%80&start=30&length=10&s=rank&tr=no_range&keyfrom=search.page&suser=user163&site=163.com");  
    }
    
    //controller.addSeed("http://news.qq.com/society_index.shtml");  
    //controller.addSeed("http://news.baidu.com/ns?from=news&cl=2&bt=0&y0=2015&m0=3&d0=28&y1=2015&m1=3&d1=28&et=0&q1=%D5%D4%B1%BE%C9%BD&submit=%B0%D9%B6%C8%D2%BB%CF%C2&q3=&q4=&s=1&mt=0&lm=0&begin_date=2015-3-28&end_date=2015-3-28&tn=newsdy&ct1=1&ct=1&rn=20&q6=");  
    //controller.addSeed("http://www.ics.uci.edu/~welling/");  

    /*
     * Start the crawl. This is a blocking operation, meaning that your code
     * will reach the line after this only when crawling is finished.
     */
    //controller.start(BasicCrawler.class, numberOfCrawlers);
    TencentNewsCrawler.configure(strQryWord);
    controller.start(TencentNewsCrawler.class, numberOfCrawlers);
    //controller.start(NetEaseNewsCrawler.class, numberOfCrawlers);
    //controller.start(SohuNewsCrawler.class, numberOfCrawlers);
    controller.shutdown();
    
    
   
  }
}