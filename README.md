# Asynchronous Primary Processor

The asynchronous primary processor is used to control processing of asynchronous queues for two Geode clusters using the WAN gateway feature  where one cluster is the primary cluster and the other cluster is the secondary or backup Geode cluster. The primary cluster is responsible for processing all asynchronous queues and in the event of a primary cluster failure the asynchronous queue processing is performed by the secondary or backup cluster. 

## async-processor-configurer
The asynchronous process configurer is a Geode function which is used to turn on or off asynchronous queue processing in a cluster. The configurer function must be called using the onRegion method and also must provide the key or keys to configure using the filter method. 
You can pass a special key "ALL" and the configurer will create keys for all defined asynchronous event listeners. Lastly there are two arguments supported primary-on (make the cluster primary) or primary-off (make the cluster secondary).

#### Example 
execute function --id=AsyncProcessorConfigurer --region=AsyncProcessorConfig --filter=AsyncProcessor --arguments=primary-on

execute function --id=AsyncProcessorConfigurer --region=AsyncProcessorConfig --filter=ALL --arguments=primary-on

## async-processor-monitor
The asynchronous process monitor is a singleton invoked by the abstract class monitor-async-process that has implemented by an asynchronous event listener to process asynchronous events to monitor which cluster is primary cluster that processes asynchronous events.

## async-processor-thread-factory
The async processor thread factory is used by the async-processor-monitor to create the thread pool name

## async-processor-monitor-listener
The async processor monitor listener is used to determine when the cluster is shutting down by listening for a region destroy event and then shutting down the async-processor-monitor thread pool.

## monitor-async-process
The monitor-async-process is an abstract class extended by the async-processor class and implements the async-process-monitor to perform monitoring.

## async-processor
The async-processor is an example asynchronous queue processor that extends the monitor-async-process  

### Additional Requirements
#### Region
The async-processor-monitor uses the AsyncProcessorConfig region to save details about the primary cluster. This region must be defined in both of the clusters and should be a replicated region.
#### Server Property
The cache servers in the primary cluster should define the property "primary-async-processor=true". The async-processor-configuer function uses this property to determine which cluster is initially the primary cluster.
