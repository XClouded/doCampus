package com.doschool.zother;

import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class MJSONArray extends JSONArray {

	public MJSONObject getMJSONObject(int index) {
		try {

			return new MJSONObject(super.getJSONObject(index).toString());
		} catch (JSONException e) {
			return null;
		}
	}

	@Override
	public String getString(int index) {
		// TODO Auto-generated method stub
		try {
			return super.getString(index);
		} catch (JSONException e) {
			return null;
		}
	}

	public MJSONArray() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MJSONArray(Collection copyFrom) {
		super(copyFrom);
		// TODO Auto-generated constructor stub
	}

	public MJSONArray(JSONTokener readFrom) throws JSONException {
		super(readFrom);
		// TODO Auto-generated constructor stub
	}

	public MJSONArray(String json) throws JSONException {
		super(json);
		// TODO Auto-generated constructor stub
	}
}
