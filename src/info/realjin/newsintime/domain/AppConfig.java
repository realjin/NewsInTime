package info.realjin.newsintime.domain;

import java.util.Properties;

public class AppConfig {
	private Properties props;

	public static final String CFGNAME_BATCHSIZE = "batchsize";
	public static final String CFGNAME_UI_MAIN_COLSELECTOR_TEXTSIZE = "ui.main.colselector.textsize";

	// color config
	public static final String CFGNAME_COLOR_BG_COLSELECTOR_BAR = "color.bg.colselector.bar";
	public static final String CFGNAME_COLOR_TEXT_COLSELECTOR_MENU_ITEM_TV = "color.text.colselector.menu.item.tv";

	// TODO: make sure every config has a default value when get!
	public AppConfig() {
		props = new Properties();
		testInitConfig();
		testLoadColorProperties();
	}

	private void testLoadColorProperties() {
		Properties colorProps = new Properties();
		colorProps.setProperty(CFGNAME_COLOR_BG_COLSELECTOR_BAR, "0xff00aeef");
		colorProps.setProperty(CFGNAME_COLOR_TEXT_COLSELECTOR_MENU_ITEM_TV,
				"0xffe1e1e1");
		loadColorProperties(colorProps);
	}

	private void loadColorProperties(Properties colorProps) {
		for (Object ok : props.keySet()) {
			String k = (String) ok;
			props.setProperty(k, colorProps.getProperty(k));
		}
	}

	/**
	 * mmm: test only
	 */
	private void testInitConfig() {
		props.put(CFGNAME_BATCHSIZE, "5");
		props.put(CFGNAME_UI_MAIN_COLSELECTOR_TEXTSIZE, "24");
	}

	// ----public methods

	public void setProperty(String name, String value) {
		props.setProperty(name, value);
	}

	public String getProperty(String name) {
		return props.getProperty(name);
	}

	public String getProperty(String name, String defaultValue) {
		return props.getProperty(name, defaultValue);
	}
}
