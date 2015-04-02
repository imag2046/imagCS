package imag.crawler.tests;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.lakeside.data.sqldb.MysqlDataSource;

public class ImportData2Mysql {

	@Test
	public void test() {
		
		// save group top10 names into sql;
		//saveGroupNamesIntoSQL("/home/wxm/saveResults/getTop10Names_Neg.txt");
		
		// save img url;
		saveimgUrlIntoSQL("/home/wxm/saveResults/imgslist/Hanwang_vecImgurlList.txt");
		saveimgUrlIntoSQL("/home/wxm/saveResults/imgslist/ugc123_file_list.txt");
		saveimgUrlIntoSQL("/home/wxm/saveResults/imgslist/ugc4_file_list.txt");
		
		// save tag list;
		//saveTagsIntoSQL("/home/wxm/saveResults/Hanwang_groupNamesInfor.txt");
		
//		// save img_group_infor;
//		saveGroupInfoIntoSQL("/home/wxm/saveResults/ugc_results/Hanwang_groupImgInfor.txt");
//		saveGroupInfoIntoSQL("/home/wxm/saveResults/ugc_results/ugc123_img_group_refine.txt");
//		saveGroupInfoIntoSQL("/home/wxm/saveResults/ugc_results/ugc4_img_group_refine.txt");
//		
//		// save 20names with one groupid;
//		saveGroupNames20IntoSQL("/home/wxm/saveResults/ugc_results/Hanwang_groupNamesInfor_20.txt");
//		saveGroupNames20IntoSQL("/home/wxm/saveResults/ugc_results/ugc_20names.txt");
//		//saveGroupNames20IntoSQL("/home/wxm/saveResults/getTop10Names.txt","/home/wxm/saveResults/getTop10Names_Neg.txt");

		
		System.out.println("finish!");
	}
	
