package imag.crawler.coredb.trans;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.impl.SessionImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.hibernate3.SpringSessionContext;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 多数据连接的事物实现.
 * 此实现自动发现当前上下文中的需要的sessionFactory信息。
 * 
 * @author houdejun
 *
 */
public class MultipleHibernateTransactionManager extends AbstractPlatformTransactionManager  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9151812973139206309L;

	private static final List<SessionFactory> EMPTY_SESSION_FACTORY = new ArrayList<SessionFactory>(); 

	private boolean hibernateManagedSession = false;

	private SQLExceptionTranslator jdbcExceptionTranslator;

	private SQLExceptionTranslator defaultJdbcExceptionTranslator;

	/**
	 * Create a new HibernateTransactionManager instance.
	 * A SessionFactory has to be set to be able to use it.
	 * @see #setSessionFactory
	 */
	public MultipleHibernateTransactionManager() {
	}
	
	/**
	 * Set the SessionFactory that this instance should manage transactions for.
	 * @param set
	 */
	public void setSessionFactory(Set<SessionFactory> set) {
		if(!TransactionSynchronizationManager.hasResource(this)){
			if(set!=null){
				List<SessionFactory> resource = new ArrayList<SessionFactory>(set);
				TransactionSynchronizationManager.bindResource(this, resource);
			}
		}
	}

	/**
	 * Set the SessionFactory that this instance should manage transactions for.
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		List<SessionFactory> resource = (List<SessionFactory>) TransactionSynchronizationManager.getResource(this);
		if(resource==null){
			resource = new ArrayList<SessionFactory>();
			TransactionSynchronizationManager.bindResource(this, resource);
		}
		resource.add(sessionFactory);
	}

	/**
	 * Return the SessionFactory that this instance should manage transactions for.
	 */
	public List<SessionFactory> getSessionFactory() {
	 	List<SessionFactory> resource = (List<SessionFactory>) TransactionSynchronizationManager.getResource(this);
	 	return resource==null?EMPTY_SESSION_FACTORY:resource;
	}

	/**
	 * Set whether to operate on a Hibernate-managed Session instead of a
	 * Spring-managed Session, that is, whether to obtain the Session through
	 * Hibernate's {@link org.hibernate.SessionFactory#getCurrentSession()}
	 * instead of {@link org.hibernate.SessionFactory#openSession()} (with a Spring
	 * {@link org.springframework.transaction.support.TransactionSynchronizationManager}
	 * check preceding it).
	 * <p>Default is "false", i.e. using a Spring-managed Session: taking the current
	 * thread-bound Session if available (e.g. in an Open-Session-in-View scenario),
	 * creating a new Session for the current transaction otherwise.
	 * <p>Switch this flag to "true" in order to enforce use of a Hibernate-managed Session.
	 * Note that this requires {@link org.hibernate.SessionFactory#getCurrentSession()}
	 * to always return a proper Session when called for a Spring-managed transaction;
	 * transaction begin will fail if the <code>getCurrentSession()</code> call fails.
	 * <p>This mode will typically be used in combination with a custom Hibernate
	 * {@link org.hibernate.context.CurrentSessionContext} implementation that stores
	 * Sessions in a place other than Spring's TransactionSynchronizationManager.
	 * It may also be used in combination with Spring's Open-Session-in-View support
	 * (using Spring's default {@link SpringSessionContext}), in which case it subtly
	 * differs from the Spring-managed Session mode: The pre-bound Session will <i>not</i>
	 * receive a <code>clear()</code> call (on rollback) or a <code>disconnect()</code>
	 * call (on transaction completion) in such a scenario; this is rather left up
	 * to a custom CurrentSessionContext implementation (if desired).
	 */
	public void setHibernateManagedSession(boolean hibernateManagedSession) {
		this.hibernateManagedSession = hibernateManagedSession;
	}

	/**
	 * Set the JDBC exception translator for this transaction manager.
	 * <p>Applied to any SQLException root cause of a Hibernate JDBCException that
	 * is thrown on flush, overriding Hibernate's default SQLException translation
	 * (which is based on Hibernate's Dialect for a specific target database).
	 * @param jdbcExceptionTranslator the exception translator
	 * @see java.sql.SQLException
	 * @see org.hibernate.JDBCException
	 * @see org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
	 * @see org.springframework.jdbc.support.SQLStateSQLExceptionTranslator
	 */
	public void setJdbcExceptionTranslator(SQLExceptionTranslator jdbcExceptionTranslator) {
		this.jdbcExceptionTranslator = jdbcExceptionTranslator;
	}

	/**
	 * Return the JDBC exception translator for this transaction manager, if any.
	 */
	public SQLExceptionTranslator getJdbcExceptionTranslator() {
		return this.jdbcExceptionTranslator;
	}

	@Override
	protected Object doGetTransaction() {
		MultipleHibernateTransactionObject mtxObject = new MultipleHibernateTransactionObject();
		List<SessionFactory> sessionFactories = getSessionFactory();
		for(SessionFactory sessionFactory:sessionFactories){
			HibernateTransactionObject txObject = new HibernateTransactionObject();
			txObject.setSessionFactory(sessionFactory);
			txObject.setSavepointAllowed(isNestedTransactionAllowed());
			SessionHolder sessionHolder =
					(SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
			if (sessionHolder != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Found thread-bound Session [" +
							SessionFactoryUtils.toString(sessionHolder.getSession()) + "] for Hibernate transaction");
				}
				txObject.setSessionHolder(sessionHolder);
			}
			else if (this.hibernateManagedSession) {
				try {
					Session session = sessionFactory.getCurrentSession();
					if (logger.isDebugEnabled()) {
						logger.debug("Found Hibernate-managed Session [" +
								SessionFactoryUtils.toString(session) + "] for Spring-managed transaction");
					}
					txObject.setExistingSession(session);
				}
				catch (HibernateException ex) {
					throw new DataAccessResourceFailureException(
							"Could not obtain Hibernate-managed Session for Spring-managed transaction", ex);
				}
			}
			DataSource sfds = SessionFactoryUtils.getDataSource(sessionFactory);
			ConnectionHolder conHolder = (ConnectionHolder)
					TransactionSynchronizationManager.getResource(sfds);
			txObject.setConnectionHolder(conHolder);
			txObject.setDataSource(sfds);
			mtxObject.add(txObject);
		}
		return mtxObject;
	}

	@Override
	protected boolean isExistingTransaction(Object transaction) {
		MultipleHibernateTransactionObject mtxObject = (MultipleHibernateTransactionObject) transaction;
		for(HibernateTransactionObject txObject:mtxObject){
			if((txObject.hasSpringManagedTransaction() ||
					(this.hibernateManagedSession && txObject.hasHibernateManagedTransaction()))){
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		MultipleHibernateTransactionObject mtxObject = (MultipleHibernateTransactionObject) transaction;
		for(HibernateTransactionObject txObject:mtxObject){
			SessionFactory sessionFactory = txObject.getSessionFactory();
			DataSource dataSource = txObject.getDataSource();
			Session session = null;
	
			try {
				if (txObject.getSessionHolder() == null || txObject.getSessionHolder().isSynchronizedWithTransaction()) {
					Session newSession = sessionFactory.openSession();
					if (logger.isDebugEnabled()) {
						logger.debug("Opened new Session [" + SessionFactoryUtils.toString(newSession) +
								"] for Hibernate transaction");
					}
					txObject.setSession(newSession);
				}
	
				session = txObject.getSessionHolder().getSession();
	
				if (isSameConnectionForEntireSession(session)) {
					// We're allowed to change the transaction settings of the JDBC Connection.
					if (logger.isDebugEnabled()) {
						logger.debug(
								"Preparing JDBC Connection of Hibernate Session [" + SessionFactoryUtils.toString(session) + "]");
					}
					Connection con = session.connection();
					Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
					txObject.setPreviousIsolationLevel(previousIsolationLevel);
				}
				else {
					// Not allowed to change the transaction settings of the JDBC Connection.
					if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
						// We should set a specific isolation level but are not allowed to...
						throw new InvalidIsolationLevelException(
								"HibernateTransactionManager is not allowed to support custom isolation levels: " +
								"make sure that its 'prepareConnection' flag is on (the default) and that the " +
								"Hibernate connection release mode is set to 'on_close' (SpringTransactionFactory's default). " +
								"Make sure that your LocalSessionFactoryBean actually uses SpringTransactionFactory: Your " +
								"Hibernate properties should *not* include a 'hibernate.transaction.factory_class' property!");
					}
					if (logger.isDebugEnabled()) {
						logger.debug(
								"Not preparing JDBC Connection of Hibernate Session [" + SessionFactoryUtils.toString(session) + "]");
					}
				}
	
				if (definition.isReadOnly() && txObject.isNewSession()) {
					// Just set to NEVER in case of a new Session for this transaction.
					session.setFlushMode(FlushMode.MANUAL);
				}
	
				if (!definition.isReadOnly() && !txObject.isNewSession()) {
					// We need AUTO or COMMIT for a non-read-only transaction.
					FlushMode flushMode = session.getFlushMode();
					if (flushMode.lessThan(FlushMode.COMMIT)) {
						session.setFlushMode(FlushMode.AUTO);
						txObject.getSessionHolder().setPreviousFlushMode(flushMode);
					}
				}
				Transaction hibTx;
	
				// Register transaction timeout.
				int timeout = determineTimeout(definition);
				if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
					// Use Hibernate's own transaction timeout mechanism on Hibernate 3.1+
					// Applies to all statements, also to inserts, updates and deletes!
					hibTx = session.getTransaction();
					hibTx.setTimeout(timeout);
					hibTx.begin();
				}
				else {
					// Open a plain Hibernate transaction without specified timeout.
					hibTx = session.beginTransaction();
				}
				
				if (logger.isDebugEnabled() && txObject.isNewSession()){
					Connection connection = session.connection();
					int code = connection.hashCode();
					logger.debug("create connection["+code+"] mode ["+connection.isReadOnly()+"]");
				}
				// Add the Hibernate transaction to the session holder.
				txObject.getSessionHolder().setTransaction(hibTx);
	
				// Register the Hibernate Session's JDBC Connection for the DataSource, if set.
				if (dataSource != null && txObject.isNewSessionHolder() && !TransactionSynchronizationManager.hasResource(dataSource)) {
					Connection con = session.connection();
					ConnectionHolder conHolder = new ConnectionHolder(con);
					if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
						conHolder.setTimeoutInSeconds(timeout);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Exposing Hibernate transaction as JDBC transaction [" + con + "]");
					}
					TransactionSynchronizationManager.bindResource(dataSource, conHolder);
					txObject.setConnectionHolder(conHolder);
				}
	
				// Bind the session holder to the thread.
				if (txObject.isNewSessionHolder() && !TransactionSynchronizationManager.hasResource(sessionFactory)) {
					TransactionSynchronizationManager.bindResource(sessionFactory, txObject.getSessionHolder());
				}
				txObject.getSessionHolder().setSynchronizedWithTransaction(true);
			}
	
			catch (Exception ex) {
				if (txObject.isNewSession()) {
					try {
						if (session.getTransaction().isActive()) {
							session.getTransaction().rollback();
						}
					}
					catch (Throwable ex2) {
						logger.debug("Could not rollback Session after failed transaction begin", ex);
					}
					finally {
						SessionFactoryUtils.closeSession(session);
					}
				}
				throw new CannotCreateTransactionException("Could not open Hibernate Session for transaction", ex);
			}
		}
	}

	@Override
	protected Object doSuspend(Object transaction) {
		MultipleHibernateTransactionObject mtxObject = (MultipleHibernateTransactionObject) transaction;
		MultipleSuspendedResourcesHolder msuspendedResourcesHolder =new MultipleSuspendedResourcesHolder();
		for(HibernateTransactionObject txObject:mtxObject){
			DataSource dataSource = txObject.getDataSource();
			SessionFactory sessionFactory = txObject.getSessionFactory();
			txObject.setSessionHolder(null);
			SessionHolder sessionHolder =
					(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
			txObject.setConnectionHolder(null);
			ConnectionHolder connectionHolder = null;
			if (dataSource != null) {
				connectionHolder = (ConnectionHolder) TransactionSynchronizationManager.unbindResource(dataSource);
			}
			SuspendedResourcesHolder e = new SuspendedResourcesHolder(sessionHolder, connectionHolder);
			e.setSessionFactory(sessionFactory);
			e.setDataSource(dataSource);
			msuspendedResourcesHolder.add(e);
		}
		return msuspendedResourcesHolder;
	}

	@Override
	protected void doResume(Object transaction, Object suspendedResources) {
		MultipleSuspendedResourcesHolder msuspendedResourcesHolder =new MultipleSuspendedResourcesHolder();
		for(SuspendedResourcesHolder resourcesHolder :msuspendedResourcesHolder){
			SessionFactory sessionFactory = resourcesHolder.getSessionFactory();
			if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
				// From non-transactional code running in active transaction synchronization
				// -> can be safely removed, will be closed on transaction completion.
				TransactionSynchronizationManager.unbindResource(sessionFactory);
			}
			TransactionSynchronizationManager.bindResource(sessionFactory, resourcesHolder.getSessionHolder());
			if (resourcesHolder.getDataSource() != null) {
				TransactionSynchronizationManager.bindResource(resourcesHolder.getDataSource(), resourcesHolder.getConnectionHolder());
			}
		}
	}

	@Override
	protected void prepareForCommit(DefaultTransactionStatus status) {
		MultipleHibernateTransactionObject mtxObject = (MultipleHibernateTransactionObject) status.getTransaction();
		for(HibernateTransactionObject txObject:mtxObject){
			if (txObject.isNewSession()) {
				Session session = txObject.getSessionHolder().getSession();
				if (!session.getFlushMode().lessThan(FlushMode.COMMIT)) {
					logger.debug("Performing an early flush for Hibernate transaction");
					try {
						session.flush();
					}
					catch (HibernateException ex) {
						throw convertHibernateAccessException(ex,txObject.getDataSource(),txObject.getSessionFactory());
					}
					finally {
						session.setFlushMode(FlushMode.MANUAL);
					}
				}
			}
		}
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		MultipleHibernateTransactionObject mtxObject = (MultipleHibernateTransactionObject) status.getTransaction();
		for(HibernateTransactionObject txObject:mtxObject){
			if (status.isDebug()) {
				logger.debug("Committing Hibernate transaction on Session [" +
						SessionFactoryUtils.toString(txObject.getSessionHolder().getSession()) + "]");
			}
			try {
				txObject.getSessionHolder().getTransaction().commit();
			}
			catch (org.hibernate.TransactionException ex) {
				// assumably from commit call to the underlying JDBC connection
				throw new TransactionSystemException("Could not commit Hibernate transaction", ex);
			}
			catch (HibernateException ex) {
				// assumably failed to flush changes to database
				throw convertHibernateAccessException(ex,txObject.getDataSource(),txObject.getSessionFactory());
			}
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		MultipleHibernateTransactionObject mtxObject = (MultipleHibernateTransactionObject) status.getTransaction();
		for(HibernateTransactionObject txObject:mtxObject){
			if (status.isDebug()) {
				logger.debug("Rolling back Hibernate transaction on Session [" +
						SessionFactoryUtils.toString(txObject.getSessionHolder().getSession()) + "]");
			}
			try {
				txObject.getSessionHolder().getTransaction().rollback();
			}
			catch (org.hibernate.TransactionException ex) {
				throw new TransactionSystemException("Could not roll back Hibernate transaction", ex);
			}
			catch (HibernateException ex) {
				// Shouldn't really happen, as a rollback doesn't cause a flush.
				throw convertHibernateAccessException(ex,txObject.getDataSource(),txObject.getSessionFactory());
			}
			finally {
				if (!txObject.isNewSession() && !this.hibernateManagedSession) {
					// Clear all pending inserts/updates/deletes in the Session.
					// Necessary for pre-bound Sessions, to avoid inconsistent state.
					txObject.getSessionHolder().getSession().clear();
				}
			}
		}
	}

	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status) {
		HibernateTransactionObject txObject = (HibernateTransactionObject) status.getTransaction();
		if (status.isDebug()) {
			logger.debug("Setting Hibernate transaction on Session [" +
					SessionFactoryUtils.toString(txObject.getSessionHolder().getSession()) + "] rollback-only");
		}
		txObject.setRollbackOnly();
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		MultipleHibernateTransactionObject mtxObject = (MultipleHibernateTransactionObject) transaction;
		for(HibernateTransactionObject txObject:mtxObject){
			DataSource dataSource = txObject.getDataSource();
			SessionFactory sessionFactory = txObject.getSessionFactory();
			// Remove the session holder from the thread.
			if (txObject.isNewSessionHolder()) {
				TransactionSynchronizationManager.unbindResource(sessionFactory);
			}

			// Remove the JDBC connection holder from the thread, if exposed.
			if (dataSource != null) {
				TransactionSynchronizationManager.unbindResource(dataSource);
			}
			if(TransactionSynchronizationManager.hasResource(this)){
				// Remove the sessionFactory binding
				TransactionSynchronizationManager.unbindResource(this);
			}

			Session session = txObject.getSessionHolder().getSession();
			if (session.isConnected()
					&& isSameConnectionForEntireSession(session)) {
				// We're running with connection release mode "on_close": We're
				// able to reset
				// the isolation level and/or read-only flag of the JDBC
				// Connection here.
				// Else, we need to rely on the connection pool to perform
				// proper cleanup.
				try {
					Connection con = session.connection();
					DataSourceUtils.resetConnectionAfterTransaction(con,
							txObject.getPreviousIsolationLevel());
				} catch (HibernateException ex) {
					logger.debug("Could not access JDBC Connection of Hibernate Session", ex);
				}
			}

			if (txObject.isNewSession()) {
				if (logger.isDebugEnabled()) {
					logger.debug("Closing Hibernate Session ["
							+ SessionFactoryUtils.toString(session) + "] after transaction");
				}
				SessionFactoryUtils.closeSession(session);
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Not closing pre-bound Hibernate Session ["
							+ SessionFactoryUtils.toString(session) + "] after transaction");
				}
				if (txObject.getSessionHolder().getPreviousFlushMode() != null) {
					session.setFlushMode(txObject.getSessionHolder().getPreviousFlushMode());
				}
				if (!this.hibernateManagedSession) {
					session.disconnect();
				}
			}
			txObject.getSessionHolder().clear();
		}
	}

	/**
	 * Return whether the given Hibernate Session will always hold the same
	 * JDBC Connection. This is used to check whether the transaction manager
	 * can safely prepare and clean up the JDBC Connection used for a transaction.
	 * <p>Default implementation checks the Session's connection release mode
	 * to be "on_close". Unfortunately, this requires casting to SessionImpl,
	 * as of Hibernate 3.1. If that cast doesn't work, we'll simply assume
	 * we're safe and return <code>true</code>.
	 * @param session the Hibernate Session to check
	 * @see org.hibernate.impl.SessionImpl#getConnectionReleaseMode()
	 * @see org.hibernate.ConnectionReleaseMode#ON_CLOSE
	 */
	protected boolean isSameConnectionForEntireSession(Session session) {
		if (!(session instanceof SessionImpl)) {
			// The best we can do is to assume we're safe.
			return true;
		}
		ConnectionReleaseMode releaseMode = ((SessionImpl) session).getConnectionReleaseMode();
		return ConnectionReleaseMode.ON_CLOSE.equals(releaseMode);
	}


	/**
	 * Convert the given HibernateException to an appropriate exception
	 * from the <code>org.springframework.dao</code> hierarchy.
	 * <p>Will automatically apply a specified SQLExceptionTranslator to a
	 * Hibernate JDBCException, else rely on Hibernate's default translation.
	 * @param ex HibernateException that occured
	 * @return a corresponding DataAccessException
	 * @see SessionFactoryUtils#convertHibernateAccessException
	 * @see #setJdbcExceptionTranslator
	 */
	protected DataAccessException convertHibernateAccessException(HibernateException ex,DataSource dataSource,SessionFactory sessionFactory) {
		if (getJdbcExceptionTranslator() != null && ex instanceof JDBCException) {
			return convertJdbcAccessException((JDBCException) ex, getJdbcExceptionTranslator());
		}
		else if (GenericJDBCException.class.equals(ex.getClass())) {
			return convertJdbcAccessException((GenericJDBCException) ex, getDefaultJdbcExceptionTranslator(dataSource,sessionFactory));
		}
		return SessionFactoryUtils.convertHibernateAccessException(ex);
	}

	/**
	 * Convert the given Hibernate JDBCException to an appropriate exception
	 * from the <code>org.springframework.dao</code> hierarchy, using the
	 * given SQLExceptionTranslator.
	 * @param ex Hibernate JDBCException that occured
	 * @param translator the SQLExceptionTranslator to use
	 * @return a corresponding DataAccessException
	 */
	protected DataAccessException convertJdbcAccessException(JDBCException ex, SQLExceptionTranslator translator) {
		return translator.translate("Hibernate flushing: " + ex.getMessage(), ex.getSQL(), ex.getSQLException());
	}

	/**
	 * Obtain a default SQLExceptionTranslator, lazily creating it if necessary.
	 * <p>Creates a default
	 * {@link org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator}
	 * for the SessionFactory's underlying DataSource.
	 */
	protected synchronized SQLExceptionTranslator getDefaultJdbcExceptionTranslator(DataSource dataSource,SessionFactory sessionFactory) {
		if (this.defaultJdbcExceptionTranslator == null) {
			if (dataSource != null) {
				this.defaultJdbcExceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
			}
			else {
				this.defaultJdbcExceptionTranslator = SessionFactoryUtils.newJdbcExceptionTranslator(sessionFactory);
			}
		}
		return this.defaultJdbcExceptionTranslator;
	}

	/**
	 * 存储多个HibernateTransactionObject
	 * @author houdejun
	 *
	 */
	private static class MultipleHibernateTransactionObject extends ArrayList<HibernateTransactionObject> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8103275960243534224L;
		
	}
	

	/**
	 * Hibernate transaction object, representing a SessionHolder.
	 * Used as transaction object by HibernateTransactionManager.
	 */
	private class HibernateTransactionObject extends JdbcTransactionObjectSupport {

		private SessionHolder sessionHolder;

		private boolean newSessionHolder;

		private boolean newSession;

		private SessionFactory sessionFactory;
		
		private DataSource dataSource;
		
		public void setSession(Session session) {
			this.sessionHolder = new SessionHolder(session);
			this.newSessionHolder = true;
			this.newSession = true;
		}

		public void setExistingSession(Session session) {
			this.sessionHolder = new SessionHolder(session);
			this.newSessionHolder = true;
			this.newSession = false;
		}

		public void setSessionHolder(SessionHolder sessionHolder) {
			this.sessionHolder = sessionHolder;
			this.newSessionHolder = false;
			this.newSession = false;
		}

		public SessionHolder getSessionHolder() {
			return this.sessionHolder;
		}

		public boolean isNewSessionHolder() {
			return this.newSessionHolder;
		}

		public boolean isNewSession() {
			return this.newSession;
		}

		public boolean hasSpringManagedTransaction() {
			return (this.sessionHolder != null && this.sessionHolder.getTransaction() != null);
		}

		public boolean hasHibernateManagedTransaction() {
			return (this.sessionHolder != null && this.sessionHolder.getSession().getTransaction().isActive());
		}

		public void setRollbackOnly() {
			this.sessionHolder.setRollbackOnly();
			if (hasConnectionHolder()) {
				getConnectionHolder().setRollbackOnly();
			}
		}

		public boolean isRollbackOnly() {
			return this.sessionHolder.isRollbackOnly() ||
					(hasConnectionHolder() && getConnectionHolder().isRollbackOnly());
		}
		
		public SessionFactory getSessionFactory() {
			return sessionFactory;
		}

		public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}
		
		public DataSource getDataSource() {
			return dataSource;
		}

		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}

		public void flush() {
			try {
			    this.sessionHolder.getSession().flush();
			}
			catch (HibernateException ex) {
				throw convertHibernateAccessException(ex,this.dataSource,this.sessionFactory);
			}
		}
	}

	/**
	 * 存储多个SuspendedResourcesHolder
	 * @author houdejun
	 *
	 */
	private static class MultipleSuspendedResourcesHolder extends ArrayList<SuspendedResourcesHolder> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3370005583186420292L;
		
	}
	
	/**
	 * Holder for suspended resources.
	 * Used internally by <code>doSuspend</code> and <code>doResume</code>.
	 */
	private static class SuspendedResourcesHolder {

		private final SessionHolder sessionHolder;

		private final ConnectionHolder connectionHolder;
		
		private SessionFactory sessionFactory;
		
		private DataSource dataSource;

		private SuspendedResourcesHolder(SessionHolder sessionHolder, ConnectionHolder conHolder) {
			this.sessionHolder = sessionHolder;
			this.connectionHolder = conHolder;
		}

		private SessionHolder getSessionHolder() {
			return this.sessionHolder;
		}

		private ConnectionHolder getConnectionHolder() {
			return this.connectionHolder;
		}

		public SessionFactory getSessionFactory() {
			return sessionFactory;
		}

		public void setSessionFactory(SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}

		public DataSource getDataSource() {
			return dataSource;
		}

		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
		}
	}
}
