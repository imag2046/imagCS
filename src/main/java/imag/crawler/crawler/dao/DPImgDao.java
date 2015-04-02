package imag.crawler.crawler.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import imag.crawler.crawler.entity.DPImg;
import imag.crawler.crawler.source.DeepLearnBaseDao;


/**
 * @author wxm
 *
 */
@Repository
public class DPImgDao extends DeepLearnBaseDao<DPImg, Long>{
	
	/**
	 * return the img url with the input imgId;
	 * @param imgId
	 * @return
	 */
	public DPImg queryTag(Long imgId){
		return super.get(imgId);
	}
	
	public List<DPImg> getAll(){
		return super.getAll();
	}
}