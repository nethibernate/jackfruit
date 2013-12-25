package com.jackfruit.config;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;

import com.google.common.base.Charsets;
import com.jackfruit.config.exception.ConfigBuildException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * ConfigBuilder is a class that you can use to
 * build your object from a specific configuration file.
 * 
 * <p><strong>example:</strong>
 * <p><pre><strong>Class:</strong></pre>
 * <p><pre>	class OuterConfig {</pre>
 * <p> <pre>	   int value1;</pre>
 * <p> <pre>	   String value2;</pre>
 * <p> <pre>	   InnerConfig innerConfig;</pre>
 * <p><pre>	}</pre>
 * <p>
 * <p><pre>	class InnerConfig {</pre>
 * <p>	<pre>	   int value1;</pre>
 * <p>	<pre>	   String value3;</pre>
 * <p><pre>	}</pre>
 * <p><pre><strong>ConfigFileContent:</strong></pre>
 * <p><pre>	value1 = 1</pre>
 * <p><pre>	value2 = "value"</pre>
 * <p><pre>	innerConfig {</pre>
 * <p>	<pre>	   value1 = 2</pre>
 * <p>	<pre>	   value3 = "value2"</pre>
 * <p><pre>	}</pre>
 * <p><pre>	innerConfig.value3 = "value3"</pre>
 * <p>This configuration supports class nest as shown above.
 * The member in a class can be any type except Collection.
 * <strong>We don't support Collection type.</strong>
 * <p><strong>You must obey the format of the configuration above</strong>,or you will fail
 * to build the object from the configuration file.
 * <p>Note that in the example we configure the innerConfig.value3 twice, in this case, it will pick up the last one.
 * <P>The ConfigBuilder class cannot be extended.
 * @author yaguang.xiao
 *
 */
public final class ConfigBuilder {
	
	/**
	 * Build an object from the specific configuration file.
	 * 
	 * @param cfgPath the configuration file's name with its file suffixes.
	 * @param clazz the class of the object you want to build.
	 * @return the object you want to build.
	 * @throws ConfigBuildException in the following cases:
	 * <p><pre>	configuration file path is not valid!</pre>
	 * <p><pre>	open configuration file stream fail!</pre>
	 * <p><pre>	fail to read the configuration file!</pre>
	 * <p><pre>	parse configuration file content fail!</pre>
	 * <p><pre>	fail to build the target object!</pre>
	 * 
	 */
	public static <T> T buildConfig(String cfgPath, Class<T> clazz) {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		URL url = classLoader.getResource(cfgPath);
		if(url == null) {
			throw new ConfigBuildException("configuration file path is not valid!");
		}
		
		InputStream in = null;
		Config config = null;
		try {
			in = url.openStream();
		} catch (IOException e) {
			throw new ConfigBuildException("open configuration file stream fail!", e);
		}
		
		String content = null;
		try {
			content = IOUtils.readAndClose(in, Charsets.UTF_8.name());
		} catch (Exception e) {
			throw new ConfigBuildException("fail to read the configuration file!", e);
		}
		
		try {
			config = ConfigFactory.parseString(content);
		} catch (Exception e) {
			throw new ConfigBuildException("parse configuration file content fail!please check your configuration file's format!", e);
		}
		
		return buildConfigObject("", clazz, config);
	}
	
	/**
	 * Build the specific object recursively.
	 * 
	 * @param prefix	the path of the current object in configuration file.
	 * @param clazz		the class of the object we want to build.
	 * @param config	the configuration object that contains all values we specify in the configuration file.
	 * @return
	 */
	private static <T> T buildConfigObject(String prefix, Class<T> clazz, Config config) {
		T configObject = null;
		try {
			configObject = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for(Field field : fields) {
				Object value = null;
				if(field.getType().isPrimitive() || PrimitiveType.isPrimitive(field.getType().getName())) {
					String path = prefix + field.getName();
					if(config.hasPath(path)) {
						value = config.getValue(path).unwrapped();
					}
				} else {
					value = buildConfigObject(prefix + field.getName() + ".", field.getType(), config);
				}
				if(value != null) {
					field.setAccessible(true);
					field.set(configObject, value);
					field.setAccessible(false);
				}
			}
		} catch (InstantiationException e) {
			throw new ConfigBuildException("fail to build " + clazz.getName() + " object from configuration object!", e);
		} catch (IllegalAccessException e) {
			throw new ConfigBuildException("fail to build " + clazz.getName() + " object from configuration object!", e);
		}
		
		return configObject;
	}
}
