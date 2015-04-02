/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.framework.config;

import com.lakeside.core.utils.ApplicationResourceUtils;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ConfigurationManager {
	private static Map<Integer, Configuration> configs = new HashMap();

	public static Configuration load(String path) {
		int code = path.hashCode();
		Configuration conf = (Configuration) configs.get(Integer.valueOf(code));
		if (conf != null) {
			return conf;
		}
		synchronized (configs) {
			if (!(configs.containsKey(Integer.valueOf(code)))) {
				conf = new Configuration();
				try {
					String absolutePath = ApplicationResourceUtils
							.getResourceUrl(path);

					SAXReader reader = new SAXReader();
					File file = new File(absolutePath);
					Document document = null;
					if (!(file.exists())) {
						InputStream stream = ApplicationResourceUtils
								.getResourceStream(path);
						document = reader.read(stream);
					} else {
						document = reader.read(file);
					}
					Element root = document.getRootElement();
					Iterator iterator = root.elementIterator();
					while (iterator.hasNext()) {
						Element next = (Element) iterator.next();

						String name = "";
						String value = "";
						Element nameEl = next.element("name");
						if (name != null) {
							name = nameEl.getTextTrim();
						}
						Element valueEl = next.element("value");
						if (value != null) {
							value = valueEl.getTextTrim();
						}
						conf.put(name, value);
					}
					configs.put(Integer.valueOf(code), conf);
				} catch (DocumentException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return conf;
	}

	public static String append2Len(String src, int len) {
		String res = src;
		for (int i = src.length(); i < len; ++i) {
			res = "0" + res;
		}
		return res;
	}
}