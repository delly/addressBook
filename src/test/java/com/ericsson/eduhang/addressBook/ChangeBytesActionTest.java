package com.ericsson.eduhang.addressBook;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class ChangeBytesActionTest implements Action {
	
	private final byte[] result;

	 public ChangeBytesActionTest(final byte[] result) {  
	        this.result = result;  
	    }  
	 
	@Override
	public void describeTo(final Description description) {
		 description.appendText("change ").appendValueList("", ", ", "", result)  
         .appendText(" value");  
	}

	@Override
	public Object invoke(final Invocation invocation) throws Throwable {
		final byte[] bytesArray = (byte[]) invocation.getParametersAsArray()[0];  
        for (int i = 0; i < result.length; i++) {  
            bytesArray[i] = result[i];  
        }  
        return result.length;  
    }  

}
