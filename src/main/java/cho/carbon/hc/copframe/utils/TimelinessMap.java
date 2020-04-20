package cho.carbon.hc.copframe.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 具有时效性的Map，该Map只有获取方法，但是在获取的时候
 * @author Copperfield
 * @date 2018年4月11日 下午4:13:59
 * @param <K>
 * @param <V>
 */
public class TimelinessMap<K, V>{

	private final Map<K, V> source;
	private final long timeout;
	private Map<K, Long> lastOperateTimeMap = new HashMap<>();
	
	Logger logger = LoggerFactory.getLogger(TimelinessMap.class);
	
	/**
	 * 
	 * @param timeout
	 */
	public TimelinessMap(long timeout) {
		this(new HashMap<>(), timeout);
	}
	
	/**
	 * 构造一个时效性Map
	 * @param source
	 * @param timeout
	 */
	public TimelinessMap(Map<K, V> source, long timeout) {
		super();
		this.source = source;
		this.timeout = timeout;
	}
	
	private void doWhenUncontainsKeyOrOvertime(K key, Consumer<K> consumer) {
		if(!lastOperateTimeMap.containsKey(key)) {
			consumer.accept(key);
		}else {
			Long lastOperateTime = lastOperateTimeMap.get(key);
			if(System.currentTimeMillis() - lastOperateTime > timeout) {
				consumer.accept(key);
			}
		}
	}

	/**
	 * 获得key对应的value
	 * 当key获取的时间超时时，会调用loadFunction重新获取value对象
	 * @param key
	 * @param loadFunction
	 * @return
	 */
	public synchronized V get(K key, Function<K, V> loadFunction){
		doWhenUncontainsKeyOrOvertime(key, (k)->doLoad(k, loadFunction));
		return source.get(key);
	}
	
	
	public synchronized Map<K, V> getMap(Set<K> keys, BiConsumer<Set<K>, Map<K, V>> loadConsumer){
		Map<K, V> loadedValueMap = new HashMap<K, V>();
		if(keys != null) {
			Set<K> toLoadKeys = filterUncontainsKeyOrOvertimeKeys(keys);
			loadConsumer.accept(toLoadKeys, loadedValueMap);
			loadedValueMap.forEach((k, v) ->{
				try {
					source.put(k, v);
					lastOperateTimeMap.put(k, System.currentTimeMillis());
				} catch (Exception e) {
					throw new RuntimeException("加载key[" + k + "]对应的值时发生错误", e);
				}
			});
			for (K key : keys) {
				if(!loadedValueMap.containsKey(key)) {
					loadedValueMap.put(key, source.get(key));
				}
			}
		}
		return loadedValueMap;
	}
	
	private Set<K> filterUncontainsKeyOrOvertimeKeys(Set<K> keys) {
		Set<K> ks = new HashSet<K>();
		if(keys != null && !keys.isEmpty()) {
			for (K key : keys) {
				if(!lastOperateTimeMap.containsKey(key)) {
					ks.add(key);
				}else {
					Long lastOperateTime = lastOperateTimeMap.get(key);
					if(System.currentTimeMillis() - lastOperateTime > timeout) {
						ks.add(key);
					}
				}
			}
		}
		return ks;
	}

	/**
	 * 根据多个key获得对应的所有的value的Map
	 * 获得的value会在过期后调用loadFunction重新获取
	 * @param keys
	 * @param resultMap
	 * @param loadFunction
	 */
	@SuppressWarnings("unchecked")
	public synchronized <CK extends Set<K>, MV extends Map<K, V>> void toMap(CK keys, MV resultMap, Function<CK, MV> loadFunction){
		Assert.notNull(resultMap);
		Assert.notNull(loadFunction);
		Set<K> queryKeys = new HashSet<>();
		keys.forEach(k->{
			doWhenUncontainsKeyOrOvertime(k, (k1)->queryKeys.add(k1));
		});
		if(!queryKeys.isEmpty()) {
			try {
				MV mv = loadFunction.apply((CK) queryKeys);
				Long now = System.currentTimeMillis();
				mv.forEach((k, v)->{
					source.put(k, v);
					lastOperateTimeMap.put(k, now);
				});
			} catch (Exception e) {
				logger.debug("执行根据多个Key获得对应的value的map时发生错误", e);
			}
		}
		keys.forEach(k->resultMap.put(k, source.get(k)));
	}

	private void doLoad(K key, Function<K, V> loadFunction) {
		try {
			V value = loadFunction.apply(key);
			source.put(key, value);
			lastOperateTimeMap.put(key, System.currentTimeMillis());
		} catch (Exception e) {
			throw new RuntimeException("加载key[" + key + "]对应的值时发生错误", e);
		}
	}
	
	public synchronized void refresh() {
		lastOperateTimeMap.clear();
		source.clear();
	}

	public Map<K, V> getAll(Supplier<Map<K, V>> supplier) {
		Map<K, V> map = supplier.get();
		if(map != null) {
			synchronized (source) {
				map.forEach((key, value)->{
					source.put(key, value);
					lastOperateTimeMap.put(key, System.currentTimeMillis());
				});
			}
		}
		return new HashMap<>(source);
	}

	public boolean contains(String moduleName) {
		return source.containsKey(moduleName);
	}
}
