/**
 * Created DataPage.java at 2:21:35 PM by hungvq
 * TODO
 */
package com.vega.samo.db.page;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataPage<T> extends Pageable {
	/**
   * 
   */
  private static final long serialVersionUID = -3217572918447688728L;
  private List<T> data;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
}
