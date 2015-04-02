package imag.crawler.tests;

import com.lakeside.data.sqldb.MysqlDataSource;



public class MySQLDataSource {

	private static MysqlDataSource dataSource;
	public static synchronized MysqlDataSource get(){
		if(dataSource == null){
			String jdbcurl = "jdbc:mysql://172.29.32.33:3306/dp_test?useUnicode=true&amp;characterEncoding=UTF-8&amp;charSet=UTF-8";
			String userName = "wxm";
			String password = "123456";
			dataSource = new MysqlDataSource(jdbcurl, userName, password);
		}
		return dataSource;
	}
	
}