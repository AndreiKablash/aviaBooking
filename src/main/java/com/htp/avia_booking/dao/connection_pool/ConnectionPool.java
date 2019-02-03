package com.htp.avia_booking.dao.connection_pool;

import org.apache.log4j.Logger;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

/**
 * Class is designed for working with database connections
 * Contains two queues with connections that are used and free connections
 * Number of connections and other parameters necessary for correctly work
 * must be specified in property file
 */
public final class ConnectionPool implements ConnectionPoolInterface {
	private BlockingQueue<Connection> connectionQueue;
	private BlockingQueue<Connection> workingQueue;
	
	private String driverName;
	private String databaseURL;
	private String databaseName;
	private String databaseLogin;
	private String databasePassword;
	private int poolSize;
	
	private static final ConnectionPool instance = new ConnectionPool();
	private static final int DEFAULT_POOL_SIZE = 5;
	private static final Logger LOGGER_ROOT = Logger.getRootLogger();
	
	private ConnectionPool() {
		DatabaseConfigManager manager = DatabaseConfigManager.getInstance();
		driverName = manager.getProperty(DatabaseConfigManager.DATABASE_DRIVER_NAME);
		databaseURL = manager.getProperty(DatabaseConfigManager.DATABASE_URL);
		databaseName = manager.getProperty(DatabaseConfigManager.DATABASE_NAME);
		databaseLogin = manager.getProperty(DatabaseConfigManager.DATABASE_LOGIN);
		databasePassword = manager.getProperty(DatabaseConfigManager.DATABASE_PASSWORD);
		
		try {
			poolSize = Integer.valueOf(manager.getProperty(DatabaseConfigManager.DATABASE_POOL_SIZE));
		} catch (NumberFormatException e) {
			LOGGER_ROOT.error("Size of pull is bad!", e);
			poolSize = DEFAULT_POOL_SIZE;
		}
	}
	
	public static ConnectionPool getInstance() {
		return instance;
	}


    /**
     * Initialize queues with new free connections number of which is specified in property file
     * @throws ConnectionPoolException when appear errors which associated with incorrect reading from file or driver loading
     */
	public void init() throws ConnectionPoolException {

		try {
			Class.forName(driverName);
			connectionQueue = new ArrayBlockingQueue<>(poolSize);
			workingQueue = new ArrayBlockingQueue<>(poolSize);
			for(int i = 0; i < poolSize; i++) {
				Connection connection = DriverManager.getConnection(databaseURL + databaseName, databaseLogin, databasePassword);
				ConnectionWrap connectionWrap = new ConnectionWrap(connection);
				connectionQueue.add(connectionWrap);
			}		
		} catch (SQLException e) {
			throw new ConnectionPoolException("SQLException", e);
		} catch (ClassNotFoundException e) {
			throw new ConnectionPoolException("ClassNotFoundException", e);
		}
	}

    /**
     * Close connection pool, make empty queues with connections
     * @throws ConnectionPoolException when appear errors which associated with incorrect reading from file
     */
	public void dispose() throws ConnectionPoolException {
		clearConnectionQueue();
	}

	private void clearConnectionQueue() {
		closeConnectionsQueue(workingQueue);
		closeConnectionsQueue(connectionQueue);
	}
	
	private void closeConnectionsQueue(
			BlockingQueue<Connection> conQueue) {
		Connection connection;
		while((connection = conQueue.poll()) != null) {
			try {
				if(!connection.getAutoCommit()) {
					connection.rollback();
				}
				((ConnectionWrap)connection).reallyClose();
			} catch (SQLException e) {
				LOGGER_ROOT.error("Close connection is not available!", e);
			}	
		}
	}

    /**
     * Method put connection from free-connection-queue to work-connection-queue
     * and return this connection
     *
     * @return connection
     * @throws ConnectionPoolException when connection interrupted while waiting
     */
	public Connection getConnection() throws ConnectionPoolException {
		Connection connect;
		try {
			connect = connectionQueue.take();
			workingQueue.add(connect);
		} catch(InterruptedException e) {
			throw new ConnectionPoolException("Error connect", e);
		}
		return connect;
	}

	/**
	 * Put connection in free connection-queue from working queue
	 */
	public void putConnection() {	
		connectionQueue.add(workingQueue.poll());
	}
	
	private class ConnectionWrap implements Connection {
		private Connection connection;
		
		public ConnectionWrap(Connection connect) throws SQLException{
			this.connection = connect;
			this.connection.setAutoCommit(true);
		}
		
		public void reallyClose() throws SQLException {
			this.connection.close();
		}
		
		@Override
		public boolean isWrapperFor(Class<?> arg0) throws SQLException {
			return connection.isWrapperFor(arg0);
		}

		@Override
		public <T> T unwrap(Class<T> arg0) throws SQLException {
			return connection.unwrap(arg0);
		}

		@Override
		public void abort(Executor arg0) throws SQLException {
			connection.abort(arg0);
		}

		@Override
		public void clearWarnings() throws SQLException {
			connection.clearWarnings();
		}

        /**
         * Method not really close connection.
         * Method sets the connection settings and returns to the original connection to queue.
         *
         * @throws SQLException when one of methods calling in close connection
         */
		@Override
		public void close() throws SQLException {
			
			if(connection.isClosed()) {
				throw new SQLException("Closed connection is never be closed repeatly!");
			}

			if(connection.isReadOnly()) {
				connection.setReadOnly(false);
			}

			if(!connection.getAutoCommit()) {
				connection.setAutoCommit(true);
			}
			putConnection();
		}

		@Override
		public void commit() throws SQLException {
			connection.commit();		
		}

