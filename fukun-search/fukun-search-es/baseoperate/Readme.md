# 基本es操作
下面的基本es操作都是基于ES的7.X版本且都是使用可视化界面kibana进行操作，点击kibana界面左边栏的Dev Tools，点击Console选项卡即可。  

1、使用 GET / 获取es节点信息，包括es节点名、集群名、集群uuid、版本信息。  
返回如下：  
```
{
  "name" : "fukun_es_2",
  "cluster_name" : "fukun_es",
  "cluster_uuid" : "l_u3tVbfQnqkHiHLHhgb_g",
  "version" : {
    "number" : "7.2.0",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "508c38a",
    "build_date" : "2019-06-20T15:54:18.811730Z",
    "build_snapshot" : false,
    "lucene_version" : "8.0.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```
2、使用 GET /_cat/indices 获取索引信息。如下：  
```
yellow open shakespeare          MQXJMp3PRKGLg43zxs7mpw 1 1 0 0   283b   283b
green  open .kibana_1            pC8mQ5HHQ_-Hi4VEpGMpfw 1 0 3 0 14.8kb 14.8kb
yellow open weather              EaPpIaqMQe-8pZTJClG12A 1 1 0 0   283b   283b
yellow open customer             ezirYJS5TBWkmjihsLLo6g 1 1 3 1  8.4kb  8.4kb
green  open .kibana_task_manager JvBosMWtTL278e6eYhUL5g 1 0 2 0 29.6kb 29.6kb
yellow open accounts             NxnMZTuiRqigh5KQMJknlQ 1 1 1 0  4.8kb  4.8kb
```
3、使用 GET /_cat/health 获取集群的健康程度。如下：  
1563846961 01:56:01 fukun_es yellow 1 1 6 6 0 0 4 0 - 60.0%  

4、增加文档    
添加索引名 twitter，文档类型 _doc，id为1的索引，下面 twitter 表示索引，_doc 表示文档，1表示id。  
```
POST twitter/_doc/1
{
  "user":"GB",
  "uid": 1,
  "city":"Beijing",
  "province":"Beijing",
  "country":"China"
}
```
返回结果如下：  
```
{
  "_index" : "twitter",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 0,
  "_primary_term" : 1
}
```
5、使用 GET twitter/_doc/1 获取添加的索引信息，返回信息如下：  
```
{
  "_index" : "twitter",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "_seq_no" : 0,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "user" : "GB",
    "uid" : 1,
    "city" : "Beijing",
    "province" : "Beijing",
    "country" : "China"
  }
}
```
上面的 _source 表示的是索引中的文档内容。   

6、使用 PUT twitter/_doc/1 修改索引中的id为1的文档内容，如下： 
``` 
PUT twitter/_doc/1
{
   "user" : "GB",
    "uid" : 1,
    "city" : "北京",
    "province" : "北京",
    "country" : "中国",
    "location":{
      "lat":"29.084661",
      "lon":"111.335210"
    }
}
```
返回信息如下：  
```
{
  "_index" : "twitter",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 2,
  "result" : "updated",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 1,
  "_primary_term" : 1
}
```
然后使用 GET twitter/_doc/1 获取修改后的索引中的文档信息，查看是否修改成功，如下：  
```
{
  "_index" : "twitter",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 2,
  "_seq_no" : 1,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "user" : "GB",
    "uid" : 1,
    "city" : "北京",
    "province" : "北京",
    "country" : "中国",
    "location" : {
      "lat" : "29.084661",
      "lon" : "111.335210"
    }
  }
}
```
7、使用 POST twitter/_update_by_query 根据条件修改文档信息，将city和province为北京修改为city和province为上海，如下：  
```
POST twitter/_update_by_query
{
  "script":{
    "source":"ctx._source.city=params.city;ctx._source.province=params.province",
    "long":"painless",
    "params":{
      "city":"上海",
      "province":"上海"
    }
  },
  "query":{
    "match":{
      "user":"GB"
    }
    
  }
}
```
结果如下：  
```
{
  "took" : 183,
  "timed_out" : false,
  "total" : 1,
  "updated" : 1,
  "deleted" : 0,
  "batches" : 1,
  "version_conflicts" : 0,
  "noops" : 0,
  "retries" : {
    "bulk" : 0,
    "search" : 0
  },
  "throttled_millis" : 0,
  "requests_per_second" : -1.0,
  "throttled_until_millis" : 0,
  "failures" : [ ]
}
```
然后使用 GET twitter/_doc/1 获取根据条件修改后的索引中的文档信息，查看根据相关的条件是否修改成功，如下：  
```
{
  "_index" : "twitter",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 3,
  "_seq_no" : 2,
  "_primary_term" : 1,
  "found" : true,
  "_source" : {
    "uid" : 1,
    "country" : "中国",
    "province" : "上海",
    "city" : "上海",
    "location" : {
      "lon" : "111.335210",
      "lat" : "29.084661"
    },
    "user" : "GB"
  }
}
```
上面演示了根据id修改文档信息和根据相关的条件修改索引信息，es在修改信息方面显得很强大。  

