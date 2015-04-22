package imag.crawler.coredb.dao.base;

import org.hibernate.SessionFactory;

/**
 * SessionFactory 设置访问接口
 * 
 * @author houdejun
 *
 * @param <T>
 * @param <PK>
 */
public interface SessionFactoryAware {

	/**
	 * 取得sessionFactory.
	 */
	public SessionFactory getSessionFactory();

	/**
	 * 设置sessionFactory
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory);

}