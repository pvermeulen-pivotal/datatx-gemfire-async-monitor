package datatx.util.async.monitor.processor;

import java.util.concurrent.TimeUnit;

import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.internal.cache.wan.AsyncEventQueueConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datatx.util.async.function.processor.AsyncProcessorConfigurer;
import datatx.util.async.processor.AsyncProcessor;

public abstract class MonitorAsyncProcessorImpl {
	private static final Logger LOG = LoggerFactory.getLogger(AsyncProcessor.class);
	private int distributionId = -1;
	private boolean primary = false;
	private Region<String, Integer> region;
	private String memberName;
	private String asyncProcessRegionKey;
	private long waitPeriod;
	private TimeUnit timeUnit;

	protected boolean isPrimary() {
		return primary;
	}

	protected String getAsyncProcessRegionKey() {
		return asyncProcessRegionKey;
	}

	protected String getMemberName() {
		return memberName;
	}

	public MonitorAsyncProcessorImpl(String asyncProcessRegionKey) {
		this(asyncProcessRegionKey, 5L, TimeUnit.SECONDS);
	}

	public MonitorAsyncProcessorImpl(String asyncProcessRegionKey, long waitPeriod, TimeUnit timeUnit) {
		this.asyncProcessRegionKey = asyncProcessRegionKey;
		this.memberName = CacheFactory.getAnyInstance().getDistributedSystem().getDistributedMember().getName();
		this.waitPeriod = waitPeriod;
		this.timeUnit = timeUnit;
		startMonitor();
	}

	public void startMonitor() {
		region = AsyncProcessorMonitor.getRegion();
		distributionId = AsyncProcessorMonitor.getDistributedSystemId();
		if (distributionId > 0) {
			AsyncProcessorMonitor.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					Object id = region.get(asyncProcessRegionKey);
					if (id != null) {
						if (((int) id) == distributionId) {
							primary = true;
						} else {
							primary = false;
						}
					} else {
						if (Boolean.getBoolean(AsyncProcessorConfigurer.PRIMARY_PROP) == true) {
							primary = true;
							region.put(asyncProcessRegionKey, distributionId);
						} else {
							primary = false;
						}
					}
				}
			}, 0L, this.waitPeriod, this.timeUnit);
		} else {
			String msg = "The " + AsyncProcessorMonitor.DIST_ID_PROP + " is not set or is invalid";
			LOG.error(msg);
			throw new AsyncEventQueueConfigurationException(msg);
		}
	}
}
