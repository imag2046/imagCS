
package imag.databaseSql;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.lakeside.data.sqldb.MysqlDataSource;

/**
 * @author xmwang
 * @ 2015
 */
public class ImagSQLDao {
	public MysqlDataSource getDataSource(){
		return ImagSQLDataSource.get();
	}

	public NamedParameterJdbcTemplate getJdbcTemplate(){
		return ImagSQLDataSource.get().getJdbcTemplate();
	}
	
	public MapSqlParameterSource parseMap2ParmSource(Map<String,Object> data){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();  
		for(Entry<String,Object> e:data.entrySet()){
			  namedParameters.addValue(e.getKey(), e.getValue());  
		} 
		return namedParameters;
	}
	/**
	 * save relational data
	 * 
	 * @param data
	 */
	public void execute(String sql,Map[] data) {
		int[] update = this.getJdbcTemplate().batchUpdate(sql, data);
		System.out.println(update);
	}
	/**
	 * save relational data
	 * 
	 * @param data
	 */
	public List<Map<String, Object>> queryForList(String sql,Map<String, ?> paramMap) {
		return this.getJdbcTemplate().queryForList(sql, paramMap);
	}

}
