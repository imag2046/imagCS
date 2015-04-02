package imag.crawler.crawler.source;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.stereotype.Component;

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * 
 * management data source
 * 
 * @author hdj
 */
@Component
public class DeepLearnDataSource {

	protected Logger log= LoggerFactory.getLogger(this.getClass());
	
	private static MysqlDataSource dataSource = null;
	
	private static AnnotationSessionFactoryBean sessionFactory = null;
	
	@Autowired
	private SenseDataBaseConfig dbConfig;
	
	@Autowired
	public void init(){
		String jdbcurl = dbConfig.getConfig("deeplearn.jdbc.url");
		String userName = dbConfig.getConfig("deeplearn.jdbc.username");
		String password = dbConfig.getConfig("deeplearn.jdbc.password");
		dataSource  = new MysqlDataSource(jdbcurl, userName, password);
		dataSource.setDefaultReadOnly(false);
		sessionFactory = new AnnotationSessionFactoryBean();
		sessionFactory.setDataSource(dataSource.getDataSource());
		sessionFactory.setPackagesToScan(dbConfig.getConfig("deeplearn.entity.packagesToScan").split(",") );
		Properties userHibProperties = this.hibernateProperties();
		userHibProperties.put("hibernate.hbm2ddl.auto", "update");
		sessionFactory.setHibernateProperties(userHibProperties);
		log.info("*****Congratulations! We success to load DataSource & SessionFactory of mysql sense_deeplearn. *****");
	}

	@Bean
	@Lazy
	@Qualifier("deeplearn")
	public  MysqlDataSource get() {
		return dataSource;
	}
	
	/**
	 * 定义live sessionfactory bean
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Lazy
	@Qualifier("deeplearnsf")
	public SessionFactory getManageSessionFactory() throws Exception{
		sessionFactory.afterPropertiesSet();
		return sessionFactory.getObject();
	}
	
	protected Properties hibernateProperties() {
        return new Properties() {
            /**
			 * 
			 */
			{
                this.put("persistence.dialect", "org.hibernate.dialect.MySQL5Dialect");
                this.put("hibernate.show_sql", "false");
            }
        };
    }
}
