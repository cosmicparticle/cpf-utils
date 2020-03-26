package cho.carbon.hc.copframe.utils.range;
/**
 * 
 * <p>Title: Range</p>
 * <p>Description: </p><p>
 * 范围接口
 * </p>
 * @author Copperfield Zhang
 * @date 2017年5月9日 上午10:13:30
 */
public interface Range<T> {
	boolean inRange(T point);
}
