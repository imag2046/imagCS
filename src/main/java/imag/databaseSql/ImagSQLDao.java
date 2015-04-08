
package imag.databaseSql;

import java.util.HashMap;
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

@SuppressWarnings("unchecked")
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
	 * save  data
	 * 
	 * @param data
	 */
	public void execute(String sql,Map[] data) {
		int[] update = this.getJdbcTemplate().batchUpdate(sql, data);
		//System.out.println(update);
	}
	/**
	 * query data
	 * 
	 * @param data
	 */
	public List<Map<String, Object>> queryForList(String sql,Map<String, ?> paramMap) {
		return this.getJdbcTemplate().queryForList(sql, paramMap);
	}
	
	/**
	 * qry the 'new_url' column with the condation of 'sub_Domain';
	 * @param subDomain
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String>  qryNewsBySubDomain(String subDomain){
		// SELECT * FROM `newsdatatest` WHERE `sub_domain`="qq.com"
		String sql = " SELECT `news_url` FROM `newsdatatest` WHERE `sub_domain` =:subDomain ";
		Map<String,Object> paramMap = this.newParameters();
		paramMap.put("subDomain", subDomain);
		
		return this.getJdbcTemplate().queryForList(sql,paramMap,String.class);
		
	}
	/**
	 * delete data by table id;
	 * @param newsId
	 */
	@SuppressWarnings("unchecked")
	public void deleteById(long newsId){
		String sql = " DELETE FROM `newsdatatest` WHERE `id`=:id ";
		Map<String,Object> paramMap = this.newParameters();
		paramMap.put("id", newsId);
		
		int temp = this.getJdbcTemplate().update(sql, paramMap);
	
	}

	
	/**
	 * function: Map<String,Object> = new HashMap<String,Object>();
	 * @return
	 */
	protected Map<String, Object> newParameters(){
		return new HashMap<String,Object>();
	}
	
	

}
