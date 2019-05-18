# springboot_chainofresponsibility
网关权限控制_责任链模式
责任链模式
什么是责任链模式

客户端发出一个请求，链上的对象都有机会来处理这一请求，而客户端不需要知道谁是具体的处理对象。这样就实现了请求者和接受者之间的解耦，并且在客户端可以实现动态的组合职责链。使编程更有灵活性。

定义：使多个对象都有机会处理请求，从而避免了请求的发送者和接受者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有对象处理它为止。其过程实际上是一个递归调用。

要点主要是：
　1、有多个对象共同对一个任务进行处理。
  2、这些对象使用链式存储结构，形成一个链，每个对象知道自己的下一个对象。
  3、一个对象对任务进行处理，可以添加一些操作后将对象传递个下一个任务。也可以在此对象上结束任务的处理，并结束任务。
  4、客户端负责组装链式结构，但是客户端不需要关心最终是谁来处理了任务。
责任链模式类结构图

　1.抽象处理者(Handler)角色：定义出一个处理请求的接口。如果需要，接口可以定义 出一个方法以设定和返回对下家的引用。这个角色通常由一个Java抽象类或者Java接口实现。上图中Handler类的聚合关系给出了具体子类对下家的引用，抽象方法handleRequest()规范了子类处理请求的操作。
　2.具体处理者(ConcreteHandler)角色：具体处理者接到请求后，可以选择将请求处理掉，或者将请求传给下家。由于具体处理者持有对下家的引用，因此，如果需要，具体处理者可以访问下家
责任链模式优缺点
优点：
职责链模式的最主要功能就是：动态组合，请求者和接受者解耦。
请求者和接受者松散耦合：请求者不需要知道接受者，也不需要知道如何处理。每个职责对象只负责自己的职责范围，其他的交给后继者。各个组件间完全解耦。
动态组合职责：职责链模式会把功能分散到单独的职责对象中，然后在使用时动态的组合形成链，从而可以灵活的分配职责对象，也可以灵活的添加改变对象职责。

缺点：
产生很多细粒度的对象：因为功能处理都分散到了单独的职责对象中，每个对象功能单一，要把整个流程处理完，需要很多的职责对象，会产生大量的细粒度职责对象。
不一定能处理：每个职责对象都只负责自己的部分，这样就可以出现某个请求，即使把整个链走完，都没有职责对象处理它。这就需要提供默认处理，并且注意构造链的有效性。
责任链模式应用场景
1.	多条件流程判断 权限控制
2.	ERP系统 流程审批 总经理、人事经理、项目经理
3.	Java过滤器的底层实现Filter 
比如：在Java过滤器中客户端发送请求到服务器端，过滤会经过参数过滤、session过滤、表单过滤、隐藏过滤、检测请求头过滤
 

网关权限控制责任链模式

在网关作为微服务程序的入口，拦截客户端所有的请求实现权限控制 ，比如先判断Api接口限流、黑名单、用户会话、参数过滤。
Api接口限流→黑名单拦截→用户会话→参数过滤

GatewayHandler抽象角色

public abstract class GatewayHandler {
    protected GatewayHandler nextGatewayHandler;

    /**
     * 处理业务逻辑
     *
     * @return true 表示继续执行 false表示不继续执行..
     */
    public abstract void service();

    public void setHandler(GatewayHandler gatewayHandler) {
        this.nextGatewayHandler = gatewayHandler;
    }
    protected void nextService(){
         if(nextGatewayHandler!=null){
             nextGatewayHandler.service();;
         }
    }
}



（具体Handler实现）
@Component
public class CurrentLimitHandler extends GatewayHandler {
    @Override
    public void service() {
        System.out.println("第一关网关限流判断....");
        nextService();
    }
}



@Component
public class ConversationHandler extends GatewayHandler {
    @Override
    public void service() {
        System.out.println("第三关用户会话拦截判断....");
        nextService();
    }
}



@Component
public class BlacklistHandler extends GatewayHandler {
    @Override
    public void service() {
        System.out.println("第二关黑名单拦截判断....");
        nextService();
    }
}
@Component
public class ConversationHandler extends GatewayHandler {
    @Override
    public void service() {
        System.out.println("第三关用户会话拦截判断....");
        nextService();
    }
}