8、使用 DELETE twitter 删除索引，返回如下的信息：  
```  
{
  "acknowledged" : true
}
```
9、使用 POST _bulk 批量插入数据，注意下面批量插入的格式，以行为单位，每行必须以\n结尾，
不然会报如下相关的错误：   

```
"reason": "Unexpected end-of-input: expected close marker for OBJECT (from [Source: [B@1ce48eee; line: 1, column: 0])\n at [Source: [B@1ce48eee; line: 1, column: 3]"
```  
```
POST _bulk
{"index":{"_index":"twitter"}}
{"user":"双榆树-张三","message":"今天天气不错，出去转转去","uid": 2,"age":20, "city":"北京","province":"北京", "country":"中国", "address":"中国北京市海淀区", "location":{ "lat":"39.970718", "lon":"116.325747"}}
{"index":{"_index":"twitter"}}
{"user":"东城区-老刘","message":"出发，下一站云南！","uid": 3, "age":30,"city":"北京","province":"北京","country":"中国","address":"中国北京市东城区台基厂三条3号", "location":{ "lat":"39.904313","lon":"116.412754"}}
{"index":{"_index":"twitter"}}
{"user":"东城区-李四","message":"Happy Birthday！", "uid": 4,"age":30, "city":"北京", "province":"北京","country":"中国","address":"中国北京市东城区", "location":{"lat":"39.893801","lon":"116.408986" }}
{"index":{"_index":"twitter"}}
{"user":"朝阳区-老贾", "message":"123,gogogo！","uid": 5,"age":35,"city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区建国门", "location":{ "lat":"39.718256", "lon":"116.367910" }}
{"index":{"_index":"twitter"}}
{ "user":"朝阳区-老王","message":"Happy Birthday！My Friend", "uid": 6,"age":50, "city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区国贸", "location":{"lat":"39.918256","lon":"116.467910" }}
{"index":{"_index":"twitter"}}
{ "user":"虹桥-老吴","message":"好友来了都今天我生日，好友来了，什么birthday happy 就成","uid": 7, "age":50, "city":"上海","province":"上海", "country":"中国","address":"中国上海市闵行区","location":{ "lat":"31.175927", "lon":"112.383328"}}

```
返回结果如下：  
```
{
  "took" : 72,
  "errors" : false,
  "items" : [
    {
      "index" : {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "5x_sHGwB01YPn65fACZH",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 6,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "index" : {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "6B_sHGwB01YPn65fACZI",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 7,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "index" : {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "6R_sHGwB01YPn65fACZI",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 8,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "index" : {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "6h_sHGwB01YPn65fACZI",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 9,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "index" : {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "6x_sHGwB01YPn65fACZI",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 10,
        "_primary_term" : 1,
        "status" : 201
      }
    },
    {
      "index" : {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "7B_sHGwB01YPn65fACZI",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "_seq_no" : 11,
        "_primary_term" : 1,
        "status" : 201
      }
    }
  ]
}
```
上面就表示批量插入成功。  

