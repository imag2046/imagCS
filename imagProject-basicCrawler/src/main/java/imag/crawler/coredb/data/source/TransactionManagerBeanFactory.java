package imag.crawler.coredb.data.source;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import imag.crawler.coredb.trans.MultipleHibernateTransactionManager;

/**
 *     
    <!-- 分布式事务,控制多个连接下的事物处理 -->
	<bean id="jotm" class="org.springframework.transaction.jta.JotmFactoryBean" />  
    <bean id="transactionManager" class="org.springframework.transaction.jta.JtaTransactionManager">  
        <property name="userTransaction" ref="jotm" />  
    </bean>  
 * 
 * 
 * @author  
 *
 */
@Component
public class TransactionManagerBeanFactory{
	
	private static PlatformTransactionManager transactionManager=null; 
	private static Object sync = new Object();
	private static final String BEAN_NAME = "transactionManager";
	
	private PlatformTransactionManager getTransactionManagerAsNeed(){
		if(transactionManager==null){
			synchronized(sync){
				if(transactionManager==null){
					transactionManager = new MultipleHibernateTransactionManager();
				}
			}
		}
		return transactionManager;
	}

	@Bean(name=BEAN_NAME)
	@Lazy
	public PlatformTransactionManager getTransactionManager() throws Exception {
		return getTransactionManagerAsNeed();
	}
}
