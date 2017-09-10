package id.rustan.bakingapps.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import id.rustan.bakingapps.model.data.Recipe;


public interface HttpClient {


    String ENDPOINT = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/";

    @GET("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")

    Observable<List<Recipe>> getRecipes();

}