10、上面执行了批量操作，下面使用 GET twitter/_search 查询插入的结果，如下： 
``` 
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 6,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "7R_6HGwB01YPn65fZCaN",
        "_score" : 1.0,
        "_source" : {
          "user" : "双榆树-张三",
          "message" : "今天天气不错，出去转转去",
          "uid" : 2,
          "age" : 20,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市海淀区",
          "location" : {
            "lat" : "39.970718",
            "lon" : "116.325747"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "7h_6HGwB01YPn65fZCaN",
        "_score" : 1.0,
        "_source" : {
          "user" : "东城区-老刘",
          "message" : "出发，下一站云南！",
          "uid" : 3,
          "age" : 30,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市东城区台基厂三条3号",
          "location" : {
            "lat" : "39.904313",
            "lon" : "116.412754"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "7x_6HGwB01YPn65fZCaN",
        "_score" : 1.0,
        "_source" : {
          "user" : "东城区-李四",
          "message" : "Happy Birthday！",
          "uid" : 4,
          "age" : 30,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市东城区",
          "location" : {
            "lat" : "39.893801",
            "lon" : "116.408986"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "8B_6HGwB01YPn65fZCaN",
        "_score" : 1.0,
        "_source" : {
          "user" : "朝阳区-老贾",
          "message" : "123,gogogo！",
          "uid" : 5,
          "age" : 35,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市朝阳区建国门",
          "location" : {
            "lat" : "39.718256",
            "lon" : "116.367910"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "8R_6HGwB01YPn65fZCaN",
        "_score" : 1.0,
        "_source" : {
          "user" : "朝阳区-老王",
          "message" : "Happy Birthday！My Friend",
          "uid" : 6,
          "age" : 50,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市朝阳区国贸",
          "location" : {
            "lat" : "39.918256",
            "lon" : "116.467910"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "8h_6HGwB01YPn65fZCaN",
        "_score" : 1.0,
        "_source" : {
          "user" : "虹桥-老吴",
          "message" : "好友来了都今天我生日，好友来了，什么birthday happy 就成",
          "uid" : 7,
          "age" : 50,
          "city" : "上海",
          "province" : "上海",
          "country" : "中国",
          "address" : "中国上海市闵行区",
          "location" : {
            "lat" : "31.175927",
            "lon" : "112.383328"
          }
        }
      }
    ]
  }
}

```
说明第九项批量插入成功。  

11、使用 GET twitter/_mapping 获取索引的映射信息，mapping相当于数据库的库表结构，这里指的是index的一种结构，返回信息如下：  
```
{
  "twitter" : {
    "mappings" : {
      "properties" : {
        "address" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "age" : {
          "type" : "long"
        },
        "city" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "country" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "location" : {
          "properties" : {
            "lon" : {
              "type" : "text",
              "fields" : {
                "keyword" : {
                  "type" : "keyword",
                  "ignore_above" : 256
                }
              }
            },
            "lat" : {
              "type" : "text",
              "fields" : {
                "keyword" : {
                  "type" : "keyword",
                  "ignore_above" : 256
                }
              }
            }
          }
        },
        "message" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "province" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "uid" : {
          "type" : "long"
        },
        "user" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        }
      }
    }
  }
}
```
**上面的mapping中的type为text的类型可以用于全文搜索，keyword类型可以用于聚合查询和统计分析，上面有些字段类型即是text又是keyword，表明该字段即能用于全文搜索，
又能进行统计分析，支持不同的用途，发现上面执行批量插入后，mapping是自动发生的，即自动产生了mapping，通常叫它Dynamic Mapping，即动态mapping。
但是动态mapping有时会有问题，比如上面的location字段没有mapping正确，没有映射为地理位置（geo_point）的相关类型，而是mapping成text了，这就出现问题了。
那遇到这种问题怎么解决呢？必须重新配置mapping，下面就进行重新配置mapping的操作。**  

12、首先执行 DELETE twitter，删除这个索引，然后执行如下命令：  
```
PUT twitter
{
  "settings":{"number_of_shards": 1}
}
```
**上面设置了一个分片，7.X版本之前默认是5个分片，到了7.X后，默认都是一个分片。**       

