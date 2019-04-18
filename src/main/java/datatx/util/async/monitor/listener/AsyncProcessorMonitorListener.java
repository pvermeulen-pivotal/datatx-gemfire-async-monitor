package datatx.util.async.monitor.listener;

import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.RegionEvent;

import datatx.util.async.monitor.processor.AsyncProcessorMonitor;

@SuppressWarnings("rawtypes")
public class AsyncProcessorMonitorListener implements CacheListener {

	@Override
	public void afterCreate(EntryEvent event) {
		// Don't care		
	}

	@Override
	public void afterUpdate(EntryEvent event) {
		// Don't care		
	}

	@Override
	public void afterInvalidate(EntryEvent event) {
		// Don't care		
	}

	@Override
	public void afterDestroy(EntryEvent event) {
		// Don't care		
	}

	@Override
	public void afterRegionInvalidate(RegionEvent event) {
		// Don't care		
	}

	@Override
	public void afterRegionDestroy(RegionEvent event) {
		AsyncProcessorMonitor.getInstance().shutdown();
	}

	@Override
	public void afterRegionClear(RegionEvent event) {
		// Don't care		
	}

	@Override
	public void afterRegionCreate(RegionEvent event) {
		// Don't care		
	}

	@Override
	public void afterRegionLive(RegionEvent event) {
		// Don't care		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
