package cho.carbon.hc.copframe.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public class CollectionUtils {
	/**
	 * 将集合转换为map
	 * @param items 集合对象，不能为null
	 * @param keyGetter 每个元素对象获取其key的方法
	 * @return {@link LinkedHashMap}对象
	 */
	public static <V, T> Map<T, V> toMap(Collection<V> items, Function<V, T> keyGetter){
		Assert.notNull(items);
		Assert.notNull(keyGetter);
		Map<T, V> map = new LinkedHashMap<T, V>();
		items.forEach(item->{
			if(item==null) {
				return;
			}
			T key = keyGetter.apply(item);
			if(key != null){
				map.put(key, item);
			}
		});
		return map;
	}
	
	/**
	 * 将集合中key相同的组合放到list中，以这个key为返回map的key，list为map的value
	 * @param items
	 * @param keyGetter
	 * @return
	 */
	public static <T, V, C extends Collection<V>> Map<T, C> toCollectionMap(
			Collection<V> items, 
			Function<V, T> keyGetter, 
			Supplier<C> collectionItemSupplier){
		return toCollectionMap(items, keyGetter, item->item, collectionItemSupplier);
	}
	
	/**
	 * 将集合中key相同的组合放到list中，以这个key为返回map的key，list为map的value
	 * @param <VV>
	 * @param items
	 * @param keyGetter
	 * @return
	 */
	public static <T, V, VV, C extends Collection<VV>> Map<T, C> toCollectionMap(
			Collection<V> items, 
			Function<V, T> keyGetter, 
			Function<V, VV> itemWrapper,
			Supplier<C> collectionItemSupplier){
		Assert.notNull(items);
		Assert.notNull(keyGetter);
		Assert.notNull(collectionItemSupplier);
		Map<T, C> map = new LinkedHashMap<T, C>();
		items.forEach(item->{
			T key = keyGetter.apply(item);
			if(key != null){
				C list = map.get(key);
				if(list == null){
					list = collectionItemSupplier.get();
					map.put(key, list);
				}
				list.add(itemWrapper.apply(item));
			}
		});
		return map;
	}
	
	/**
	 * 将集合中key相同的组合放到list中，以这个key为返回map的key，list为map的value
	 * @param items
	 * @param keyGetter
	 * @return
	 */
	public static <T, V> Map<T, List<V>> toListMap(Collection<V> items, Function<V, T> keyGetter){
		return toCollectionMap(items, keyGetter, ()->new ArrayList<>());
	}
	
	public static String toChain(Collection<?> source, String spliter) {
		if(source == null){
			return null;
		}else{
			StringBuffer buffer = new StringBuffer();
			String _spliter = spliter == null? "": spliter;
			source.forEach(item -> {
				buffer.append(FormatUtils.toString(item) + _spliter);
			});
			if(buffer.length() > 0){
				buffer.delete(buffer.length() - spliter.length(), buffer.length());
			}
			return buffer.toString();
		}
	}

	public static String toChain(Collection<?> source) {
		return toChain(source, ",");
	}
	
	/**
	 * 从集合source的元素中抽取属性，添加到另一个集合中作为元素
	 * @param source
	 * @param target
	 * @param itemGetter
	 */
	public static <T, R> void appendTo(Collection<T> source, Collection<R> target, Function<T, R> itemGetter) {
		if(source != null && target != null && itemGetter != null){
			source.forEach(item -> {
				R r = itemGetter.apply(item);
				target.add(r);
			});
		}
	}
	/**
	 * 从集合source中抽取属性，放到一个新的list中作为元素
	 * @param source 集合
	 * @param itemGetter 获得属性的方法
	 * @return
	 */
	public static <T, R> ArrayList<R> toList(Collection<T> source, Function<T, R> itemGetter){
		ArrayList<R> result = new ArrayList<R>();
		appendTo(source, result, itemGetter);
		return result;
	}
	
	/**
	 * 处理集合中的每个对象，将其转换为有序set
	 * @param source
	 * @param itemGetter
	 * @return
	 */
	public static <T, R> LinkedHashSet<R> toSet(Collection<T> source, Function<T, R> itemGetter){
		LinkedHashSet<R> result = new LinkedHashSet<R>();
		appendTo(source, result, itemGetter);
		return result;
	}
	
	/**
	 * 筛选Map中的元素，将符合条件的元素复制到另一个map中
	 * @param source 存放原数据的map
	 * @param target 复制的目标map
	 * @param filterFn 筛选器，返回true时，将元素放到目标map中
	 */
	public static <T, R, M extends Map<T, R>> void filter(M source, M target, BiFunction<T, R, Boolean> filterFn){
		source.forEach((key, value)->{
			if(Boolean.TRUE.equals(filterFn.apply(key, value))){
				target.put(key, value);
			}
		});
	}
	
	public static <T, R> LinkedHashMap<T, R> filter(Map<T, R> source, Function<T, Boolean> filterFn){
		LinkedHashMap<T, R> map = new LinkedHashMap<T, R>();
		filter(source, map, (key, value)->filterFn.apply(key));
		return map;
	}
	
	public static <T> void filter(Collection<T> source, Collection<T> target, Function<T, Boolean> filterFn){
		source.forEach(item->{
			if(Boolean.TRUE.equals(filterFn.apply(item))){
				target.add(item);
			}
		});
	}
	
	public static <T> LinkedHashSet<T> filter(Collection<T> source, Function<T, Boolean> filterFn){
		LinkedHashSet<T> set = new LinkedHashSet<T>();
		filter(source, set, filterFn);
		return set;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends V, V> V[] toArray(Collection<T> source, Class<V> eleClass){
		return source.toArray((V[]) Array.newInstance(eleClass, source.size()));
	}

	public static <T> void removes(Iterable<T> collection, Predicate<T> removable) {
		if(collection != null) {
			Iterator<T> itr = collection.iterator();
			while(itr.hasNext()) {
				T item = itr.next();
				if(removable.test(item)) {
					itr.remove();
				}
			}
		}
	}

	public static <K, V> void removesMapItem(Map<K, V> map, BiPredicate<K, V> removable) {
		if(map != null) {
			Set<Entry<K, V>> entrySet = map.entrySet();
			removes(entrySet, entry->removable.test(entry.getKey(), entry.getValue()));
		}
	}
	
}