13、重新定义mapping，让 location 映射成 geo_point类型，正确设置成地理位置，如下：  
```
PUT twitter/_mapping
{
  "properties": {
    "address":{
      "type":"text",
      "fields": {
        "keyword":{
          "type":"keyword",
          "ignore_above":256
        }
      }
    },
    "city":{
      "type":"keyword"
    },
    "province":{
      "type":"keyword"
    },
     "country":{
      "type":"keyword"
    },
     "location":{
      "type":"geo_point"
    },
     "uid":{
      "type":"long"
    },
    "user":{
      "type":"text",
      "fields": {
        "keyword":{
          "type":"keyword",
          "ignore_above":256
        }
      }
    }
  }
}
```
keyword类型的保存比text类型的保存节省内存空间，再次通过 GET twitter/_mapping 获取索引的映射信息，如下：  
```
{
  "twitter" : {
    "mappings" : {
      "properties" : {
        "address" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
        "city" : {
          "type" : "keyword"
        },
        "country" : {
          "type" : "keyword"
        },
        "location" : {
          "type" : "geo_point"
        },
        "province" : {
          "type" : "keyword"
        },
        "uid" : {
          "type" : "long"
        },
        "user" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        }
      }
    }
  }
}
```
然后再次批量插入之前的数据，如下： 
``` 
POST _bulk
{"index":{"_index":"twitter"}}
{"user":"双榆树-张三","message":"今天天气不错，出去转转去","uid": 2,"age":20, "city":"北京","province":"北京", "country":"中国", "address":"中国北京市海淀区", "location":{ "lat":"39.970718", "lon":"116.325747"}}
{"index":{"_index":"twitter"}}
{"user":"东城区-老刘","message":"出发，下一站云南！","uid": 3, "age":30,"city":"北京","province":"北京","country":"中国","address":"中国北京市东城区台基厂三条3号", "location":{ "lat":"39.904313","lon":"116.412754"}}
{"index":{"_index":"twitter"}}
{"user":"东城区-李四","message":"Happy Birthday！", "uid": 4,"age":30, "city":"北京", "province":"北京","country":"中国","address":"中国北京市东城区", "location":{"lat":"39.893801","lon":"116.408986" }}
{"index":{"_index":"twitter"}}
{"user":"朝阳区-老贾", "message":"123,gogogo！","uid": 5,"age":35,"city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区建国门", "location":{ "lat":"39.718256", "lon":"116.367910" }}
{"index":{"_index":"twitter"}}
{ "user":"朝阳区-老王","message":"Happy Birthday！My Friend", "uid": 6,"age":50, "city":"北京","province":"北京","country":"中国","address":"中国北京市朝阳区国贸", "location":{"lat":"39.918256","lon":"116.467910" }}
{"index":{"_index":"twitter"}}
{ "user":"虹桥-老吴","message":"好友来了都今天我生日，好友来了，什么birthday happy 就成","uid": 7, "age":50, "city":"上海","province":"上海", "country":"中国","address":"中国上海市闵行区","location":{ "lat":"31.175927", "lon":"112.383328"}}

PUT twitter/_doc/1
{
   "user" : "GB",
    "uid" : 1,
    "city" : "北京",
    "province" : "北京",
    "country" : "中国",
    "location":{
      "lat":"29.084661",
      "lon":"111.335210"
    }
}
```
再次使用 GET twitter/_search 查看是否插入成功，发现  
```
 "total" : {
      "value" : 7,
      "relation" : "eq"
    },
```
中的value值为7，说明插入成功。  

下面演示怎么去查询。  

14、查询city为北京的索引记录，如下：  
```
GET twitter/_search
{
  "query":{
    "match": {
      "city": "北京"
    }
  }
  
}
```
返回结果为6条。  

15、查询city是北京并且年龄是30的索引记录，如下：  

```
GET twitter/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "city": "北京"
        }},
        {"match": {
          "age": 30
        } }
      ]
    }
  }
}
```
返回结果为2条记录。  

16、查询city不在北京的索引记录，如下： 
``` 
GET twitter/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "city": "北京"
        }},
        {"match": {
          "age": 30
        } }
      ]
    }
  }
}
```
查到了city为上海的索引记录且只有一条。   

