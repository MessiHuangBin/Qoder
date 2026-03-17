# 代码分析模式与技巧

## 目录
1. [分析入口识别](#分析入口识别)
2. [调用链追踪策略](#调用链追踪策略)
3. [常见代码模式识别](#常见代码模式识别)
4. [Spring/MyBatis特定分析](#springmybatis特定分析)

---

## 分析入口识别

### Controller方法识别

```java
// 典型特征
@RestController / @Controller
@RequestMapping / @GetMapping / @PostMapping / @PutMapping / @DeleteMapping

// 示例
@PostMapping("/api/borrow")
public Result<BorrowDTO> borrowBook(@RequestBody BorrowRequest request) {
    // 这是入口方法
}
```

### Service方法入口

```java
// 典型特征
@Service
@Transactional
public 返回值 方法名(参数) {
    // 业务逻辑
}
```

### 识别被调用的服务

```java
// 通过依赖注入识别
@Autowired / @Resource
private XxxService xxxService;

// 构造器注入
public MyService(XxxService xxxService) {
    this.xxxService = xxxService;
}
```

---

## 调用链追踪策略

### 策略1: 从上往下追踪

```
1. 读取Controller方法
2. 找到调用的AppService方法
3. 找到调用的DomainService方法
4. 找到调用的Repository方法
5. 找到Mapper接口方法
6. 找到对应的XML SQL
```

### 策略2: 接口到实现

当遇到接口调用时：

```java
// 接口定义
public interface BookRepository {
    Book findById(Long id);
}

// 需要找到实现类
@Repository
public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book findById(Long id) {
        // 实际实现
    }
}
```

**查找技巧**：
- 搜索 `implements XxxRepository`
- 搜索 `@Repository` + 类名含 `Impl`
- 检查 `infrastructure/repository/impl/` 目录

### 策略3: Mapper接口到XML

```java
// Mapper接口
@Mapper
public interface BookMapper {
    BookPO selectById(Long id);
}
```

对应的XML位置：`resources/mapper/BookMapper.xml`

```xml
<mapper namespace="com.example.xxx.mapper.BookMapper">
    <select id="selectById" resultType="BookPO">
        SELECT * FROM book WHERE id = #{id}
    </select>
</mapper>
```

---

## 常见代码模式识别

### 模式1: 校验-处理-返回

```java
public Result doSomething(Request request) {
    // 1. 参数校验
    if (request.getXxx() == null) {
        throw new BusinessException("xxx不能为空");
    }
    
    // 2. 业务处理
    Entity entity = service.process(request);
    
    // 3. 返回结果
    return Result.success(convert.toDTO(entity));
}
```

### 模式2: 查询-判断-执行

```java
public void execute(Long id) {
    // 1. 查询数据
    Entity entity = repository.findById(id);
    
    // 2. 判断状态
    if (entity == null) {
        throw new NotFoundException("数据不存在");
    }
    if (entity.getStatus() != Status.ACTIVE) {
        throw new BusinessException("状态不允许操作");
    }
    
    // 3. 执行操作
    entity.doAction();
    repository.save(entity);
}
```

### 模式3: 聚合根模式（DDD）

```java
// 领域实体包含业务逻辑
public class Order {
    private OrderStatus status;
    
    public void cancel() {
        if (this.status == OrderStatus.SHIPPED) {
            throw new BusinessException("已发货订单不能取消");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
```

### 模式4: 策略模式/多态处理

```java
// 根据类型选择不同处理逻辑
public void process(String type) {
    Handler handler = handlerFactory.getHandler(type);
    handler.handle();
}
```

**分析时注意**：需要找到所有Handler实现类

---

## Spring/MyBatis特定分析

### @Transactional 分析

```java
@Transactional
public void businessMethod() {
    // 整个方法在一个事务中
    step1();
    step2(); // 如果这里失败，step1也会回滚
}

@Transactional(propagation = Propagation.REQUIRES_NEW)
public void independentMethod() {
    // 开启新事务，与外层事务独立
}
```

**分析要点**：
- 事务边界在哪里
- 嵌套调用的事务传播行为
- 回滚条件（默认RuntimeException）

### MyBatis动态SQL

```xml
<select id="findByCondition">
    SELECT * FROM table
    <where>
        <if test="name != null">
            AND name = #{name}
        </if>
        <if test="status != null">
            AND status = #{status}
        </if>
    </where>
</select>
```

**分析要点**：
- 哪些条件是动态的
- 各条件的组合效果
- 可能产生的SQL变体

### ResultMap映射

```xml
<resultMap id="entityMap" type="Entity">
    <id property="id" column="id"/>
    <result property="userName" column="user_name"/>
    <association property="detail" javaType="Detail">
        <id property="id" column="detail_id"/>
    </association>
    <collection property="items" ofType="Item">
        <id property="id" column="item_id"/>
    </collection>
</resultMap>
```

**分析要点**：
- 字段与列的映射关系
- 关联对象如何加载
- 集合数据如何映射

---

## 分析检查清单

在分析过程中，确保回答以下问题：

### 入口层（Controller）
- [ ] 接口URL是什么？
- [ ] 请求方法（GET/POST/PUT/DELETE）？
- [ ] 请求参数结构？
- [ ] 返回值结构？

### 应用层（AppService）
- [ ] 调用了哪些领域服务？
- [ ] 数据转换逻辑？
- [ ] 事务边界？

### 领域层（DomainService）
- [ ] 核心业务规则是什么？
- [ ] 有哪些条件判断？
- [ ] 领域对象如何变化？

### 基础设施层（Repository/Mapper）
- [ ] 查询/更新了哪些表？
- [ ] SQL条件是什么？
- [ ] PO与Entity如何转换？

### 异常处理
- [ ] 可能抛出哪些异常？
- [ ] 在哪一层被捕获？
- [ ] 对用户的提示信息？
