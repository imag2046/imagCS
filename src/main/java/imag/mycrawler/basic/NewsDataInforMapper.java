/**
 * @author xmwang
 * @ 2015
 */
package imag.mycrawler.basic;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author wxm516
 *
 */
public class NewsDataInforMapper implements RowMapper {
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		NewsDataInfor infor = new NewsDataInfor();
		infor.setId(rs.getLong("id"));
		infor.setNewsUrl(rs.getString("news_url"));
		infor.setParentUrl(rs.getString("parent_url"));
		infor.setSubDomain(rs.getString("sub_domain"));
		infor.setDocId(rs.getLong("docid"));
		infor.setImgUrls(rs.getString("img_urls"));
		infor.setVideoUrls(rs.getString("video_urls"));
		infor.setNewsTitle(rs.getString("title"));
		infor.setNewsDocument(rs.getString("document"));

		return infor;
	}

}