17、查询city即是北京又是上海的索引记录，前面使用的bool表达式中的must，下面使用bool表达式中的
should，如下：  
```
GET twitter/_search
{
  "query": {
    "bool": {
      "should": [
        {"match": {
          "city": "北京"
        }},
        {"match": {
          "city": "上海"
        } }
      ]
    }
  }
}
```
上面应该查到7条索引记录，把city为北京和上海的索引记录都查出来。  

18、查询city即是北京又是上海的索引记录的总数，如下：  
```
GET twitter/_count
{
  "query": {
    "bool": {
      "should": [
        {"match": {
          "city": "北京"
        }},
        {"match": {
          "city": "上海"
        } }
      ]
    }
  }
}
```
返回结果如下：  
```
{
  "count" : 7,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  }
}
```
上面说的都是最基础的查询，下面说几个复杂一点查询，如下：  

19、查询地址是北京，地理位置距离设置的经纬度3KM的索引记录，首先按照查询条件筛选出地址是北京，然后
对查询的结果按照地理位置进行过滤，如下：  
```
GET twitter/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "address": "北京"
        }}
      ]
    }
  },
  "post_filter": {
    "geo_distance": {
      "distance": "3km",
      "location": {
        "lat": 39.920086,
        "lon": 116.454182
      }
    }
  }
}
```
距离为3KM，查询结果只有一条索引记录；设置距离为5KM，发现查到3条索引记录。   

20、查询地址是北京，距离设置的经纬度为5公里，并且按照地理位置的距离升序排序的索引记录，如下：  
```
GET twitter/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "address": "北京"
        }}
      ]
    }
  },
  "post_filter": {
    "geo_distance": {
      "distance": "5km",
      "location": {
        "lat": 39.920086,
        "lon": 116.454182
      }
    }
  },"sort": [
    {
      "_geo_distance": {
        "location": {
          "lat": 39.920086,
          "lon": 116.454182
        }, 
        "order": "asc",
        "unit": "km"
      }
    }
  ]
}    
```
21、查询age大于等于30岁小于等于40岁的索引记录，并且按照年龄降序排序，如下： 
``` 
GET twitter/_search
{
  "query": {
  "range": {
    "age": {
      "gte": 30,
      "lte": 40
    }
  }
  },
  "sort": [
    {
      "age": {
        "order": "desc"
      }
    }
  ]
}
```
22、查询message包含 happy birthday 的索引记录，如下：     
```
GET twitter/_search
{
  "query": {
    "match": {
      "message": "happy birthday"
    }
 
  }
}
```
查询结果只要message中包含 happy birthday 都会被查出来，而且不区分大小写，Happy Birthday也查出来了，并且 happy birthday 倒过来，即包含 birthday happy 的索引记录也会被查出来了。  
那么怎么过滤掉倒过来的birthday happy，只查询不区分大小写的happy birthday的索引记录呢？ 如下：  
```
GET twitter/_search
{
  "query": {
    "match_phrase": {
      "message": "happy birthday"
    }
 
  }
}
```
上面的结果说明了默认的分词是不区分大小写的。  

23、对查询的结果进行高亮的展示，如下：  

```
GET twitter/_search
{
  "query": {
    "match_phrase": {
      "message": "happy birthday"
    }
  },
  "highlight": {
    "fields": {
      "message": {}
    }
  }
}
```
从结果可以发现message变成了如下的模样：  
```
 "highlight" : {
          "message" : [
            "<em>Happy</em> <em>Birthday</em>！"
          ]
        }
```
对内容加上了<em>标签，对happy和birthday进行高亮显示，用于返回给前端高亮显示。   

前面我们说了单个条件的查询和复合条件的查询等相关的搜索与查询，下面我们说一下相关的
聚合操作。  

24、只要是聚合分析的都是以aggs开头的key，统计不同年龄段的人数，如下：  

