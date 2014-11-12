package com.doschool.zother;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MJSONObject extends JSONObject {

	public MJSONArray getMJSONArray(String name) {
		// TODO Auto-generated method stub
		try {
			return new MJSONArray(super.getJSONArray(name).toString());
		} catch (JSONException e) {
			return new MJSONArray();
		}
	}

	public MJSONObject getMJSONObject(String name) {
		try {
			return new MJSONObject(super.getJSONObject(name).toString());
		} catch (JSONException e) {
			return new MJSONObject();
		}

	}

	@Override
	public double getDouble(String name) {
		try {
			return super.getDouble(name);
		} catch (JSONException e) {
			return 0.0;
		}
	}

	public int getInt(String name, int def) {
		try {
			return super.getInt(name);
		} catch (JSONException e) {
			return def;
		}
	}

	@Override
	public long getLong(String name) {
		try {
			return super.getLong(name);
		} catch (JSONException e) {
			return 0l;
		}
	}

	@Override
	public String getString(String name) {
		try {
			return super.getString(name);
		} catch (JSONException e) {
			return "";
		}
	}

	public MJSONObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MJSONObject(JSONObject copyFrom, String[] names) throws JSONException {
		super(copyFrom, names);
		// TODO Auto-generated constructor stub
	}

	public MJSONObject(JSONTokener readFrom) throws JSONException {
		super(readFrom);
		// TODO Auto-generated constructor stub
	}

	public MJSONObject(Map copyFrom) {
		super(copyFrom);
		// TODO Auto-generated constructor stub
	}

	public MJSONObject(String json) throws JSONException {
		super(json);
		// TODO Auto-generated constructor stub
	}

}
