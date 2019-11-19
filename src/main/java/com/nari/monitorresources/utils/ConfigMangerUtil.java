package com.nari.monitorresources.utils;
import com.nari.monitorresources.CodeConstant;
import org.ini4j.Config;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.PropertyResourceBundle;

/**
 * 处理数据源配置文件
 * @author lenovo
 *
 */
public class ConfigMangerUtil {
	
  private static ConfigMangerUtil configMangerUtil;
  	
  	private PropertyResourceBundle bundle;

    private final static Logger logger = LoggerFactory.getLogger(ConfigMangerUtil.class);
	
	public ConfigMangerUtil() {
		//FileReader fr;
		try {
			InputStream in = ConfigMangerUtil.class.getClassLoader().getResourceAsStream("param.properties");
			bundle = new PropertyResourceBundle(in);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static synchronized ConfigMangerUtil getInstance() {
		if(configMangerUtil==null) {
			configMangerUtil=new ConfigMangerUtil();
		}
		return configMangerUtil;
	}

	/**
	 * 获取对应key的值
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return this.bundle.getString(key); //properties.getProperty(key);
	}

	public  static  String getSection() {

        String path = System.getenv("D5000_HOME");
        String filePath = path + File.separator + "conf" + File.separator + "domain.sys";
        logger.info("filePath is" + filePath);
        Config config = new Config();
        config.setMultiSection(true);
        InputStreamReader isr  = null;
        String  section = null;
        try {

            isr = new InputStreamReader(new FileInputStream(filePath));
            Wini ini = new Wini(isr);

            String reValue =  (String)ini.get("DOMAIN", "TYPEID", String.class);
            //com.nari.monitorresources.CodeConstant;
            if (reValue.equals(CodeConstant.CODE_DOMAIN.ZERO)) {

                return  section = CodeConstant.CODE_SECTION.ONE;

            } else if (reValue.equals(CodeConstant.CODE_DOMAIN.ONE)) {

                return  section = CodeConstant.CODE_SECTION.TWO;

            } else if (reValue.equals(CodeConstant.CODE_DOMAIN.TWO)) {

                return  section = CodeConstant.CODE_SECTION.THREE;
            }

        } catch (FileNotFoundException e){

            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return section;
    }
}