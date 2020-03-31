package com.yyy.yyyasm_es;

import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
class YyyasmEsApplicationTests {



    @Test
    void contextLoads() {
        String a ="[\n" +
                "      {\n" +
                "        \"_index\" : \"book_index\",\n" +
                "        \"_type\" : \"books\",\n" +
                "        \"_id\" : \"1\",\n" +
                "        \"_score\" : 1.0,\n" +
                "        \"_source\" : {\n" +
                "          \"name\" : \"python\",\n" +
                "          \"desc\" : \"study book\",\n" +
                "          \"price\" : 40,\n" +
                "          \"tags\" : [\n" +
                "            \"a\",\n" +
                "            \"b\",\n" +
                "            \"c\",\n" +
                "            \"d\",\n" +
                "            \"e\"\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"_index\" : \"book_index\",\n" +
                "        \"_type\" : \"books\",\n" +
                "        \"_id\" : \"3\",\n" +
                "        \"_score\" : 1.0,\n" +
                "        \"_source\" : {\n" +
                "          \"name\" : \"javaee\",\n" +
                "          \"desc\" : \"study book\",\n" +
                "          \"price\" : 45,\n" +
                "          \"tags\" : [\n" +
                "            \"biancheng\"\n" +
                "          ]\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"_index\" : \"book_index\",\n" +
                "        \"_type\" : \"books\",\n" +
                "        \"_id\" : \"2\",\n" +
                "        \"_score\" : 1.0,\n" +
                "        \"_source\" : {\n" +
                "          \"name\" : \"java\",\n" +
                "          \"desc\" : \"study\",\n" +
                "          \"price\" : 31,\n" +
                "          \"tags\" : [\n" +
                "            \"a\",\n" +
                "            \"b\",\n" +
                "            \"c\",\n" +
                "            \"d\",\n" +
                "            \"e\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }\n" +
                "    ]";

        String b = "{\n" +
                "        \"_index\" : \"book_index\",\n" +
                "        \"_type\" : \"books\",\n" +
                "        \"_id\" : \"1\",\n" +
                "        \"_score\" : 1.0,\n" +
                "        \"_source\" : {\n" +
                "          \"name\" : \"python\",\n" +
                "          \"desc\" : \"study book\",\n" +
                "          \"price\" : 40,\n" +
                "          \"tags\" : [\n" +
                "            \"a\",\n" +
                "            \"b\",\n" +
                "            \"c\",\n" +
                "            \"d\",\n" +
                "            \"e\"\n" +
                "          ]\n" +
                "        }\n" +
                "      }";
        //字符串
        JsonParser jsonParser = new JsonParser();
        JsonObject asJsonObject = jsonParser.parse(b).getAsJsonObject();
        System.out.println(new Date()+"kai");
        System.out.println(asJsonObject);
        System.out.println(asJsonObject.get("_source").getAsJsonObject().get("tags"));

        //字符串数组
        JsonParser jsonParser1 = new JsonParser();
        JsonArray asJsonObject1 = jsonParser1.parse(a).getAsJsonArray();
        for (int i = 0; i < asJsonObject1.size(); i++) {
            JsonElement jsonElement = asJsonObject1.get(i);
            JsonObject as = jsonParser1.parse(String.valueOf(jsonElement)).getAsJsonObject();
            JsonElement source = as.get("_source");//.getAsJsonObject().get("tags");  获取第二级下面的
            System.out.println(source);
        }
        System.out.println(new Date()+"ting");

    }

}
