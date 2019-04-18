package datatx.util.async.monitor.processor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.internal.cache.wan.AsyncEventQueueConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncProcessorMonitor {
	public static final String DIST_ID_PROP = "distributed-system-id";

	private final ScheduledExecutorService executor;
	private static AsyncProcessorMonitor monitor;
	private static final String REGION = "AsyncProcessorConfig";
	private static final String PROP_FILE_PROP = "gemfirePropertyFile";
	private static final Logger LOG = LoggerFactory.getLogger(AsyncProcessorMonitor.class);

	public void shutdown() {
		executor.shutdownNow();
		try {
			LOG.info(this.getClass().getSimpleName() + " waiting to shutdown");
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			LOG.info(this.getClass().getSimpleName() + " waiting to shutdown exception ", e);
		}
		executor.shutdown();
		LOG.info(this.getClass().getSimpleName() + " shutdown");
	}

	public synchronized static AsyncProcessorMonitor getInstance() {
		if (monitor == null) {
			monitor = new AsyncProcessorMonitor();
		}
		return monitor;
	}

	private AsyncProcessorMonitor() {
		executor = Executors.newScheduledThreadPool(0,
				new AsyncProcessorThreadFactory(this.getClass().getSimpleName()));
	}

	public synchronized void execute(Runnable r, long initialWaitTime, long waitTimeAfterCompletion, TimeUnit unit) {
		executor.scheduleAtFixedRate(r, initialWaitTime, waitTimeAfterCompletion, unit);
	}

	public synchronized static Region<String, Integer> getRegion() {
		Region<String,Integer> region = CacheFactory.getAnyInstance().getRegion(REGION);
		if (region == null) {
			String msg = "No " + REGION + " region was found";
			LOG.error(msg);
			throw new AsyncEventQueueConfigurationException(msg);
		}
		return region;
	}

	public synchronized static int getDistributedSystemId() {
		int id = Integer.getInteger(DIST_ID_PROP, -1).intValue();
		Properties gfProps = new Properties();
		if (id == -1) {
			String propFile = System.getProperty(PROP_FILE_PROP);
			if (propFile != null && propFile.length() > 0) {
				try {
					InputStream input = new FileInputStream(propFile);
					gfProps.load(input);
					input.close();
					id = Integer.parseInt((String) gfProps.get(DIST_ID_PROP));
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			} else {
				LOG.error("The " + DIST_ID_PROP
						+ " property was not found in the system or gemfire properties. No async processing will occur");
			}
		}
		return id;
	}
}
