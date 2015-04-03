
package imag.databaseSql;

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author xmwang
 * @ 2015
 */
public class ImagSQLDataSource {
	private static MysqlDataSource dataSource;
	public static synchronized MysqlDataSource get(){
		if(dataSource == null){
			String jdbcurl = "jdbc:mysql://127.0.0.1:3306/dp_test?useUnicode=true&amp;characterEncoding=UTF-8&amp;charSet=UTF-8";
			String userName = "wxm";
			String password = "123456";
			dataSource = new MysqlDataSource(jdbcurl, userName, password);
		}
		return dataSource;
	}

}