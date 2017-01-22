package com.purity.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegexTest {

	public RegexTest() {
	}
	
	public static void main(String[] str){
		String str1 = "user/1234/fe";
		String str2 = "user/:userId/fe";
		
		String[] strArr1 = str1.split("/");
		String[] strArr2 = str2.split("/");
		System.out.println(Arrays.toString(strArr1));
		System.out.println(Arrays.toString(strArr2));
		Map<String, String> res = new HashMap<String, String>();
		
		boolean ismatch = true;
		if (strArr1.length != strArr2.length) 
			ismatch = false;
		if (strArr1.length  == strArr2.length){
			for (int i = 0; i < strArr1.length; i++){
				if (!strArr1[i].equals(strArr2[i])){
					if (strArr2[i].startsWith(":")){
						res.put(strArr2[i].substring(1, strArr2[i].length()), 
								strArr1[i]);
					} else {
						ismatch = false;
						break;
					}
				}
			}
		}
		
		System.out.println("match:" + ismatch);
		System.out.println(res);
	}
	
}
