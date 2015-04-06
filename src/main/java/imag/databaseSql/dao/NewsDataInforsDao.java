/**
 * @author xmwang
 * @ 2015
 */
package imag.databaseSql.dao;

import java.util.List;
import java.util.Map;

import imag.databaseSql.entity.NewsDataInfors;



/**
 * @author wxm516
 *
 */
public class NewsDataInforsDao extends CrawlerNewsBaseDao<NewsDataInfors, Long>{
	
	/* 
	 * 取得全部的数据;
	 */
	public List<NewsDataInfors> getAll(){
		return super.getAll();
	}

}
