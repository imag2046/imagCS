/**
 * @author xmwang
 * @ 2015
 */
package imag.crawler.tests;

import imag.databaseSql.dao.ImagSQLDao;

import java.util.List;

import org.junit.Test;

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author wxm516
 *
 */
public class mySQLTest {
	
	@Test
	public void deleteTest(){
		ImagSQLDao imagSQLDao = new ImagSQLDao();
		MysqlDataSource mysql = imagSQLDao.getDataSource();
		/***************** 添加之前要先判断当前要保存的url是否在数据库中已经下载过,没有下载记录然后才保存  *****************/
		//imagSQLDao.deleteById(1);
		List<String> list = imagSQLDao.qryColumnByColumn("news_url", "sub_domain", "qq.com", "newsdatatest");
		
		System.out.println("list.size = " + list.size());
		
	}

}
