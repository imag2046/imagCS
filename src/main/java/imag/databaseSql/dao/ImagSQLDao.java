
package imag.databaseSql.dao;

import java.util.ArrayList;
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
	 * save   data into dbase;
	 * 
	 * @param data
	 */
	public void saveIntoBase(String sql,Map[] data) {
		int[] update = this.getJdbcTemplate().batchUpdate(sql, data);
		//System.out.println(update);
	}
	
	/**
	 * query row data
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
	public List<String>  qryNewsUrlBySubDomain(String subDomain){
		// SELECT * FROM `newsdatatest` WHERE `sub_domain`="qq.com"
		String sql = " SELECT `news_url` FROM `newsdata` WHERE `sub_domain` =:subDomain ";
		Map<String,Object> paramMap = this.newParameters();
		paramMap.put("subDomain", subDomain);
		
		return this.getJdbcTemplate().queryForList(sql,paramMap,String.class);
		
	}
	
	public String getColumnByColumn(String qryColName,String conColName,String conColValue,String tableName){
		// SELECT `news_url` FROM `newsdata` WHERE `sub_domain` ="qq.com"
		StringBuffer sbSql = new StringBuffer();
		Map<String,Object> paramMap = this.newParameters();
		sbSql.append(" select  ").append(qryColName).append("   from   ");
		sbSql.append(tableName).append(" where ");
		sbSql.append(conColName).append(" =:").append(conColName);
		paramMap.put(conColName, conColValue);
		
		return this.getJdbcTemplate().queryForObject(sbSql.toString(), paramMap, String.class);
	}
	
	/**
	 * 以conColName作为查询条件,返回qryColName列的值的lists;
	 * @param qryColName
	 * @param conColName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String>  qryColumnByColumn(String qryColName,String conColName,String conColValue,String tableName){
		// SELECT `news_url` FROM `newsdata` WHERE `sub_domain` ="qq.com"
		StringBuffer sbSql = new StringBuffer();
		Map<String,Object> paramMap = this.newParameters();
		sbSql.append(" select  ").append(qryColName).append("   from   ");
		sbSql.append(tableName).append(" where ");
		sbSql.append(conColName).append(" =:").append(conColName);
		
		paramMap.put(conColName, conColValue);
		
		return this.getJdbcTemplate().queryForList(sbSql.toString(),paramMap,String.class);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<String>  qryColumn(String qryColName,String tableName){
		// sql : " SELECT title FROM `newsdata` WHERE 1 ";
		StringBuffer sbSql = new StringBuffer();
		Map<String,Object> paramMap = this.newParameters();
		sbSql.append(" select  ").append(qryColName).append("   from   ");
		sbSql.append(tableName).append(" where 1");
		
		return this.getJdbcTemplate().queryForList(sbSql.toString(),paramMap,String.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryImgDataInfoByNewsUrl(String strNewsUrl){
		Map<String,Object> map = this.newParameters();
		// sql : SELECT `img_data`,`img_url` FROM `news_imgs_data` WHERE news_url="www.test.com" ;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" SELECT `img_data`,`img_url` FROM `news_imgs_data` WHERE news_url=:newsUrl ");
		Map<String,Object> paramMap = this.newParameters();
		paramMap.put("newsUrl", strNewsUrl);
		
		return this.getJdbcTemplate().queryForList(sbSql.toString(), paramMap);
	}
	
	/**
	 * @param strColumnName
	 * @param strColumnValue
	 * @return 
	 * 返回 一行Map<String,Object> 数据或是 多行的 Map<String,Object> 数据
	 * Map<String,Object>中key为库表中的字段名
	 */
	public List<Map<String,Object>> qryRowDataByColumn(String strColumnName,String strColumnValue){
		Map<String,Object> map = this.newParameters();
		// sql : SELECT `id`,`qry_word`,`news_url`,`pub_time`,`parent_url`,`sub_domain`,`img_urls`,`video_urls`,`title`,`document`,`web_cache` FROM `newsdata` WHERE `id`=100 ;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" SELECT `id`,`qry_word`,`news_url`,`pub_time`,`parent_url`,`sub_domain`,`img_urls`,`video_urls`,`title`,`document`  FROM `newsdata` WHERE  ");
		sbSql.append(strColumnName).append("=:").append(strColumnName);
		Map<String,Object> paramMap = this.newParameters();
		paramMap.put(strColumnName, strColumnValue);
		
		return this.getJdbcTemplate().queryForList(sbSql.toString(), paramMap);
	}
	
	/**
	 * @param tableName
	 * @return
	 * 返回 imgs_url 和 news_url 两列数据
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> qryImgsUrlsByNewsUrl(String tableName){
		Map<String,Object> map = this.newParameters();
		// sql :SELECT`news_url`,`img_urls` FROM `newsdata` WHERE 1=1 ;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" SELECT`news_url`,`img_urls` FROM ");
		sbSql.append(tableName).append(" where 1=:value");
		Map<String,Object> paramMap = this.newParameters();
		paramMap.put("value", "1");
		
		return this.getJdbcTemplate().queryForList(sbSql.toString(), paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public List<byte[]> qryImgDataByNewsUrl(String strNewsUrl){
		Map<String,Object> map = this.newParameters();
		// sql : SELECT `img_data`,`img_url` FROM `news_imgs_data` WHERE news_url="www.test.com" ;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append(" SELECT `img_data` FROM `news_imgs_data` WHERE news_url=:newsUrl ");
		Map<String,Object> paramMap = this.newParameters();
		paramMap.put("newsUrl", strNewsUrl);
		
		return this.getJdbcTemplate().queryForList(sbSql.toString(),paramMap,byte[].class);
	}
	
	
	/**
	 * delete data by table id;
	 * @param newsId
	 */
	@SuppressWarnings("unchecked")
	public void deleteById(long newsId){
		String sql = " DELETE FROM `newsdata` WHERE `id`=:id ";
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
