package com.chengzhang.mupdfreader.app.model;

import com.chengzhang.mupdfreader.app.constants.ChooseType;

public class ChoosePDFItem {
	 public ChooseType type;
	 public String name;

	public ChoosePDFItem (ChooseType t, String n) {
		type = t;
		name = n;
	}
}
