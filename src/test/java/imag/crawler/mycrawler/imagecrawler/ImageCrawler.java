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

import imag.crawler.crawler.Page;
import imag.crawler.crawler.WebCrawler;
import imag.crawler.parser.BinaryParseData;
import imag.crawler.url.WebURL;
import imag.databaseSql.dao.ImagSQLDao;
import imag.mycrawler.dbaseInfor.NewsImgsInfor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import com.lakeside.data.sqldb.MysqlDataSource;

/*
 * This class shows how you can crawl images on the web and store them in a
 * folder. This is just for demonstration purposes and doesn't scale for large
 * number of images. For crawling millions of images you would need to store
 * downloaded images in a hierarchy of folders
 */
/**
 * @author wxm516
 *
 */
public class ImageCrawler extends WebCrawler {

  private static final Pattern filters = Pattern
      .compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

  private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");

  private static File storageFolder;
  private static String[] crawlDomains;
  private static List<Map<String,Object>>  urlsMapList;

  public static void configure(String[] domain, String storageFolderName) {
    crawlDomains = domain;

    storageFolder = new File(storageFolderName);
    if (!storageFolder.exists()) {
      storageFolder.mkdirs();
    }
  }
  
  public static void configure(String[] domain, String storageFolderName, List<Map<String,Object>>  urlsMap) {
	    crawlDomains = domain;
	    urlsMapList = urlsMap;
	    storageFolder = new File(storageFolderName);
	    if (!storageFolder.exists()) {
	      storageFolder.mkdirs();
	    }
	  }

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    if (filters.matcher(href).matches()) {
      return true;
    }

    if (imgPatterns.matcher(href).matches()) {
      return true;
    }

    for (String domain : crawlDomains) {
      if (href.startsWith(domain)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void visit(Page page) {
	  
    String   strImgUrl = "";
    String   strNewsUrl = ""; // img_url 对应的 news_url;
    String   strQryWord = ""; // img_url 对应的 qry_word;
	  
    strImgUrl = page.getWebURL().getURL();
    System.out.println("strImgUrl: " + strImgUrl);
    
    // We are only interested in processing images which are bigger than 10k;
    if (!imgPatterns.matcher(strImgUrl).matches() ||
        !((page.getParseData() instanceof BinaryParseData) || (page.getContentData().length < (10 * 1024)))) {
      return;
    }

    // get a unique name for storing this image
    String extension = strImgUrl.substring(strImgUrl.lastIndexOf('.'));
    String hashedName = UUID.randomUUID() + extension;
    
    /***************************** NewsImgsInfor  *****************************/
    byte[] bImgData = page.getContentData(); // get the image binary data;
    NewsImgsInfor newsImgsInfor = new NewsImgsInfor();
    newsImgsInfor.setImgUrl(strImgUrl);
    newsImgsInfor.setImgData(bImgData);
    // get the news_url of this img_url;
    for(Map<String,Object> urlsMap:urlsMapList){
    	if(urlsMap.containsKey(strImgUrl)){
    		String strTmp[] = urlsMap.get(strImgUrl).toString().split(",");
    		strQryWord = strTmp[0];  // get qry_word of this img_url;
    		strNewsUrl = strTmp[1];  // get news_url of this img_url;
    	}
    }
    newsImgsInfor.setNewsUrl(strNewsUrl);
    newsImgsInfor.setQryWord(strQryWord);
    /***************************** Save Into DBase  *****************************/
    saveImgIntoDBase(newsImgsInfor);

    /*
    // store image
    String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
    try {
		 Files.write(page.getContentData(), new File(filename)); 
	    } catch (IOException iox) {
	    	System.out.println("error =  " + iox.getMessage());
	    }
    System.out.println("filename: " + filename);
   */
   
    
  }
  
  /**
 * @param newsImgsInfor
 * table name: news_imgs_data;
 * save img binary data into dbase;
 */
public void  saveImgIntoDBase(NewsImgsInfor newsImgsInfor) {
		ImagSQLDao imagSQLDao = new ImagSQLDao();
		MysqlDataSource mysql = imagSQLDao.getDataSource();
		/***************** 添加之前要先判断当前要保存的url是否在数据库中已经下载过,没有下载记录然后才保存  *****************/
		//List<NewsDataInfors> newsInforsList = newsInforsdao.qryNewsBySubDomain(newsDataInfor.getSubDomain());
		//List<NewsDataInfor> newsInforsList = mysqlDao.qryNewsBySubDomain(newsDataInfor.getSubDomain());
		List<String> newsUrlsList = imagSQLDao.qryColumn("img_url","news_imgs_data"); 
		int    nSize = newsUrlsList.size();
		/***************** 查找是否在数据库中已经存在相同url的news信息 *****************/
		int    nFlag = 1;
		for(int iIndex=0;iIndex<nSize;iIndex++){
			String newsUrl = newsUrlsList.get(iIndex);
			if(newsUrl.equals(newsImgsInfor.getNewsUrl())){
				// there is already has a same url in the database;
				nFlag = 2;
				break ;
			}
		}
		if(nFlag == 1){ 
			// there is no  same 'news_url' in the database;
			String sql = "INSERT INTO `imagdatatest`.`news_imgs_data` (`id`,`qry_word`, `news_url`, `img_url`,  `img_data`) VALUES (NULL, :qryWord, :newsUrl, :imgUrl, :imgData);";
			Map[] maps = new Map[1];
			for (int i = 0; i < 1; i++) {
				HashMap<String, Object> paramMap = new HashMap();
				paramMap.put("qryWord",newsImgsInfor.getQryWord());
				paramMap.put("newsUrl", newsImgsInfor.getNewsUrl());
				paramMap.put("imgUrl", newsImgsInfor.getImgUrl());
				paramMap.put("imgData", newsImgsInfor.getImgData());
				
				maps[i] = paramMap;
			}
			imagSQLDao.saveIntoBase(sql, maps);
		}
		
	}
  
  
}