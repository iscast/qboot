package org.qboot.sys.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import org.qboot.common.entity.TreeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 菜单权限实体
 * @author iscast
 * @date 2020-09-25
 */
public class SysMenuDto extends TreeEntity<String> {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@NotNull
	@Length(min=1,max=50)
	private String name;

	/**
	 * 链接
	 */
	@Length(max=200)
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String href;

	/**
	 * 目标
	 */
	@Length(max=10)
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String target;

	/**
	 * 0：目录 1：菜单 2：按钮
	 */
	@NotNull
	@Length(max=2)
	@Pattern(regexp="0|1|2")
	private String type;

	/**
	 * 图标
	 */
	@Length(max=50)
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String icon;

	/**
	 * 是否在菜单中显示1显示，0 不显示
	 */
	@NotNull
	@Length(max=1)
	@Pattern(regexp="0|1")
	private String isShow;
	
	/**
	 * 改变状态按钮
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private String isShowString;

	/**
	 * 权限标识
	 */
	@Length(max=200)
	private String permission;
	
	/**
	 * 是否有下级菜单
	 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer hasSub;

    @JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Long> ids;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href == null ? null : href.trim();
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target == null ? null : target.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon == null ? null : icon.trim();
	}

	public String getIsShow() {
		return isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow == null ? null : isShow.trim();
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission == null ? null : permission.trim();
	}

	public Integer getHasSub() {
		return hasSub;
	}

	public void setHasSub(Integer hasSub) {
		this.hasSub = hasSub;
	}

	public String getIsShowString() {
		return isShowString;
	}

	public void setIsShowString(String isShowString) {
		this.isShowString = isShowString;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}