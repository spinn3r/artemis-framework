import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
@Ignore
public class Spinn3rAuthTest {

    @Test
    public void test1() throws Exception {

        JestClientFactory factory = new JestClientFactory();

        factory.setHttpClientConfig(new HttpClientConfig
                                      .Builder("http://my_vendor.elasticsearch.spinn3r.com")
                                      .multiThreaded(true)
                                      .build());

        JestClient client = factory.getObject();

        String query = "{\n" +
                         "    \"size\": 1,\n" +
                         "    \"query\": {\n" +
                         "        \"query_string\" : {\n" +
                         "            \"query\" : \"main:Firefox\"\n" +
                         "        }\n" +
                         "    }  \n" +
                         "}";

        Search search = new Search.Builder(query)
                          // multiple index or types can be added.
                          .addIndex("content_*")
                          .addType("content")
                          .setHeader( "X-vendor", "my_vendor" )
                          .setHeader( "X-vendor-auth", "my_vendor_auth" )
                          .build();

        SearchResult result = client.execute(search);

        System.out.printf( "%s\n", result.getTotal() );

    }

}
