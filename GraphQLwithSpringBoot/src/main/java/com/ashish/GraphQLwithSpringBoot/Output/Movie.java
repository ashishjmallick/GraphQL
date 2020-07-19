/*
 *  @Author: Ashish Mallick
 *  linkedIn : https://www.linkedin.com/in/ashish-mallick
 */


package com.ashish.GraphQLwithSpringBoot.Output;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.annotations.Beta;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Movie {

	@Id
	private Integer movieId;
	
	private String movieName;
	private String category;
	private float imdbRating;
	private String[] castlist;
	
	public Movie(Integer movieId, String movieName, String category , float imdbRating  , String[] castlist ) {
		this.movieId = movieId;
		this.movieName = movieName;
		this.category = category;
		this.imdbRating = imdbRating;
		this.castlist = castlist;
	}

	public Movie() {
		
	}

	
}
