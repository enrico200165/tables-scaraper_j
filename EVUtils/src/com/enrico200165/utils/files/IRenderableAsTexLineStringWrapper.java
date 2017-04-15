package com.enrico200165.utils.files;

/**
 * @author enrico
 * To allow using String as IrenderableAsTextLine :-)
 */
public class IRenderableAsTexLineStringWrapper implements IRenderableAsTextLine {
	
	
	public IRenderableAsTexLineStringWrapper() {
		this.str = "";
	}

	public IRenderableAsTexLineStringWrapper(String s) {
		this.str = s;
	}

	public IRenderableAsTexLineStringWrapper concat(String s) {
		str += s;
		return this;
	}

	public IRenderableAsTexLineStringWrapper concat(String s, String sep) {
		str = str+sep+s;
		return this;
	}

	
	@Override
	public String getLine() {
		// TODO Auto-generated method stub
		return str;
	}

	
	String str;

}