```
GET twitter/_search
{
  "size":0,
  "aggs": {
    "age": {
      "range": {
        "field":"age",
        "ranges":[
          {
            "from":20,
            "to":30
          },
           {
            "from":30,
            "to":40
          },
           {
            "from":40,
            "to":50
          }
          ]
        
      }
    }
  } 
} 
```
上面的内容 size = 0，表示命中的内容不展示，但是如果把size设置不为0，比如为10，那么命中的内容
就会随着统计信息一起展示。  
size = 0 的结果如下（只展示aggregations部分）：  
```
"aggregations" : {
    "age" : {
      "buckets" : [
        {
          "key" : "20.0-30.0",
          "from" : 20.0,
          "to" : 30.0,
          "doc_count" : 1
        },
        {
          "key" : "30.0-40.0",
          "from" : 30.0,
          "to" : 40.0,
          "doc_count" : 3
        },
        {
          "key" : "40.0-50.0",
          "from" : 40.0,
          "to" : 50.0,
          "doc_count" : 0
        }
      ]
    }
  }
```
从上面的结果可以看出，年龄在20岁到30岁的人数为1人，30岁到40岁的人数为3人，没有年龄在40岁到50岁的人。  
size = 10 的结果如下：  
```
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 7,
      "relation" : "eq"
    },
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "-R9kHWwB01YPn65fkSbk",
        "_score" : 1.0,
        "_source" : {
          "user" : "双榆树-张三",
          "message" : "今天天气不错，出去转转去",
          "uid" : 2,
          "age" : 20,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市海淀区",
          "location" : {
            "lat" : "39.970718",
            "lon" : "116.325747"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "-h9kHWwB01YPn65fkSbk",
        "_score" : 1.0,
        "_source" : {
          "user" : "东城区-老刘",
          "message" : "出发，下一站云南！",
          "uid" : 3,
          "age" : 30,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市东城区台基厂三条3号",
          "location" : {
            "lat" : "39.904313",
            "lon" : "116.412754"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "-x9kHWwB01YPn65fkSbk",
        "_score" : 1.0,
        "_source" : {
          "user" : "东城区-李四",
          "message" : "Happy Birthday！",
          "uid" : 4,
          "age" : 30,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市东城区",
          "location" : {
            "lat" : "39.893801",
            "lon" : "116.408986"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "_B9kHWwB01YPn65fkSbk",
        "_score" : 1.0,
        "_source" : {
          "user" : "朝阳区-老贾",
          "message" : "123,gogogo！",
          "uid" : 5,
          "age" : 35,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市朝阳区建国门",
          "location" : {
            "lat" : "39.718256",
            "lon" : "116.367910"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "_R9kHWwB01YPn65fkSbk",
        "_score" : 1.0,
        "_source" : {
          "user" : "朝阳区-老王",
          "message" : "Happy Birthday！My Friend",
          "uid" : 6,
          "age" : 50,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "address" : "中国北京市朝阳区国贸",
          "location" : {
            "lat" : "39.918256",
            "lon" : "116.467910"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "_h9kHWwB01YPn65fkSbk",
        "_score" : 1.0,
        "_source" : {
          "user" : "虹桥-老吴",
          "message" : "好友来了都今天我生日，好友来了，什么birthday happy 就成",
          "uid" : 7,
          "age" : 50,
          "city" : "上海",
          "province" : "上海",
          "country" : "中国",
          "address" : "中国上海市闵行区",
          "location" : {
            "lat" : "31.175927",
            "lon" : "112.383328"
          }
        }
      },
      {
        "_index" : "twitter",
        "_type" : "_doc",
        "_id" : "1",
        "_score" : 1.0,
        "_source" : {
          "user" : "GB",
          "uid" : 1,
          "city" : "北京",
          "province" : "北京",
          "country" : "中国",
          "location" : {
            "lat" : "29.084661",
            "lon" : "111.335210"
          }
        }
      }
    ]
  },
  "aggregations" : {
    "age" : {
      "buckets" : [
        {
          "key" : "20.0-30.0",
          "from" : 20.0,
          "to" : 30.0,
          "doc_count" : 1
        },
        {
          "key" : "30.0-40.0",
          "from" : 30.0,
          "to" : 40.0,
          "doc_count" : 3
        },
        {
          "key" : "40.0-50.0",
          "from" : 40.0,
          "to" : 50.0,
          "doc_count" : 0
        }
      ]
    }
  }
}
```
聚合的时候只需要统计结果，不需要把命中的信息都展示出来，所以一般设置size为0。  

