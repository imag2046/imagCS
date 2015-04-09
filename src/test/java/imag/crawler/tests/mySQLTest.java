/**
 * @author xmwang
 * @ 2015
 */
package imag.crawler.tests;

import imag.databaseSql.dao.ImagSQLDao;

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
		//List<NewsDataInfors> newsInforsList = newsInforsdao.qryNewsBySubDomain(newsDataInfor.getSubDomain());
		//List<NewsDataInfor> newsInforsList = mysqlDao.qryNewsBySubDomain(newsDataInfor.getSubDomain());
		//imagSQLDao.deleteById(1);
	}

}
