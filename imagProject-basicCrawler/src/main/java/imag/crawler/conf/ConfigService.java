package imag.crawler.conf;

import org.springframework.stereotype.Component;

import com.framework.config.Configuration;
import com.framework.config.ConfigurationManager;
import com.lakeside.web.beans.config.ConfigSetting;

/**
 * 系统配置组件服务
 */
@Component
public class ConfigService implements ConfigSetting{
	
	private Configuration globalConf = ConfigurationManager.load("conf/sense-config.xml");
	
	public ConfigService(){
		
	}
	
	public String getConfig(String key){
		return globalConf.get(key);
	}
}