25、查询message包含happy birthday的索引记录，并且对查询的结果按照city进行分组，如下：  

```
GET twitter/_search
{
  "query": {
    "match": {
      "message": "happy birthday"
    }
  },
  "size":0,
  "aggs": {
    "age": {
      "terms": {
        "field":"city",
        "size": 10
        
      }
    }
  }
}
```
查询结果如下：  
```
{
  "took" : 9,
  "timed_out" : false,
  "_shards" : {
    "total" : 1,
    "successful" : 1,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : {
      "value" : 3,
      "relation" : "eq"
    },
    "max_score" : null,
    "hits" : [ ]
  },
  "aggregations" : {
    "age" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "北京",
          "doc_count" : 2
        },
        {
          "key" : "上海",
          "doc_count" : 1
        }
      ]
    }
  }
}
```
上面查出2个人在北京，一个人在上海，并且按照城市city进行分组，可以将size = 0 设置 size = 3，详细查看
哪三个人被命中。  

下面说一下分词的使用，分词和分析器是比较重要的，通过分析器对文本进行分词并且过滤操作，生成倒排索引供我们搜索的时候使用。    
一般的分析器 analyzer 有三部分组成，一个是char filter，对字符进行过滤；一个是tokenizer，即分词器，对一个文本进行分词；
最后一个是filter，对分开的词进行过滤。

26、使用 standard 分词器进行分词，查看它是怎么对一个文本进行分词的。如下：  
```  
GET twitter/_analyze
{
  "text":["Happy Birthday"],
  "analyzer": "standard"
}
```
结果如下：  
```
{
  "tokens" : [
    {
      "token" : "happy",
      "start_offset" : 0,
      "end_offset" : 5,
      "type" : "<ALPHANUM>",
      "position" : 0
    },
    {
      "token" : "birthday",
      "start_offset" : 6,
      "end_offset" : 14,
      "type" : "<ALPHANUM>",
      "position" : 1
    }
  ]
}
```
从上面的结果可以看出，标准的分析器把Happy Birthday以中间的空格分成了happy和birthday两个词，并且Happy和Birthday都变成了小写，
但是如果"Happy Birthday" 中间的空格是一个点，那么使用标准的分词器就无法进行分词了，如下：  
```
{
  "tokens" : [
    {
      "token" : "Happy.Birthday",
      "start_offset" : 0,
      "end_offset" : 14,
      "type" : "<ALPHANUM>",
      "position" : 0
    }
  ]
}
```
发现 Happy.Birthday 没有分开。  

那么对于这种情况能不能使用其他的分词器进行分呢？当然有，如下：  
```
GET twitter/_analyze
{
  "text":["Happy.Birthday"],
   "analyzer": "simple"
}
```
执行以上的请求，就可以按照点进行分词了，并且Happy和Birthday都变成了小写。  
其实在前面设置mapping的时候我们可以针对某个字段指定分词器，如果不指定，默认就是使用标准的
分词器。如下： 
``` 
"user" : {
          "type" : "text",
          "analyzer":"standard",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        }
```
当然了我们可以指定用户自己的分词器，或者ik中文分词器。    

能不能自己设置分词器与filter呢？当然可以，如下的操作首先选择一个分词器，然后
对分词器分过的词进行过滤，比如变成小写，如下：  
```
GET twitter/_analyze
{
  "text":["Happy Birthday"],
   "tokenizer": "standard",
   "filter": ["lowercase"]
}
```
返回结果如下：  
```
{
  "tokens" : [
    {
      "token" : "happy",
      "start_offset" : 0,
      "end_offset" : 5,
      "type" : "<ALPHANUM>",
      "position" : 0
    },
    {
      "token" : "birthday",
      "start_offset" : 6,
      "end_offset" : 14,
      "type" : "<ALPHANUM>",
      "position" : 1
    }
  ]
}
```



 




  









