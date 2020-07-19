/*
 *  @Author: Ashish Mallick
 *  linkedIn : https://www.linkedin.com/in/ashish-mallick
 */


package com.ashish.GraphQLwithSpringBoot.Output;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Getter
@Setter
public class Cast implements  Serializable {

	@Id
	private Integer actorId;
	
	private String actorName;
	private String city;
	private Integer  age;
	
	public Cast(Integer actorId, String actorName, String city, Integer age) {
		this.actorId = actorId;
		this.actorName = actorName;
		this.city = city;
		this.age = age;
	}

	public Cast() {
		
	}
	
	
}
