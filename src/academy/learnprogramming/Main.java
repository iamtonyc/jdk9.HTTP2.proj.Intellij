package academy.learnprogramming;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpHeaders;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void  main(String[] args) throws Exception{
        String url="http://www.example.com";
        //getURLSync(url);
        //postURLSync(url);
        getURLAsync(url);
    }

    public static void getURLAsync(String url) throws Exception{
        HttpClient client =HttpClient.newHttpClient();

        CompletableFuture<HttpResponse<String>> compFuture=client.sendAsync(
                HttpRequest
                    .newBuilder(new URI(url))
                    .GET()
                    .build(),
                HttpResponse.BodyHandler.asString());

            System.out.println("Async request had been made....");

            while(!compFuture.isDone())
                    System.out.println("Doing some else");

            System.out.println("Async request coomplete");
            processResponse(compFuture.get());
    }

    public static void postURLSync(String url) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse response=client.send(
                HttpRequest
                    .newBuilder(new URI(url))
                    .headers("Foo","Foo-val","Bar","Bar-val")
                    .POST(HttpRequest.BodyProcessor.fromString("This is the String"))
                    .build(),
                HttpResponse.BodyHandler.asFile(Paths.get("fileXXXXXXX.txt"))
        );

        processResponse(response);

    }

    public static void getURLSync(String url) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request=HttpRequest
                                .newBuilder(new URI(url))
                                .GET()
                                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandler.asString() );
        processResponse(response);
    }

    public static void processResponse(HttpResponse response){
        System.out.println("Status code:"+ response.statusCode());
        System.out.println("Body :" + response.body());
        System.out.println("Headers:");
        HttpHeaders header=response.headers();
        Map<String, List<String>> map=header.map();
        map.forEach((k,v)-> System.out.println("\t"+k+":"+v));
    }
}
