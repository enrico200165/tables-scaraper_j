package com.enrico200165.utils.files;



/**
 * @author enrico
 * calculates whether the item passed to it must be included in a rendering or not
 * used within loops to render lists of items
 */
public interface IEVRenderableFilter {
	
	boolean includeIt(IRenderableAsTextLine e);

}