	public void saveimgUrlIntoSQL(String filePath) {
		// resource;
		MysqlDao mysqlDao = new MysqlDao();
		MysqlDataSource mysql = mysqlDao.getDataSource();
		// get the data in the file ;
		// img url list;
		String sql = "INSERT INTO `dp_test`.`dp_all_img` (`id`, `url`) VALUES (NULL, :value );";
		List<String> list = new ArrayList<String>();
		try {
			list = getList(filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map[] maps = new Map[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				HashMap<String, Object> paramMap = new HashMap();
				paramMap.put("value", list.get(i));
				maps[i] = paramMap;
			}
		}
		mysqlDao.execute(sql, maps);
	}

	public void saveTagsIntoSQL(String filePath) {
		// resource;
		MysqlDao mysqlDao = new MysqlDao();
		MysqlDataSource mysql = mysqlDao.getDataSource();
		// get the data in the file ;
		// tag list;
		String sql = "INSERT INTO `sense_deeplearning`.`dp_tag` (`id`, `tag`) VALUES (NULL, :value );";
		List<String> list = new ArrayList<String>();
		try {
			list = getList(filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map[] maps = new Map[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				HashMap<String, Object> paramMap = new HashMap();
				paramMap.put("value", list.get(i));
				maps[i] = paramMap;
			}
		}
		mysqlDao.execute(sql, maps);
	}
	
	/**
	 * @param filePath
	 */
	public void saveGroupNamesIntoSQL(String filePath) {
		// resource;
		MysqlDao mysqlDao = new MysqlDao();
		MysqlDataSource mysql = mysqlDao.getDataSource();
		// get the data in the file ;
		// tag list;
		String sql = "INSERT INTO `sense_deeplearning`.`dp_group` (`id`, `group_id`, `flag`, `name_first`, `name_second`, `name_third`, `name_four`, `name_five`, `name_six`, `name_seven`, `name_eight`, `name_nine`, `name_ten`) VALUES (NULL, :groupId, :flag, :name_first, :name_second, :name_third, :name_four, :name_five, :name_six, :name_seven, :name_eight, :name_nine, :name_ten);";
		List<String> list = new ArrayList<String>();
		try {
			list = getGroupNamesList(filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map[] maps = new Map[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				HashMap<String, Object> paramMap = new HashMap();
				String strLine[] = list.get(i).split(",");
				// know the groupid and flag;
//				paramMap.put("groupId", strLine[0]);
//				paramMap.put("flag", strLine[1]);
//				paramMap.put("name_first", strLine[2]);
//				paramMap.put("name_second", strLine[3]);
//				paramMap.put("name_third", strLine[4]);
//				paramMap.put("name_four", strLine[5]);
//				paramMap.put("name_five", strLine[6]);
//				paramMap.put("name_six", strLine[7]);
//				paramMap.put("name_seven", strLine[8]);
//				paramMap.put("name_eight", strLine[9]);
//				paramMap.put("name_nine", strLine[10]);
//				paramMap.put("name_ten", strLine[11]);
				
				// no groupId and flag in the file;
				paramMap.put("groupId", i+1+37);
				paramMap.put("flag", "2");  // 1 for positive; 2 for negative;
				paramMap.put("name_first", strLine[0]);
				paramMap.put("name_second", strLine[1]);
				paramMap.put("name_third", strLine[2]);
				paramMap.put("name_four", strLine[3]);
				paramMap.put("name_five", strLine[4]);
				paramMap.put("name_six", strLine[5]);
				paramMap.put("name_seven", strLine[6]);
				paramMap.put("name_eight", strLine[7]);
				paramMap.put("name_nine", strLine[8]);
				paramMap.put("name_ten", strLine[9]);
				maps[i] = paramMap;
			}
		}
		mysqlDao.execute(sql, maps);
	}
	
	public void saveGroupNames20IntoSQL(String filePath1,String filePath2) {
		// resource;
		MysqlDao mysqlDao = new MysqlDao();
		MysqlDataSource mysql = mysqlDao.getDataSource();
		// get the data in the file ;
		// tag list;
		String sql = "INSERT INTO `sense_deeplearning`.`dp_group_topnames` (`id`, `group_id`, `pos_flag`, `name_first`, `name_second`, `name_third`, `name_four`, `name_five`, `name_six`, `name_seven`, `name_eight`, `name_nine`, `name_ten`, `neg_flag`,`name_elev`,`name_twev`,`name_thirteen`,`name_fourteen`,`name_fifteen`,`name_sixteen`,`name_seventeen`,`name_eighteen`,`name_ninteen`,`name_twenty`) VALUES (NULL, :groupId, :pos_flag, :name_first, :name_second, :name_third, :name_four, :name_five, :name_six, :name_seven, :name_eight, :name_nine, :name_ten, :neg_flag, :name_elev, :name_twev, :name_thirteen, :name_fourteen, :name_fifteen, :name_sixteen, :name_seventeen, :name_eighteen, :name_ninteen, :name_twenty);";
		List<String> list = new ArrayList<String>();
		try {
			//list = getGroupNamesList(filePath);
			list = getGroup20NamesList(filePath1,filePath2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map[] maps = new Map[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				HashMap<String, Object> paramMap = new HashMap();
				String strLine[] = list.get(i).split(",");
				// know the groupid and flag;
//				paramMap.put("groupId", strLine[0]);
//				paramMap.put("pos_flag", strLine[1]);
//				paramMap.put("name_first", strLine[2]);
//				paramMap.put("name_second", strLine[3]);
//				paramMap.put("name_third", strLine[4]);
//				paramMap.put("name_four", strLine[5]);
//				paramMap.put("name_five", strLine[6]);
//				paramMap.put("name_six", strLine[7]);
//				paramMap.put("name_seven", strLine[8]);
//				paramMap.put("name_eight", strLine[9]);
//				paramMap.put("name_nine", strLine[10]);
//				paramMap.put("name_ten", strLine[11]);
//				
//				paramMap.put("neg_flag", strLine[12]);
//				paramMap.put("name_elev", strLine[13]);
//				paramMap.put("name_twev", strLine[14]);
//				paramMap.put("name_thirteen", strLine[15]);
//				paramMap.put("name_fourteen", strLine[16]);
//				paramMap.put("name_fifteen", strLine[17]);
//				paramMap.put("name_sixteen", strLine[18]);
//				paramMap.put("name_seventeen", strLine[19]);
//				paramMap.put("name_eighteen", strLine[20]);
//				paramMap.put("name_ninteen", strLine[21]);
//				paramMap.put("name_twenty", strLine[22]);
				
				// no groupId and flag in the file;
				paramMap.put("groupId", strLine[0]);
				paramMap.put("pos_flag", strLine[1]);
				paramMap.put("name_first", strLine[2]);
				paramMap.put("name_second", strLine[3]);
				paramMap.put("name_third", strLine[4]);
				paramMap.put("name_four", strLine[5]);
				paramMap.put("name_five", strLine[6]);
				paramMap.put("name_six", strLine[7]);
				paramMap.put("name_seven", strLine[8]);
				paramMap.put("name_eight", strLine[9]);
				paramMap.put("name_nine", strLine[10]);
				paramMap.put("name_ten", strLine[11]);
				
				paramMap.put("neg_flag", strLine[12]);
				paramMap.put("name_elev", strLine[13]);
				paramMap.put("name_twev", strLine[14]);
				paramMap.put("name_thirteen", strLine[15]);
				paramMap.put("name_fourteen", strLine[16]);
				paramMap.put("name_fifteen", strLine[17]);
				paramMap.put("name_sixteen", strLine[18]);
				paramMap.put("name_seventeen", strLine[19]);
				paramMap.put("name_eighteen", strLine[20]);
				paramMap.put("name_ninteen", strLine[21]);
				paramMap.put("name_twenty", strLine[22]);
				maps[i] = paramMap;
			}
		}
		mysqlDao.execute(sql, maps);
	}
	
	public void saveGroupNames20IntoSQL(String filePath) {
		// resource;
		MysqlDao mysqlDao = new MysqlDao();
		MysqlDataSource mysql = mysqlDao.getDataSource();
		// get the data in the file ;
		// tag list;
		String sql = "INSERT INTO `sense_deeplearning`.`dp_all_group_topnames` (`id`, `group_id`, `pos_flag`, `name_first`, `name_second`, `name_third`, `name_four`, `name_five`, `name_six`, `name_seven`, `name_eight`, `name_nine`, `name_ten`, `neg_flag`,`name_elev`,`name_twev`,`name_thirteen`,`name_fourteen`,`name_fifteen`,`name_sixteen`,`name_seventeen`,`name_eighteen`,`name_ninteen`,`name_twenty`) VALUES (NULL, :groupId, :pos_flag, :name_first, :name_second, :name_third, :name_four, :name_five, :name_six, :name_seven, :name_eight, :name_nine, :name_ten, :neg_flag, :name_elev, :name_twev, :name_thirteen, :name_fourteen, :name_fifteen, :name_sixteen, :name_seventeen, :name_eighteen, :name_ninteen, :name_twenty);";
		List<String> list = new ArrayList<String>();
		try {
			list = getGroupNamesList(filePath);
			//list = getGroup20NamesList(filePath1,filePath2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map[] maps = new Map[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				HashMap<String, Object> paramMap = new HashMap();
				String strLine[] = list.get(i).split(",");
				// know the groupid and flag;
//				paramMap.put("groupId", strLine[0]);
//				paramMap.put("pos_flag", strLine[1]);
//				paramMap.put("name_first", strLine[2]);
//				paramMap.put("name_second", strLine[3]);
//				paramMap.put("name_third", strLine[4]);
//				paramMap.put("name_four", strLine[5]);
//				paramMap.put("name_five", strLine[6]);
//				paramMap.put("name_six", strLine[7]);
//				paramMap.put("name_seven", strLine[8]);
//				paramMap.put("name_eight", strLine[9]);
//				paramMap.put("name_nine", strLine[10]);
//				paramMap.put("name_ten", strLine[11]);
//				
//				paramMap.put("neg_flag", strLine[12]);
//				paramMap.put("name_elev", strLine[13]);
//				paramMap.put("name_twev", strLine[14]);
//				paramMap.put("name_thirteen", strLine[15]);
//				paramMap.put("name_fourteen", strLine[16]);
//				paramMap.put("name_fifteen", strLine[17]);
//				paramMap.put("name_sixteen", strLine[18]);
//				paramMap.put("name_seventeen", strLine[19]);
//				paramMap.put("name_eighteen", strLine[20]);
//				paramMap.put("name_ninteen", strLine[21]);
//				paramMap.put("name_twenty", strLine[22]);
				
				// no groupId and flag in the file;
				paramMap.put("groupId", strLine[0]);
				paramMap.put("pos_flag", strLine[1]);
				paramMap.put("name_first", strLine[2]);
				paramMap.put("name_second", strLine[3]);
				paramMap.put("name_third", strLine[4]);
				paramMap.put("name_four", strLine[5]);
				paramMap.put("name_five", strLine[6]);
				paramMap.put("name_six", strLine[7]);
				paramMap.put("name_seven", strLine[8]);
				paramMap.put("name_eight", strLine[9]);
				paramMap.put("name_nine", strLine[10]);
				paramMap.put("name_ten", strLine[11]);
				
				paramMap.put("neg_flag", strLine[12]);
				paramMap.put("name_elev", strLine[13]);
				paramMap.put("name_twev", strLine[14]);
				paramMap.put("name_thirteen", strLine[15]);
				paramMap.put("name_fourteen", strLine[16]);
				paramMap.put("name_fifteen", strLine[17]);
				paramMap.put("name_sixteen", strLine[18]);
				paramMap.put("name_seventeen", strLine[19]);
				paramMap.put("name_eighteen", strLine[20]);
				paramMap.put("name_ninteen", strLine[21]);
				paramMap.put("name_twenty", strLine[22]);
				maps[i] = paramMap;
			}
		}
		mysqlDao.execute(sql, maps);
	}

	public void saveGroupInfoIntoSQL(String filePath) {
		// resource;
		MysqlDao mysqlDao = new MysqlDao();
		MysqlDataSource mysql = mysqlDao.getDataSource();
		// get the data in the file ;
		// img_group list;
		String sql = "INSERT INTO `dp_img_test`.`dp_all_img_group` (`id`, `group_id`, `img_id`, `pos_neg_flag`, `img_order`) VALUES (NULL, :g_id, :img_id, :flag, :order);";
		// String filePath = "/home/wxm/saveResults/getImg_Group_Info.txt";
		List<String> list = new ArrayList<String>();
		try {
			list = getList(filePath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map[] maps = new Map[list.size()];
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null) {
				HashMap<String, Object> paramMap = new HashMap();
				String strLine[] = list.get(i).split(",");
				paramMap.put("g_id", strLine[0]);
				paramMap.put("img_id", strLine[1]);
				paramMap.put("flag", strLine[2]);
				paramMap.put("order", strLine[3]);
				maps[i] = paramMap;
			}
		}
		mysqlDao.execute(sql, maps);
	}

	/**
	 * @param filePath
	 *            @ filePath : "/home/wxm/saveResults/uniqueTags.txt";
	 * @return
	 * @throws Exception
	 */
	public static List<String> getList(String filePath) throws Exception {
		List<String> list = new ArrayList<String>();
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String sLine = br.readLine();
		while (sLine != null) {
			list.add(sLine);
			sLine = br.readLine();
		}
		br.close();
		return list;
	}

	/**
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<String> getImgGroupList(String filePath)
			throws Exception {
		List<String> list = new ArrayList<String>();
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String sLine = br.readLine();
		while (sLine != null) {
			list.add(sLine);
			sLine = br.readLine();
		}
		br.close();
		return list;
	}

	/**
	 * read the group top10 names and save into the table;
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<String> getGroupNamesList(String filePath) throws Exception {
		List<String> list = new ArrayList<String>();
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String sLine = br.readLine();
		while (sLine != null) {
			list.add(sLine);
			sLine = br.readLine();
		}
		br.close();
		return list;
	}
	
	public static List<String> getGroup20NamesList(String filePath1, String filePath2) throws Exception {
		List<String> list = new ArrayList<String>();
		
		
		List<String> list1 = getGroupNamesList(filePath1);
		List<String> list2 = getGroupNamesList(filePath2);
		
		for (int i = 0; i < list1.size(); i++) {
			int gid = i+1+37;
			StringBuffer sb = new StringBuffer();
			sb.append(gid).append(",1,").append(list1.get(i)).append(",2,").append(list2.get(i));
			list.add(sb.toString());
		}
		
		return list;
	}

}
