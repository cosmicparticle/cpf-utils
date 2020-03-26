package cho.carbon.hc.copframe.utils;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;

public class TreeTable<T> implements Iterable<TableEntry<T>>{
	private TreeMap<Integer, TreeMap<Integer, T>> map = new TreeMap<Integer, TreeMap<Integer,T>>();
	private Integer yMax;
	private Integer yMin;
	
	
	public TreeTable<T> put(int x, int y, T value){
		TreeMap<Integer, T> innerMap;
		synchronized (map) {
			innerMap = map.get(x);
			if (innerMap == null) {
				innerMap = new TreeMap<Integer, T>();
				map.put(x, innerMap);
			}
		}
		innerMap.put(y, value);
		if(yMax == null || y > yMax){
			yMax = y;
		}
		if(yMin == null || y < yMin){
			yMin = y;
		}
		return this;
	}
	
	
	public T get(int x, int y){
		TreeMap<Integer, T> innerMap = map.get(x);
		if(innerMap == null){
			return null;
		}
		return innerMap.get(y);
	}
	
	
	
	/**
	 * 获得keyX的最大值
	 * @return
	 */
	public int getXMax(){
		return map.lastKey();
	}
	
	/**
	 * 获得keyX的最小值
	 * @return
	 */
	public int getXMin(){
		return map.firstKey();
	}
	
	/**
	 * 获得keyY的最大值
	 * @return
	 */
	public int getYMax(){
		return yMax;
	}
	
	/**
	 * 获得keyY的最小值
	 * @return
	 */
	public int getYMin(){
		return yMin;
	}
	
	/**
	 * 
	 * @return
	 */
	public String asMatrix(){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i <= this.getXMax(); i++) {
			for (int j = 0; j <= this.getYMax(); j++) {
				T val = this.get(i, j);
				buffer.append((val == null? "": val) + "\t");
			}
			buffer.append("\r\n");
		}
		return buffer.toString();
	}
	


	@Override
	public Iterator<TableEntry<T>> iterator() {
		Set<TableEntry<T>> es = new LinkedHashSet<TableEntry<T>>();
		this.map.forEach((x, inner) -> {
			inner.forEach((y, val) -> {
				es.add(new TableEntry<T>(x, y, val));
			});
		});
		return es.iterator();
	}
}
