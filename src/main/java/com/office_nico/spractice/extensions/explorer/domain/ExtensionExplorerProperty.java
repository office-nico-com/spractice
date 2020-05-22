package com.office_nico.spractice.extensions.explorer.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.Data;

@Entity
@Table(name = "extension_explorer_properties")
@Data
public class ExtensionExplorerProperty {
	@Id
	@GeneratedValue
	@JsonProperty("id")
	private Long id;
	
	@Column(nullable = false)
	@JsonIgnore
	private Long extensionId = null;
	
	@Column(nullable = true)
	@JsonIgnore
	private Long parentId = null;

	@Column(nullable = false)
	@JsonIgnore
	private Short targetTypeCode = null;

	@Column(nullable = false)
	@JsonProperty("name")
	private String fileName = null;

	@Column(nullable = true)
	@JsonIgnore
	private Long iconBinaryFileId = null;

	@Column(nullable = false)
	@JsonProperty("kind")
	private String listKindName = null;

	@Column(nullable = false)
	@JsonProperty("prop_type")
	private String properyKindName = null;

	@Column(nullable = false)
	@JsonProperty("prop_discr")
	private String description = null;

	@Column(nullable = false)
	@JsonProperty("prop_location")
	private String location = null;

	@Column(nullable = true)
	@JsonProperty("prop_size")
	private String fileSize = null;

	@Column(nullable = true)
	@JsonProperty("size")
	private String listSize = null;

	@Column(nullable = true)
	@JsonProperty("prop_disk_size")
	private String diskSize = null;

	@Column(nullable = false)
	@JsonIgnore
	private LocalDateTime fileCreatedAt = null;

	@Column(nullable = false)
	@JsonIgnore
	private LocalDateTime fileUpdatedAt = null;

	@Column(nullable = false)
	@JsonIgnore
	private LocalDateTime fileAccessedAt = null;

	@Column(nullable = true)
	@JsonProperty("click")
	@JsonRawValue
	private String clickEvent = null;

	@Column(nullable = true)
	@JsonProperty("dblclick")
	@JsonRawValue
	private String dblclickEvent = null;
	
	@Column(nullable = false)
	@JsonIgnore
	private Integer orderNumber = null;

	@Column(nullable = false)
	@JsonIgnore
	private LocalDateTime createdAt = null;

	@Column(nullable = true)
	@JsonIgnore
	private LocalDateTime updatedAt = null;

	@Column(nullable = false)
	@JsonIgnore
	private Long createdBy = null;

	@Column(nullable = true)
	@JsonIgnore
	private Long updatedBy = null;
	
	@Transient
	@JsonProperty("children")
	private List<ExtensionExplorerProperty> extensionExplorerProperties = null;
	
	@JsonGetter("prop_created")
	public String _getFileCreatedAt() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy年M月d日、 H:mm:ss");
		return df.format(getFileCreatedAt());
	}

	@JsonGetter("prop_updated")
	public String _getFileUpdatedAt() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy年M月d日、 H:mm:ss");
		return df.format(getFileUpdatedAt());
	}

	@JsonGetter("prop_accessed")
	public String _getFileAccessedAt() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy年M月d日、 H:mm:ss");
		return df.format(getFileAccessedAt());
	}

	@JsonGetter("updated")
	public String _getListUpdatedAt() {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd H:mm");
		return df.format(getFileUpdatedAt());
	}

	@JsonGetter("type")
	public String _getTargetType() {
		return getTargetTypeCode() == 1 ? "file" : "folder";
	}

}
