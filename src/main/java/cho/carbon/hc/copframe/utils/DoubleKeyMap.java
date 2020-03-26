package cho.carbon.hc.copframe.utils;

public interface DoubleKeyMap<K1, K2, V> extends Iterable<DoubleKeyEntry<K1, K2, V>>{
	V get(K1 k1, K2 k2);
	
	void put(K1 k1, K2 k2, V value);
	
}
