/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.framework.config;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Configuration extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;

	public Configuration() {
	}

	public Configuration(Configuration conf) {
		putAll(conf);
	}

	public Long getLong(String key) {
		String value = (String) get(key);
		if (value == null) {
			return null;
		}
		return Long.valueOf(value);
	}

	public Integer getInt(String key, int defaultValue) {
		String value = (String) get(key);
		if (value == null) {
			return Integer.valueOf(defaultValue);
		}
		return Integer.valueOf(value);
	}

	public BigDecimal getBigDecimal(String key) {
		String value = (String) get(key);
		if (value == null) {
			return null;
		}
		return new BigDecimal(value);
	}

	public Date getDate(String key) {
		String value = (String) get(key);
		if (value == null) {
			return null;
		}
		if (value.equals("")) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(value);
		} catch (ParseException e) {
		}
		return null;
	}

	public Location getLocation(String key) {
		String value = (String) get(key);
		if (value == null) {
			return null;
		}
		if (value.equals("")) {
			return null;
		}

		String[] values = value.split(",");
		if ((value == null) || (values.length != 2)) {
			return null;
		}
		return new Location(values[0], values[1]);
	}

	public String get(String key, String defaultVal) {
		String val = (String) get(key);
		if (val == null) {
			return defaultVal;
		}
		return val;
	}

	public Configuration combineWith(Configuration conf) {
		Iterator iterator = conf.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry next = (Map.Entry) iterator.next();
			String key = (String) next.getKey();
			String value = (String) next.getValue();
			if (!(containsKey(key))) {
				put(key, value);
			}
		}
		return this;
	}
}