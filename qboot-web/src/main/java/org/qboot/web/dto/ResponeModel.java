package org.qboot.web.dto;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回参数模型
 * -1 和 90开头的错误码为系统使用，其他场景自定义错误码即可
 * @author history
 *
 */
public class ResponeModel implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 成功响应吗
	 */
	public final static String GLOBAL_DEFAULT_SUCCESS = "0";
	/**
	 * 默认逻辑错误响应吗,ResponeCodeException  默认错误也是这个，前端会对这个进行统一错误提示。
	 */
	public final static String GLOBAL_DEFAULT_ERROR = "-1";

	public final static String GLOBAL_DEFAULT_SUCCESS_MSG = "GLOBAL_DEFAULT_SUCCESS_MSG";
	
	public final static String GLOBAL_DEFAULT_ERROR_MSG = "GLOBAL_DEFAULT_ERROR_MSG";
	
	/**
	 * 响应码
	 */
	private String code = GLOBAL_DEFAULT_SUCCESS;
	/**
	 * 响应信息
	 */
	private String msg = GLOBAL_DEFAULT_SUCCESS_MSG;
	/**
	 * 线程id
	 */
	private String requestId;
	
	/**
	 * 总条数，前段分页插件分页用
	 */
	private long count;
	
	/**
	 * 实际数据
	 */
	private Object data;

	public ResponeModel() {
		// TODO
//		requestId = MdcUtil.peek();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setMsg(String msg,Object[] args) {
		this.msg = msg;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public static ResponeModel ok() {
		return ok(GLOBAL_DEFAULT_SUCCESS_MSG);
	}

	public static ResponeModel ok(Object data) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setMsg(GLOBAL_DEFAULT_SUCCESS_MSG);
		responeModel.setData(data);
		return responeModel;
	}
	
	public static ResponeModel ok(String msg) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setMsg(msg);
		return responeModel;
	}


	public static ResponeModel ok(String msg,Object data) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setMsg(msg);
		responeModel.setData(data);
		return responeModel;
	}
	
	public static ResponeModel ok(PageInfo<?> page) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setMsg(GLOBAL_DEFAULT_SUCCESS_MSG);
		if(page != null) {
			responeModel.setCount(page.getTotal());
			responeModel.setData(page.getList());
		}
		return responeModel;
	}
	
	public static ResponeModel error() {
		return error(GLOBAL_DEFAULT_ERROR_MSG);
	}

	public static ResponeModel error(String code, String msg) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setCode(code);
		responeModel.setMsg(msg);
		return responeModel;
	}
	
	public static ResponeModel error(String code, String msg,Object... params) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setCode(code);
		if (null != params && StringUtils.isNotEmpty(msg)) {
			MessageFormat mf = new MessageFormat(msg);
			msg = mf.format(params);
		} 
		responeModel.setMsg(msg);
		return responeModel;
	}
	
	public static ResponeModel error(String msg) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setCode(GLOBAL_DEFAULT_ERROR);
		responeModel.setMsg(msg);
		return responeModel;
	}


	/**
	 * 将data设置成为map
	 * 
	 * @param key
	 * @param value
	 */
	public void addProperty(String key, String value) {
		if (null == data || (!(data instanceof Map))) {
			Map<String, Object> propertyMap = new HashMap<>();
			propertyMap.put(key, value);
			this.setData(propertyMap);
		} else {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)data;
			map.put(key, value);
		}
	}
}
