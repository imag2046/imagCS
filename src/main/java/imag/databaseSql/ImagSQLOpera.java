
package imag.databaseSql;

import java.util.Map;

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author xmwang
 * @ 2015
 */
public class ImagSQLOpera {
	
	
	// save into mysql with sql sentence & data;
	public void saveIntoDatabase(String sql,Map[] data){
		//resource;
		ImagSQLDao imagSQLdao = new ImagSQLDao();
		MysqlDataSource mysql = imagSQLdao.getDataSource();
		
		imagSQLdao.execute(sql, data);
		
	}
	
	
	
	
	
	

}
