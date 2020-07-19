/*
 *  @Author: Ashish Mallick
 *  linkedIn : https://www.linkedin.com/in/ashish-mallick
 */


package com.ashish.GraphQLwithSpringBoot.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ashish.GraphQLwithSpringBoot.Output.Cast;
import com.ashish.GraphQLwithSpringBoot.Output.Movie;
import com.ashish.GraphQLwithSpringBoot.Repository.MovieRepository;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;



@RestController
public class MovieController {

	@Autowired
	private MovieRepository movieRepository; 	
	
	private Movie movie1;
	private Movie movie2;
	private Movie movie3;
	private Movie movie4;
	private Movie movie5;
	private List<Movie> movieList;

	private void initializeMovies() {

		Cast cast1 = new Cast(12, "ABC", "New York", 26);
		Cast cast2 = new Cast(13, "DEF", "California", 35);
		Cast cast3 = new Cast(14, "GHI", "New Jersey", 73);
		Cast cast4 = new Cast(15, "JKL", "Indiana", 64);
		Cast cast5 = new Cast(16, "MNO", "Iowa", 73);
		Cast[] castList1 = {cast1,cast2};
		Cast[] castList2 = {cast2,cast3};
		Cast[] castList3 = {cast4,cast5};
		Cast[] castList4 = {cast1,cast5};
		Cast[] castList5 = {cast2,cast4};
		movie1 = new Movie(101, "Frozen", "U", Double.valueOf(6.7d) , castList1 ); 
		movie2 = new Movie(103, "Avengers - The End Game", "U/A", Double.valueOf(7.3d), castList2); 
		movie3 = new Movie(107, "Conjuring 2", "A", Double.valueOf(8d), castList3); 
		movie4 = new Movie(109, "A Beautiful House", "U", Double.valueOf(8.6d), castList4); 
		movie5 = new Movie(110,"Andhadhun","U/A", Double.valueOf(9d), castList5);
		 

		movieList = new ArrayList<Movie>();
		movieList.addAll(movieList);

	}
	
	private void loadAllInHSQL() {
		movieRepository.saveAll(movieList);
	}
	
	private GraphQL  graphQL;
	
	@Value("classpath:movie.graphqls")
	private Resource schemaResource;
	
	
	
	
	@PostConstruct
	public void loadSchema() throws IOException {
		initializeMovies();
		loadAllInHSQL();
		File schemaFile = schemaResource.getFile();
		TypeDefinitionRegistry  typedefRegistry = new SchemaParser().parse(schemaFile);
		RuntimeWiring runtimeWiring = buildWiring();
		GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typedefRegistry, runtimeWiring);
		graphQL = GraphQL.newGraphQL(graphQLSchema).build();
	}
	
	
	private RuntimeWiring buildWiring() {
		DataFetcher<List<Movie>> allMovieDataFetcher = data ->{
			return (List<Movie>)movieRepository.findAll();
		};
		DataFetcher<Movie> findMovieDataFetcher = data ->{
			return (Movie) movieRepository.findMovie(data.getArgument("movieName"));
		};
		
		return RuntimeWiring.newRuntimeWiring().
				type("Query", 
						  typeWriting -> typeWriting.dataFetcher("getAllMovieData", allMovieDataFetcher)
						  							.dataFetcher("findMovie", findMovieDataFetcher)
						 ).build();
		
	}
	
	@PostMapping("/getAllMovieData")
	public ResponseEntity<List<Movie>> getAllMovieData(@RequestBody String query){
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<List<Movie>>((List<Movie>) result, HttpStatus.OK);
		
	}
	
	@PostMapping("/findMovie")
	public ResponseEntity<Movie> findMovie(@RequestBody String query){
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<Movie>((Movie) result, HttpStatus.OK);
		
	}
	
	@PostMapping("/addMovie")
	public String addMovies(@RequestBody List<Movie> movies) {
		movieRepository.saveAll(movies);
		return "record inserted " + movies.size();
	}
	
	
}
