package com.enrico200165.utils.files;

public class TexLineRendererString implements ITexLineRenderer {

	@Override
	public String render(IRenderableAsTextLine e, long scanned, long included, int level) {
		return e.getLine();
	}

}
