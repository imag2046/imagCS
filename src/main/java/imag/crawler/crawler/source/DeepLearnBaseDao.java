package imag.crawler.crawler.source;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lakeside.data.sqldb.MysqlDataSource;
import imag.crawler.coredb.dao.base.BaseDao;

/**
 * the base mysql dao for review senze
 * @author qiumm
 *
 * @param <T>
 * @param <PK>
 */
public class DeepLearnBaseDao<T, PK extends Serializable> extends BaseDao<T, PK>{
	
	/**
	 * 采用@Autowired按类型注入MysqlDataSource
	 * 然后从MysqlDataSource中获取初始化baseDao类的datasouce，jdbcTemplate，sessionFactory
	 * @param coreDataSource
	 * @param sessionFactory
	 */
	@Autowired
	public void init(@Qualifier("deeplearn")MysqlDataSource coreDataSource, @Qualifier("deeplearnsf")SessionFactory sessionFactory){
		this.dataSource = coreDataSource.getDataSource();
		this.jdbcTemplate = coreDataSource.getJdbcTemplate();
		this.sessionFactory = sessionFactory;
	}
}