		@Override
		public Array createArrayOf(String arg0, Object[] arg1)
				throws SQLException {
			
			return connection.createArrayOf(arg0, arg1);
		}

		@Override
		public Blob createBlob() throws SQLException {
			return connection.createBlob();
		}

		@Override
		public Clob createClob() throws SQLException {
			return connection.createClob();
		}

		@Override
		public NClob createNClob() throws SQLException {
			return connection.createNClob();
		}

		@Override
		public SQLXML createSQLXML() throws SQLException {
			return connection.createSQLXML();
		}

		@Override
		public Statement createStatement() throws SQLException {
			return connection.createStatement();
		}

		@Override
		public Statement createStatement(int arg0, int arg1)
				throws SQLException {		
			return connection.createStatement(arg0, arg1);
		}

		@Override
		public Statement createStatement(int arg0, int arg1, int arg2)
				throws SQLException {	
			return connection.createStatement(arg0, arg1, arg2);
		}

		@Override
		public Struct createStruct(String arg0, Object[] arg1)
				throws SQLException {
			return connection.createStruct(arg0, arg1);
		}

		@Override
		public boolean getAutoCommit() throws SQLException {
			return connection.getAutoCommit();
		}

		@Override
		public String getCatalog() throws SQLException {
			return connection.getCatalog();
		}

		@Override
		public Properties getClientInfo() throws SQLException {
			return connection.getClientInfo();
		}

		@Override
		public String getClientInfo(String arg0) throws SQLException {
			return connection.getClientInfo(arg0);
		}

		@Override
		public int getHoldability() throws SQLException {
			return connection.getHoldability();
		}

		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			return connection.getMetaData();
		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			return connection.getHoldability();
		}

		@Override
		public String getSchema() throws SQLException {
			return connection.getSchema();
		}

		@Override
		public int getTransactionIsolation() throws SQLException {
			return connection.getTransactionIsolation();
		}

		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return connection.getTypeMap();
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			return connection.getWarnings();
		}

		@Override
		public boolean isClosed() throws SQLException {
			return connection.isClosed();
		}

		@Override
		public boolean isReadOnly() throws SQLException {
			return connection.isReadOnly();
		}

		@Override
		public boolean isValid(int arg0) throws SQLException {
			return connection.isValid(arg0);
		}

		@Override
		public String nativeSQL(String arg0) throws SQLException {
			return connection.nativeSQL(arg0);
		}

		@Override
		public CallableStatement prepareCall(String arg0) throws SQLException {
			return connection.prepareCall(arg0);
		}

		@Override
		public CallableStatement prepareCall(String arg0, int arg1, int arg2)
				throws SQLException {
			return connection.prepareCall(arg0, arg1, arg2);
		}

		@Override
		public CallableStatement prepareCall(String arg0, int arg1, int arg2,
				int arg3) throws SQLException {
			return connection.prepareCall(arg0, arg1, arg2, arg3);
		}

		@Override
		public PreparedStatement prepareStatement(String arg0)
				throws SQLException {
			return connection.prepareStatement(arg0);
		}

		@Override
		public PreparedStatement prepareStatement(String arg0, int arg1)
				throws SQLException {
			return connection.prepareStatement(arg0, arg1);
		}

		@Override
		public PreparedStatement prepareStatement(String arg0, int[] arg1)
				throws SQLException {
			return connection.prepareStatement(arg0, arg1);
		}

		@Override
		public PreparedStatement prepareStatement(String arg0, String[] arg1)
				throws SQLException {
			return connection.prepareStatement(arg0, arg1);
		}

		@Override
		public PreparedStatement prepareStatement(String arg0, int arg1,
				int arg2) throws SQLException {
			return connection.prepareStatement(arg0, arg1, arg2);
		}

		@Override
		public PreparedStatement prepareStatement(String arg0, int arg1,
				int arg2, int arg3) throws SQLException {
			return connection.prepareStatement(arg0, arg1, arg2, arg3);
		}

		@Override
		public void releaseSavepoint(Savepoint arg0) throws SQLException {
			connection.releaseSavepoint(arg0);
		}

		@Override
		public void rollback() throws SQLException {
			connection.rollback();
		}

		@Override
		public void rollback(Savepoint arg0) throws SQLException {
			connection.rollback(arg0);
		}

		@Override
		public void setAutoCommit(boolean arg0) throws SQLException {
			connection.setAutoCommit(arg0);
		}

		@Override
		public void setCatalog(String arg0) throws SQLException {
			connection.setCatalog(arg0);		
		}

		@Override
		public void setClientInfo(Properties arg0)
				throws SQLClientInfoException {
			connection.setClientInfo(arg0);	
		}

		@Override
		public void setClientInfo(String arg0, String arg1)
				throws SQLClientInfoException {
			connection.setClientInfo(arg0, arg1);
		}

		@Override
		public void setHoldability(int arg0) throws SQLException {
			connection.setHoldability(arg0);
		}

		@Override
		public void setNetworkTimeout(Executor arg0, int arg1)
				throws SQLException {
			connection.setNetworkTimeout(arg0, arg1);
		}

		@Override
		public void setReadOnly(boolean arg0) throws SQLException {
			connection.setReadOnly(arg0);
		}

		@Override
		public Savepoint setSavepoint() throws SQLException {
			return connection.setSavepoint();
		}

		@Override
		public Savepoint setSavepoint(String arg0) throws SQLException {
			return connection.setSavepoint(arg0);
		}

		@Override
		public void setSchema(String arg0) throws SQLException {
			connection.setSchema(arg0);
		}

		@Override
		public void setTransactionIsolation(int arg0) throws SQLException {
			connection.setTransactionIsolation(arg0);
		}

		@Override
		public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException {
			connection.setTypeMap(arg0);
		}
	}
}
