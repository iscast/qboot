package org.qboot.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.qboot.common.constants.SysConstants;
import org.qboot.common.error.ErrorCode;
import org.qboot.common.error.IError;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回参数模型
 * @author history
 */
public final class ResponeModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 响应码
	 */
	private int code = SysConstants.GLOBAL_DEFAULT_SUCCESS;
	/**
	 * 响应信息
	 */
	private String msg = SysConstants.SUCCESS;
	/**
	 * 请求id
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String requestId;
	
	/**
	 * 总条数，前段分页插件分页用
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private Long count;
	
	/**
	 * 实际数据
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;

	public ResponeModel() {}

	public static ResponeModel ok() {
		return ok(SysConstants.GLOBAL_DEFAULT_SUCCESS_MSG);
	}

    public static ResponeModel ok(IError errorCode) {
        ResponeModel responeModel = new ResponeModel();
        responeModel.setCode(errorCode.getErrorCode());
        responeModel.setMsg(errorCode.getErrorInfo());
        return responeModel;
    }

    public static ResponeModel ok(ErrorCode errorCode) {
        ResponeModel responeModel = new ResponeModel();
        responeModel.setCode(errorCode.getErrorCode());
        responeModel.setMsg(errorCode.getErrorInfo());
        return responeModel;
    }

	public static ResponeModel ok(Object data) {
		ResponeModel responeModel = new ResponeModel();
        responeModel.setCode(SysConstants.GLOBAL_DEFAULT_SUCCESS);
		responeModel.setMsg(SysConstants.GLOBAL_DEFAULT_SUCCESS_MSG);
		responeModel.setData(data);
		return responeModel;
	}
	
	public static ResponeModel ok(String msg) {
		ResponeModel responeModel = new ResponeModel();
        responeModel.setCode(SysConstants.GLOBAL_DEFAULT_SUCCESS);
		responeModel.setMsg(msg);
		return responeModel;
	}


	public static ResponeModel ok(String msg,Object data) {
		ResponeModel responeModel = new ResponeModel();
        responeModel.setCode(SysConstants.GLOBAL_DEFAULT_SUCCESS);
		responeModel.setMsg(msg);
		responeModel.setData(data);
		return responeModel;
	}
	
	public static ResponeModel ok(PageInfo<?> page) {
		ResponeModel responeModel = new ResponeModel();
        responeModel.setCode(SysConstants.GLOBAL_DEFAULT_SUCCESS);
		responeModel.setMsg(SysConstants.GLOBAL_DEFAULT_SUCCESS_MSG);
		if(page != null) {
			responeModel.setCount(page.getTotal());
			responeModel.setData(page.getList());
		}
		return responeModel;
	}
	
	public static ResponeModel error() {
		return error(SysConstants.GLOBAL_DEFAULT_ERROR_MSG);
	}

	public static ResponeModel error(int code, String msg) {
		ResponeModel responeModel = new ResponeModel();
		responeModel.setCode(code);
		responeModel.setMsg(msg);
		return responeModel;
	}
	
	public static ResponeModel error(int code, String msg,Object... params) {
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
		responeModel.setCode(SysConstants.GLOBAL_DEFAULT_ERROR);
		responeModel.setMsg(msg);
		return responeModel;
	}

    public static ResponeModel error(IError err) {
        ResponeModel responeModel = new ResponeModel();
        responeModel.setCode(err.getErrorCode());
        responeModel.setMsg(err.getErrorInfo());
        return responeModel;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
	 * 将data设置成为map
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
