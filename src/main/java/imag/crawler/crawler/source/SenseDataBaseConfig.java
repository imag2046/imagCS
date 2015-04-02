package imag.crawler.crawler.source;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.framework.config.Configuration;
import com.framework.config.ConfigurationManager;
import com.lakeside.web.beans.config.ConfigSetting;

@Component
public class SenseDataBaseConfig implements ApplicationContextAware,InitializingBean{

	private static final String Develop_Config_Path = "conf/sense-database-config.xml";
	
	private static final String Release_Config_Path = "conf/sense-database-config-release.xml";
	
	private Configuration conf;
	
	private ConfigSetting configSetting;
	
	private static final String Release = "release";

	public SenseDataBaseConfig(){
		super();
	}
	
	public String getConfig(String key){
		return conf.get(key);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		configSetting = applicationContext.getBean(ConfigSetting.class);
	}

	public void afterPropertiesSet() throws Exception {
		String release = null;
		if(configSetting!=null){
			release = configSetting.getConfig(Release);
		}
		if("true".equals(release)){
			conf = ConfigurationManager.load(Release_Config_Path);
		}else{
			conf = ConfigurationManager.load(Develop_Config_Path);
		}
	}
}
