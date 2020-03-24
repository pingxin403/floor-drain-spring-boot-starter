# floor-drain-spring-boot-starter

floor-drain是一款Web防护工具

![JDK](https://img.shields.io/badge/JDK-1.8-green.svg)


## 流程

![流程图](https://s1.ax1x.com/2020/03/24/8qQdiQ.png)



### 快速开始

#### 添加依赖

```xml
<dependency>
    <groupId>com.hyp.learn</groupId>
    <artifactId>floor-drain-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### 相关配置

```text
# 连续访问最高阀值，超过该值则认定为恶意操作的IP。单位：次 默认为20
floordrain.limit.access.threshold=20
# 间隔时间，在该时间内如果访问次数大于阀值，则记录为恶意IP，否则视为正常访问。单位：毫秒(ms)，默认为 5秒
floordrain.limit.access.interval=5000
# 当检测到恶意访问时，对恶意访问的ip进行限制的时间。单位：毫秒(ms)，默认为 1分钟
floordrain.limit.access.limitedTime=60000
# 黑名单存在的时间，在单位时间内用户访问受限的次数累加。单位：毫秒(ms)，默认为 1个月
floordrain.limit.access.blacklistTime=2592000000
# 缓存类型，默认为map存储,可选值（map、redis）
floordrain.limit.access.type=map
```

#### 开启Braum

在启动类上添加`@EnableFloorDrainConfiguration`注解

```java

@SpringBootApplication
@EnableFloorDrainConfiguration
public class FloorDrainSpringBootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloorDrainSpringBootTestApplication.class, args);
    }

}
```

#### 返回值

##### 正常
```text
{
	"code": 1,
	"msg": "[127.0.0.1]在5000毫秒内已连续发起 1 次请求",
	"expire": 0,
	"limitCount": 0,
	"accessInfo": {
		"ip": "127.0.0.1",
		"ua": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36 OPR/64.0.3417.61",
		"referer": null,
		"requestUrl": "http://127.0.0.1:8080/",
		"params": ""
	}
}
```

##### 被限制
```text
{
	"code": 0,
	"msg": "[127.0.0.1]涉嫌恶意访问已被临时限制！共被限制过[1]次，本次剩余限制时间:60000 ms",
	"expire": 60000,
	"limitCount": 1,
	"accessInfo": {
		"ip": "127.0.0.1",
		"ua": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36 OPR/64.0.3417.61",
		"referer": null,
		"requestUrl": "http://127.0.0.1:8080/",
		"params": ""
	}
}
```

| 字段  | 释义 |
| :------------: | :------------: |
| code | 响应码（1：正常，0：受限制） |
| msg | 返回内容 |
| expire | 当请求被限制时该值不为0，表示为被限制的剩余时间，单位毫秒 |
| limitCount | 当请求被限制时该值不为0，表示为被限制的次数 |
| accessInfo | 本次发起请求的内容 |

**`accessInfo`字段**

| 字段  | 释义 |
| :------------: | :------------: |
| ip | 当前访问IP |
| ua | 当前用户的UA |
| referer | 请求来源 |
| requestUrl | 当前请求的地址 |
| params | 当前请求的参数 |

#### 使用示例

1. 修改配置文件
    ```text
     # 连续访问最高阀值，超过该值则认定为恶意操作的IP。单位：次 默认为20
     floordrain.limit.access.threshold=2
     # 间隔时间，在该时间内如果访问次数大于阀值，则记录为恶意IP，否则视为正常访问。单位：毫秒(ms)，默认为 5秒
     floordrain.limit.access.interval=2000
     # 当检测到恶意访问时，对恶意访问的ip进行限制的时间。单位：毫秒(ms)，默认为 1分钟
     floordrain.limit.access.limitedTime=3000
     # 黑名单存在的时间，在单位时间内用户访问受限的次数累加。单位：毫秒(ms)，默认为 1个月
     floordrain.limit.access.blacklistTime=2592000000
     # 缓存类型，默认为map存储,可选值（map、redis）
     floordrain.limit.access.type=map
    ```
2. 在controller中处理
    ```java
    @RestController
    public class FloorDrainController {
    
        @Autowired
        FloorDrainProcessor processor;
        @Autowired
        HttpServletRequest request;
    
        @RequestMapping("/")
        public Object index() {
            FloorDrainResponse r = processor.process(request);
            if (r.getCode() == CommonConst.ERROR) {
                return "你已涉嫌恶意访问被临时禁止，请文明上网";
            }
            return "Hello world!";
        }
    }
    ```
3. 在拦截器中使用
    ```java
     @Component
     public class FloorDrainIntercepter implements HandlerInterceptor {
         private static final Logger log = LoggerFactory.getLogger(FloorDrainIntercepter.class);
         private static final int SUCCESS = 1;
         private static List<String> msgList = new ArrayList<>();
     
         static {
             msgList.add("Wow...您太冲动了，先喝杯咖啡冷静下。");
             msgList.add("Wow...一杯不够？那再来一杯。");
             msgList.add("还不够？再来一杯！");
             msgList.add("你就不怕被撑死么？");
             msgList.add("古恩吧，不接你这种客了");
             msgList.add("古恩!");
         }
     
         @Autowired
         private FloorDrainProcessor processor;
     
         @Override
         public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
             FloorDrainResponse r = processor.process(request);
             if (r.getCode() == SUCCESS) {
                 return true;
             }
             String msg = r.getLimitCount() > msgList.size() ? msgList.get(msgList.size() - 1) : msgList.get(r.getLimitCount() - 1);
             log.info(msg);
             response.setCharacterEncoding("UTF-8");
             response.setContentType("text/html;charset=utf-8");
             PrintWriter writer = response.getWriter();
             writer.write(msg);
             writer.flush();
             writer.close();
             return false;
         }
     }
    ```