package com.purity.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {
	
	public static void main(String[] args){
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Staff> staffs = new ArrayList<Staff>();
			for (int i = 0; i < 3; i++){
				staffs.add(createDummyObject());
			}
			String json = mapper.writeValueAsString(23);
			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static Staff createDummyObject(){
		Staff staff = new Staff();
		staff.setName("cyanlong");
		staff.setAge(20);
		staff.setPosition("lalal");
		staff.setSalary(new BigDecimal("323222"));	
		List<String> skills = Arrays.asList("java", "python");
		staff.setSkills(skills);
		return staff;
	}
	
}
