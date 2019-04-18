package datatx.util.async.monitor.processor;

import java.util.concurrent.ThreadFactory;

public class AsyncProcessorThreadFactory implements ThreadFactory {
	private String prefix = "Async Process Monitor";

	public AsyncProcessorThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(r, prefix);
		t.setDaemon(true);
		return t;
	}
}