package com.st.bo;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadServiceCodeUtils {

	private ReadServiceCodeUtils() {
	}

	public static final Map<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();

	static {
		bundles.put("zh_CN", ResourceBundle.getBundle("lang.serviceCode", Locale.CHINA));
		bundles.put("en", ResourceBundle.getBundle("lang.serviceCode", Locale.ENGLISH));
	}

	private final static String ERROR_STRING_DEFAULT = "error code have no message!";
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadServiceCodeUtils.class);

	/**
	 * 用于读取中文
	 */
	public static String getCNServiceMessage(int serviceCode) {
		try {
			return bundles.get("zh_CN").getString(serviceCode + "");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ERROR_STRING_DEFAULT;
		}
	}

	/**
	 * 用于读取英文
	 */
	public static String getENServiceMessage(int serviceCode) {
		try {
			return bundles.get("en").getString(serviceCode + "");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ERROR_STRING_DEFAULT;
		}
	}

}
