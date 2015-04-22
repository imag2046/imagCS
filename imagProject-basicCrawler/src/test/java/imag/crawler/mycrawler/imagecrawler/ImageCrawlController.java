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

package imag.crawler.mycrawler.imagecrawler;

import imag.crawler.crawler.CrawlConfig;
import imag.crawler.crawler.CrawlController;
import imag.crawler.fetcher.PageFetcher;
import imag.crawler.robotstxt.RobotstxtConfig;
import imag.crawler.robotstxt.RobotstxtServer;
import imag.databaseSql.dao.ImagSQLDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lakeside.data.sqldb.MysqlDataSource;


public class ImageCrawlController {
  private static final Logger logger = LoggerFactory.getLogger(ImageCrawlController.class);

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      logger.info("Needed parameters: ");
      logger.info("\t rootFolder (it will contain intermediate crawl data)");
      logger.info("\t numberOfCralwers (number of concurrent threads)");
      logger.info("\t storageFolder (a folder for storing downloaded images)");
      //return;
    }

//    String rootFolder = args[0];
//    int numberOfCrawlers = Integer.parseInt(args[1]);
//    String storageFolder = args[2];

    String rootFolder = "src/main/resources/dataCrawl";
    int numberOfCrawlers = 1;
    String storageFolder = "src/main/resources/dataCrawl/imagesCrawl";
    
    CrawlConfig config = new CrawlConfig();

    config.setCrawlStorageFolder(rootFolder);

    /*
     * Since images are binary content, we need to set this parameter to
     * true to make sure they are included in the crawl.
     */
    config.setIncludeBinaryContentInCrawling(true);

    //String[] crawlDomains = {"http://uci.edu/"};
    //String[] crawlDomains = {"http://img1.gtimg.com/news/pics/hv1/0/234/1813/117949995.jpg"};  // imgs url;
    //String[]   crawlImgUrls = new String[]{} ;
    /***************************** 修改成从数据库表 'newsdatatest'  中 获取imgs url list然后下载  *****************************/
    ImagSQLDao imagSQLDao = new ImagSQLDao();
	MysqlDataSource mysql = imagSQLDao.getDataSource();
	List<String> list = imagSQLDao.qryColumn("img_urls", "newsdatatest");
	
	List<String> imgUrlList = new ArrayList<String>();
	String   newsUrl = "";
	String   qryWord = "";
	
	List<Map<String,Object>>  urlsMap = new ArrayList<Map<String,Object>>();  // <'imgs_url','qry_word,news_url'> list;
	int i ,j;
	for(i=0; i<list.size(); i++){
		String str = list.get(i);
		if(!str.equals("NULL")){
			// get the news_url of these img_urls;
			newsUrl = imagSQLDao.getColumnByColumn("news_url", "img_urls", str, "newsdatatest");// get news_url;
			qryWord = imagSQLDao.getColumnByColumn("qry_word", "img_urls", str, "newsdatatest");// get qry_word;
			String[] urls = str.split(";");
			StringBuffer sb = new StringBuffer();
			sb.append(qryWord).append(",").append(newsUrl);// String: "qry_word,news_url";
			
			for(j=0; j<urls.length; j++){
				String imgUrl = urls[j];
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(imgUrl, sb.toString());
				urlsMap.add(map);
				imgUrlList.add(imgUrl);
			}
		}
	}
	String[]   crawlImgUrls = new String[imgUrlList.size()] ;
	for(i=0;i<imgUrlList.size();i++){
		crawlImgUrls[i] = imgUrlList.get(i);
	}
	/***************************** 修改成从数据库表 'newsdatatest'  中 获取imgs url list然后下载  *****************************/
   
    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
    for (String domain : crawlImgUrls) {
      controller.addSeed(domain);
    }

   // ImageCrawler.configure(crawlImgUrls, storageFolder);
    ImageCrawler.configure(crawlImgUrls, storageFolder,urlsMap);

    controller.start(ImageCrawler.class, numberOfCrawlers);
  }
}