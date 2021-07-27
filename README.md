# spring-elasticwithjpa-search
spring-elasticwithjpa-search

Download elastic search 7.8.0 zip folder from officical site of elastic search.

After unzipping, go to config, edit elasticsearch.yml

add cluster name which you want and path where data need to be stored. e.g : cluster.name: poc-group-search path.data: D:\workspace\search {your project location}

Before running the project make sure elastic search is up first and then run your application.

Sample request - 

postman - 
localhost:9090/rest/search/all/{text} - all matched text will be searched. localhost:9090/rest/sear
localhost:9090/rest/search/customer/all/
localhost:9090/rest/search/customer/all/Mac
