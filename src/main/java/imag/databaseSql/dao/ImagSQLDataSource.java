
package imag.databaseSql.dao;

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author xmwang
 * @ 2015
 */
public class ImagSQLDataSource {
	private static MysqlDataSource dataSource;
	public static synchronized MysqlDataSource get(){
		if(dataSource == null){
			// 测试时使用的库表；
			//String jdbcurl = "jdbc:mysql://127.0.0.1:3306/imagdatatest?useUnicode=true&amp;characterEncoding=UTF-8&amp;charSet=UTF-8";
			// 正式时使用的库表；
			String jdbcurl = "jdbc:mysql://127.0.0.1:3306/imagdata?useUnicode=true&amp;characterEncoding=UTF-8&amp;charSet=UTF-8";
			String userName = "root";
			String password = "wxm123456";
			dataSource = new MysqlDataSource(jdbcurl, userName, password);
		}
		return dataSource;
	}

}