FactoryHandler
public class FactoryHandler {
    public static GatewayHandler getGatewayHandler() {
        GatewayHandler gatewayHandler1 = new CurrentLimitHandler();
        GatewayHandler gatewayHandler2 = new BlacklistHandler();
        gatewayHandler1.setHandler(gatewayHandler2);
        GatewayHandler gatewayHandler3 = new ConversationHandler();
        gatewayHandler2.setHandler(gatewayHandler3);
        return gatewayHandler1;
    }
}


基于数据库实现
相关SQL语句
CREATE TABLE `gateway_handler` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `handler_name` varchar(32) DEFAULT NULL COMMENT 'handler名称',
  `handler_id` varchar(32) DEFAULT NULL COMMENT 'handler主键id',
  `prev_handler_id` varchar(32) DEFAULT NULL,
  `next_handler_id` varchar(32) DEFAULT NULL COMMENT '下一个handler',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of gateway_handler
-- ----------------------------
INSERT INTO `gateway_handler` VALUES ('16', 'Api接口限流', 'currentLimitHandler', null, 'blacklistHandler');
INSERT INTO `gateway_handler` VALUES ('17', '黑名单拦截', 'blacklistHandler', 'currentLimitHandler', 'conversationHandler');
INSERT INTO `gateway_handler` VALUES ('18', '会话验证', 'conversationHandler', 'blacklistHandler', null);
GatewayHandlerService
@Component
public class GatewayHandlerService {
    @Autowired
    private GatewayHandlerMapper gatewayHandlerMapper;
    private GatewayHandler firstGatewayHandler;

    public GatewayHandler getDbGatewayHandler() {
        if (firstGatewayHandler != null) {
            return firstGatewayHandler;
        }
        // 1.获取第一个GatewayHandler信息
        GatewayHandlerEntity firstGatewayHandlerEntity = gatewayHandlerMapper.getFirstGatewayHandler();
        if (firstGatewayHandlerEntity == null) {
            return null;
        }
        // 2.获取第一个firstGatewayHandler spring容器中的id
        String handlerBeanId = firstGatewayHandlerEntity.getHandlerId();
        // 3.从spring容器中获取对应的对象 firstGatewayHandler
        GatewayHandler firstGatewayHandler = SpringUtils.getBean(handlerBeanId, GatewayHandler.class);
        // 4.使用white循环 设置下一个节点 同时定义循环遍历临时对象
        GatewayHandler tempGatewayHandler = firstGatewayHandler;
        // 5.获取下一个节点
        String nextHandlerBeanId = firstGatewayHandlerEntity.getNextHandlerId();
        while (!StringUtils.isEmpty(nextHandlerBeanId)) {
            GatewayHandlerEntity nextGatewayHandlerEntity = gatewayHandlerMapper.getByHandler(nextHandlerBeanId);
            if (nextGatewayHandlerEntity == null) {
                break;
            }
            // 6.从springboot容器获取下一个handler 对象
            String tempNextHandlerBeanId = nextGatewayHandlerEntity.getHandlerId();
            GatewayHandler nextGatewayHandler = SpringUtils.getBean(tempNextHandlerBeanId, GatewayHandler.class);
            // 7.设置当前handler下一个handler对象
            tempGatewayHandler.setHandler(nextGatewayHandler);
            tempGatewayHandler = nextGatewayHandler;
            // 8.循环遍历下一个节点
            nextHandlerBeanId = nextGatewayHandlerEntity.getNextHandlerId();
        }
        this.firstGatewayHandler = firstGatewayHandler;
        return firstGatewayHandler;
    }
}

数据库访问层
GatewayHandlerMapper
public interface GatewayHandlerMapper {

   /**
    * 获取第一个GatewayHandler
    * 
    * @return
    */
   @Select("SELECT  handler_name AS handlerName,handler_id AS handlerid ,prev_handler_id AS prevhandlerid ,next_handler_id AS nexthandlerid  FROM gateway_handler WHERE  prev_handler_id is null;;")
   public GatewayHandlerEntity getFirstGatewayHandler();

   @Select("SELECT  handler_name AS handlerName,handler_id AS handlerid ,prev_handler_id AS prevhandlerid ,next_handler_id AS nexthandlerid   FROM gateway_handler WHERE  handler_id=#{handlerId}")
   public GatewayHandlerEntity getByHandler(String handlerId);

}
@Data
public class GatewayHandlerEntity implements Serializable, Cloneable {
   /** 主键ID */
   private Integer id;
   /** handler名称 */
   private String handlerName;
   /** handler主键id */
   private String handlerId;
   /** 下一个handler */
   private String nextHandlerId;


}

csdn地址：https://blog.csdn.net/qq_28056571/article/details/90300822
