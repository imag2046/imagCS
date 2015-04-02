package imag.crawler.crawler.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import imag.crawler.crawler.entity.DPImgGroup;
import imag.crawler.crawler.source.DeepLearnBaseDao;

/**
 * @author wxm
 *
 */
@Repository
public class DPImgGroupDao extends DeepLearnBaseDao<DPImgGroup, Long>{
	
	/**
	 * return the imgs (includes pos imgs and neg imgs) list of one attribute;
	 * @param groupId
	 * @return
	 */
	public List<DPImgGroup>  queryImgGroupByGroupId(Long groupId){
		//SELECT * FROM `dp_img_group` WHERE `group_id`=2
		String sql = " SELECT * FROM `dp_all_img_group` WHERE `group_id`=:groupId ";
		Map<String,Object> params = this.newParameters();
		params.put("groupId", groupId);
		List<DPImgGroup> list = this.jfind(sql, params, DPImgGroup.class);
		return list;
	}
	
	public List<DPImgGroup>  queryImgGroupByGroupIdFlag(Long groupId, int flag){
		//SELECT * FROM `dp_img_group` WHERE `group_id` :groupId and `pos_neg_flag`:flag
		String sql = " SELECT * FROM `dp_all_img_group` WHERE `group_id` =:groupId and `pos_neg_flag` =:flag ";
		Map<String,Object> params = this.newParameters();
		params.put("groupId", groupId);
		params.put("flag", flag);
		List<DPImgGroup> list = this.jfind(sql, params, DPImgGroup.class);
		return list;
	}
	
	/**
	 * return the topN imgs (includes pos imgs and neg imgs) list of one attribute;
	 * @param groupId
	 * @return
	 */
	public List<DPImgGroup>  queryTopNImgByGroupId(Long groupId, int topN){
		//"SELECT * FROM `dp_img_group` WHERE `group_id` =2 and `pos_neg_flag`=1 order by img_order asc limit 10 ";
		String sql = " SELECT * FROM `dp_all_img_group` WHERE `group_id` =:groupId  and `pos_neg_flag` =:flag  order by img_order asc limit=:topN ";
		Map<String,Object> params = this.newParameters();
		params.put("groupId", groupId);
		params.put("flag", 1);
		params.put("topN", topN);
		List<DPImgGroup> list = this.jfind(sql, params, DPImgGroup.class);
		
		String sql2 = " SELECT * FROM `dp_all_img_group` WHERE `group_id` =:groupId  and `pos_neg_flag` =:flag  order by img_order asc limit=:topN ";
		Map<String,Object> params2 = this.newParameters();
		params2.put("groupId", groupId);
		params2.put("flag", 2);
		params2.put("topN", topN);
		List<DPImgGroup> list2 = this.jfind(sql, params, DPImgGroup.class);
		
		list.addAll(list2);

		return list;
	}
	
	
	
	
	
	
	
	
}