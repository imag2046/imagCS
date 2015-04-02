package imag.crawler.coredb.trans;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.SessionFactory;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.google.common.base.Predicates;
import imag.crawler.coredb.dao.base.SessionFactoryAware;

public class MultipleTransactionInterceptor extends TransactionInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1426348687340710839L;
	
	private static Logger log = LoggerFactory.getLogger(MultipleTransactionInterceptor.class);

	/**
	 * cache the relationship of session-factory with service.
	 */
	private Map<Class<?>,Set<SessionFactory>> serviceSessionFactories = new HashMap<Class<?>,Set<SessionFactory>>();
	
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		// Work out the target class: may be <code>null</code>.
		// The TransactionAttributeSource should be passed the target class
		// as well as the method, which may be from an interface.
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);

		// If the transaction attribute is null, the method is non-transactional.
		final TransactionAttribute txAttr =
				getTransactionAttributeSource().getTransactionAttribute(invocation.getMethod(), targetClass);
		final PlatformTransactionManager tm = determineTransactionManager(txAttr);
		final String joinpointIdentification = methodIdentification(invocation.getMethod(), targetClass);

		if (txAttr == null || tm instanceof MultipleHibernateTransactionManager ) {
			MultipleHibernateTransactionManager mtm = (MultipleHibernateTransactionManager) tm;
			Set<SessionFactory> set = getServiceBindingSessionFactory(targetClass,invocation);
			mtm.setSessionFactory(set);
			TransactionInfo txInfo=null;
			if(set!=null && set.size()>0){
				// Standard transaction demarcation with getTransaction and commit/rollback calls.
				txInfo = createTransactionIfNecessary(mtm, txAttr, joinpointIdentification);
			}
			Object retVal = null;
			try {
				// This is an around advice: Invoke the next interceptor in the chain.
				// This will normally result in a target object being invoked.
				retVal = invocation.proceed();
			}
			catch (Throwable ex) {
				// target invocation exception
				completeTransactionAfterThrowing(txInfo, ex);
				throw ex;
			}
			finally {
				cleanupTransactionInfo(txInfo);
			}
			commitTransactionAfterReturning(txInfo);
			return retVal;
		}
		else {
			throw new RuntimeException("MultipleTransactionInterceptor must work with MultipleHibernateTransactionManager.");
		}
	}
	
	/**
	 * get binding session factory for this service bean.
	 * @param targetClass
	 * @param invocation
	 * @return
	 */
	private Set<SessionFactory> getServiceBindingSessionFactory(final Class<?> targetClass,final MethodInvocation invocation){
		Set<SessionFactory> set = serviceSessionFactories.get(targetClass);
		if(set==null){
			synchronized(targetClass){
				if((set=serviceSessionFactories.get(targetClass))==null){
					set=new HashSet<SessionFactory>();
					Object target = invocation.getThis();
					// trasaction start at DAO Layer
					if(target instanceof SessionFactoryAware){
						SessionFactoryAware sessionFactoryAware= (SessionFactoryAware) target;
						SessionFactory sessionFactory = sessionFactoryAware.getSessionFactory();
						set.add(sessionFactory);
						Set<SessionFactory> daoFoundSession = getBindingSessionFactory(targetClass,target);
						if(daoFoundSession!=null){
							set.addAll(daoFoundSession);
						}
					}else{
						// trasaction started at Service Layer
						Set<Field> allFields = ReflectionUtils.getAllFields(targetClass, Predicates.and(ReflectionUtils.withAnnotation(Autowired.class)));
						for(Field field:allFields){
							try {
								field.setAccessible(true);
								Object object = field.get(target);
								if(object!=null && object instanceof SessionFactoryAware){
									SessionFactoryAware sessionFactoryAware= (SessionFactoryAware) object;
									SessionFactory sessionFactory = sessionFactoryAware.getSessionFactory();
									set.add(sessionFactory);
								}
								Set<SessionFactory> daoFoundSession = getBindingSessionFactory(object.getClass(),object);
								if(daoFoundSession!=null){
									set.addAll(daoFoundSession);
								}
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							
						}
					}
					log.debug("discovered ["+set.size()+"] sessionFactory binding with service ["+targetClass+"]");
					serviceSessionFactories.put(targetClass, set);
				}
			}
		}
		return set;
	}
	
	/**
	 * get binding sessionFactory from other beans.
	 * @param targetClass
	 * @param target
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Set<SessionFactory> getBindingSessionFactory(final Class<?> targetClass,final Object target){
		Set<SessionFactory> set = new HashSet<SessionFactory>();
		if(target!=null){
			Set<Field> allFields = ReflectionUtils.getAllFields(targetClass,Predicates.and(ReflectionUtils.withAnnotation(Autowired.class)));
			set = new HashSet<SessionFactory>();
			for (Field field : allFields) {
				try {
					field.setAccessible(true);
					Object object = field.get(target);
					if (object != null && object instanceof SessionFactoryAware) {
						SessionFactoryAware sessionFactoryAware = (SessionFactoryAware) object;
						SessionFactory sessionFactory = sessionFactoryAware.getSessionFactory();
						set.add(sessionFactory);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
	
			}
		}
		return set;
	}
}
