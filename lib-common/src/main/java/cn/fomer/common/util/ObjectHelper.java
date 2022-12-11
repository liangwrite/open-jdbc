package cn.fomer.common.util;

/**
 * 类说明
 *
 * @author Liang
 * @email liang@163.com
 * @date 2022-09-02
 */
public class ObjectHelper {

	
	/**
	 * 202209 判断类型，强制转换为byte[]
	 */
	public static boolean isByteArray(Object obj) {
		//if(value!=null&&value.getClass().isArray()&&"byte[]".equals(value.getClass().getSimpleName())) {
		if(obj!=null&&obj.getClass().isArray()&&"byte[]".equals(obj.getClass().getSimpleName())) {
			return true;
		}
		return false;
	}
}
