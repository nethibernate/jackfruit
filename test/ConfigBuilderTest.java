
import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import serverconfig.GameServerConfig;

import com.jackfruit.config.ConfigBuilder;
import com.jackfruit.config.exception.ConfigBuildException;

/**
 * Tests for ConfigBuilder.
 * @author yaguang.xiao
 *
 */
@RunWith(JUnit4.class)
public class ConfigBuilderTest {
	
	@Test
	public void testConfig() {
		GameServerConfig config = ConfigBuilder.buildConfig("server.conf", GameServerConfig.class);
		
		Assert.assertThat(config.getServerName(), is("Server2"));
		Assert.assertThat(config.getHost(), is("127.0.0.3"));
		Assert.assertThat(config.getPort(), is(2256));
		Assert.assertThat(config.getLogConfig().getLogServerIp(), is("127.0.0.4"));
		Assert.assertThat(config.getLogConfig().getLogServerPort(), is(2256));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo1(), is(1));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo2(), is("this is info2"));
	}
	
	@Test
	public void testDefault() {
		GameServerConfig config = ConfigBuilder.buildConfig("server2.conf", GameServerConfig.class);
		
		Assert.assertThat(config.getServerName(), is(""));
		Assert.assertThat(config.getHost(), is(""));
		Assert.assertThat(config.getPort(), is(2233));
		Assert.assertThat(config.getLogConfig().getLogServerIp(), is("127.0.0.1"));
		Assert.assertThat(config.getLogConfig().getLogServerPort(), is(8800));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo1(), is(23));
		Assert.assertThat(config.getLogConfig().getLogInfo().getInfo2(), is("info2"));
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testInvalidConfPath() {
		thrown.expect(ConfigBuildException.class);
		thrown.expectMessage("configuration file path is not valid!");
		
		ConfigBuilder.buildConfig("invalid.conf", GameServerConfig.class);
	}
	
	@Test
	public void testInvalidConfigContent() {
		thrown.expect(ConfigBuildException.class);
		thrown.expectMessage("parse configuration file content fail!please check your configuration file's format!");
		
		ConfigBuilder.buildConfig("invalidContent.conf", GameServerConfig.class);
	}
}
