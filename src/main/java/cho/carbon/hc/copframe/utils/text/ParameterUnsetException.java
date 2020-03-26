package cho.carbon.hc.copframe.utils.text;

public class ParameterUnsetException extends RuntimeException {

	public ParameterUnsetException(String key) {
		super("参数[" + key + "]没有设置");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5303639970096903225L;

	
	
	
}
