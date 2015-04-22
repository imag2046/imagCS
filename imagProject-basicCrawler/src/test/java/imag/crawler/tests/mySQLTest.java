/**
 * @author xmwang
 * @ 2015
 */
package imag.crawler.tests;

import imag.databaseSql.dao.ImagSQLDao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.io.Files;
import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author wxm516
 *
 */
public class mySQLTest {
	
	@Test
	public void deleteTest(){
//		ImagSQLDao imagSQLDao = new ImagSQLDao();
//		//MysqlDataSource mysql = imagSQLDao.getDataSource();
//		/***************** 添加之前要先判断当前要保存的url是否在数据库中已经下载过,没有下载记录然后才保存  *****************/
//		//imagSQLDao.deleteById(1);
//		//List<String> list = imagSQLDao.qryColumnByColumn("news_url", "sub_domain", "qq.com", "newsdatatest");
//		
//		/***************** 从数据库中读取数据并写成图片文件   测试 OK *****************/
//		List<byte[]> list = imagSQLDao.qryImgDataByNewsUrl("www.test.com");
//		try {
//			 Files.write(list.get(0), new File("src/main/resources/dataCrawl/imagesCrawl/test.jpg")); 
//		    } catch (IOException iox) {
//		    	System.out.println("error =  " + iox.getMessage());
//		    }
//		
//		
//		System.out.println("list.size = " + list.size());
		
//		 ImagSQLDao imagSQLDao = new ImagSQLDao();
//			MysqlDataSource mysql = imagSQLDao.getDataSource();
//			List<String> list = imagSQLDao.qryColumn("img_urls", "newsdatatest");
//			
//			List<Map<String,Object>>  urlsMap = new ArrayList<Map<String,Object>>();  // <news_url,imgs_url> list;
//			int i = 0;
//			for(i=0; i<list.size(); i++){
//				String str = list.get(i);
//				if(!str.equals("NULL")){
//					// get the news_url of these img_urls;
//					String newsUrl = imagSQLDao.getColumnByColumn("news_url", "img_urls", str, "newsdatatest");
//					System.out.println("newsUrl = " + newsUrl);
//					String[] urls = str.split(";");
//					
//					
//					
//					//Map<String,Object> map = new HashMap<String,Object>();
//					
//				}
//			}
		
		ImagSQLDao imagSQLDao = new ImagSQLDao();
		
		List<Map<String, Object>> list = imagSQLDao.qryRowDataByColumn("news_url", "http://news.qq.com/a/20140118/005550.htm");
		
		int i=0;
		for(i = 0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			System.out.println("id = " + map.get("id"));
		}
		
		
		
		
	}

}
