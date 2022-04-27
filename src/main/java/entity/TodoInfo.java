package entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class TodoInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer todoIndex;
	private Integer checkoffStatus;
	private String category;
	private String priority;
	private String content;
	private String descriptions;
	private String remindEmailAddress;
	private Integer delaySign;
	private Long createTime;
	private Long updateTime;
	private Long remindTime;
	private Integer autoDelaySign;
	private Long delayTime;
	private Integer deleteSign;

	public Integer getTodoIndex() {
		return todoIndex;
	}

	public void setTodoIndex(Integer todoIndex) {
		this.todoIndex = todoIndex;
	}

	public Integer getCheckoffStatus() {
		return checkoffStatus;
	}

	public void setCheckoffStatus(Integer checkoffStatus) {
		this.checkoffStatus = checkoffStatus;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public String getRemindEmailAddress() {
		return remindEmailAddress;
	}

	public void setRemindEmailAddress(String remindEmailAddress) {
		this.remindEmailAddress = remindEmailAddress;
	}

	public Integer getDelaySign() {
		return delaySign;
	}

	public void setDelaySign(Integer delaySign) {
		this.delaySign = delaySign;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Long remindTime) {
		this.remindTime = remindTime;
	}

	public Integer getAutoDelaySign() {
		return autoDelaySign;
	}

	public void setAutoDelaySign(Integer autoDelaySign) {
		this.autoDelaySign = autoDelaySign;
	}

	public Long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Long delayTime) {
		this.delayTime = delayTime;
	}

	public Integer getDeleteSign() {
		return deleteSign;
	}

	public void setDeleteSign(Integer deleteSign) {
		this.deleteSign = deleteSign;
	}
}
