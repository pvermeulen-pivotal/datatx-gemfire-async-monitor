package datatx.util.async.processor;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEvent;
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datatx.util.async.monitor.processor.MonitorAsyncProcessorImpl;

public class AsyncProcessor extends MonitorAsyncProcessorImpl implements AsyncEventListener, Declarable {
	private static final Logger LOG = LoggerFactory.getLogger(AsyncProcessor.class);

	public AsyncProcessor() {
		super("AsyncProcesor", 5L, TimeUnit.SECONDS);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean processEvents(List<AsyncEvent> events) {
		boolean loggedWarning = false;
		if (this.isPrimary()) {
			loggedWarning = false;
			for (AsyncEvent ae : events) {
				// *********************************************************************
				// do process event work here and the log info below is only for testing
				// *********************************************************************
				LOG.info("Member: " + this.getMemberName() + ": Operation: " + ae.getOperation().toString() + " Key: "
						+ ae.getKey() + " Event ID: " + ae.getEventSequenceID() + " Callback: "
						+ ae.getCallbackArgument());
			}
		} else {
			if (!loggedWarning) {
				LOG.warn("Member: " + this.getMemberName() + " " + this.getClass().getSimpleName()
						+ " is not the primary processor");
				loggedWarning = true;
			}
		}
		return true;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Properties arg0) {
		// TODO Auto-generated method stub
		
	}
}
