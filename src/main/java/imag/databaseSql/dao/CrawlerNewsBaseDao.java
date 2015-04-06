
package imag.databaseSql.dao;

import imag.crawler.coredb.dao.base.BaseDao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lakeside.data.sqldb.MysqlDataSource;



/**
 * the base mysql dao for crawler news
 * @author xmwang
 * @ 2015
 */
public class CrawlerNewsBaseDao<T, PK extends Serializable> extends BaseDao<T, PK> {
	/**
	 * 采用@Autowired按类型注入MysqlDataSource
	 * 然后从MysqlDataSource中获取初始化baseDao类的datasouce，jdbcTemplate，sessionFactory
	 * @param coreDataSource
	 * @param sessionFactory
	 */
	@Autowired
	public void init(@Qualifier("newsdatatest")MysqlDataSource coreDataSource, @Qualifier("newsdatatestsf")SessionFactory sessionFactory){
		this.dataSource = coreDataSource.getDataSource();
		this.jdbcTemplate = coreDataSource.getJdbcTemplate();
		this.sessionFactory = sessionFactory;
	}

}
