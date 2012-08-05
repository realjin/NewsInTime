package info.realjin.newsintime.domain;

import java.util.Properties;

public class AppConfig {
	private Properties props;

	public static final String CFGNAME_BATCHSIZE = "batchsize";

	public AppConfig() {
		props = new Properties();
		testInitConfig();
	}

	/**
	 * mmm: test only
	 */
	private void testInitConfig() {
		props.put(CFGNAME_BATCHSIZE, "2");
	}

	// ----public methods

	public void put(String name, String value) {
		props.put(name, value);
	}

	public String get(String name) {
		return (String) props.get(name);
	}
}
