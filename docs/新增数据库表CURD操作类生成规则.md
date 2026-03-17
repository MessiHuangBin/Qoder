# 新增数据库表CURD操作类生成规则

## 概述

当需要为数据库新表生成CURD操作类时，按照本文档规范生成以下7个核心文件。

## 生成文件清单

假设新表名为 `{TableName}`（如：Book、Order等）

| 序号 | 文件类型 | 文件路径 | 说明 |
|------|----------|----------|------|
| 1 | Domain实体 | `domain/model/entity/{TableName}.java` | 领域实体，包含业务方法 |
| 2 | 仓储接口 | `domain/support/{TableName}Repository.java` | 仓储接口定义 |
| 3 | PO类 | `infrastructure/repository/po/{TableName}PO.java` | 持久化对象，与表结构对应 |
| 4 | 转换器 | `infrastructure/repository/convert/{TableName}POConvert.java` | Domain与PO双向转换 |
| 5 | Mapper接口 | `infrastructure/repository/mapper/{TableName}Mapper.java` | MyBatis Mapper接口 |
| 6 | 仓储实现 | `infrastructure/repository/impl/{TableName}RepositoryImpl.java` | 仓储接口实现 |
| 7 | Mapper XML | `src/main/resources/mapper/{TableName}Mapper.xml` | MyBatis SQL映射 |

## 各文件详细规范

### 1. Domain实体类

**路径**: `domain/model/entity/{TableName}.java`

**规范**:
- 使用Lombok注解@Data
- 字段与数据库表对应

**示例**:
```java
public class Book {
    private Long id;
    private String title;
    // ... 其他字段
}
```

### 2. 仓储接口

**路径**: `domain/support/{TableName}Repository.java`

**规范**:
- 返回类型为Domain实体
- 参数类型为Domain实体或基本类型
- 包含基本CURD方法

**示例**:
```java
public interface BookRepository {
    Book findById(Long id);
    Book findByIsbn(String isbn);
    List<Book> findByTitle(String title);
    List<Book> findAll();
    void save(Book book);
    void update(Book book);
    void delete(Long id);
}
```

### 3. PO类

**路径**: `infrastructure/repository/po/{TableName}PO.java`

**规范**:
- 使用Lombok的`@Data`注解
- 字段与数据库表完全对应
- 驼峰命名与数据库下划线命名映射在XML中处理

**示例**:
```java
@Data
public class BookPO {
    private Long id;
    private String title;
    private LocalDateTime createTime;
    // ...
}
```

### 4. 转换器

**路径**: `infrastructure/repository/convert/{TableName}POConvert.java`

**规范**:
- 使用`@Component`注解
- 提供`toDomain()`方法：PO -> Domain
- 提供`toPO()`方法：Domain -> PO
- 提供`toDomainList()`方法：PO列表 -> Domain列表

**示例**:
```java
@Component
public class BookPOConvert {
    public Book toDomain(BookPO po) { }
    public BookPO toPO(Book book) { }
    public List<Book> toDomainList(List<BookPO> poList) { }
}
```

### 5. Mapper接口

**路径**: `infrastructure/repository/mapper/{TableName}Mapper.java`

**规范**:
- 使用`@Mapper`注解
- 使用`@Param`注解标注参数
- 返回类型为PO
- 参数类型为PO或基本类型

**示例**:
```java
@Mapper
public interface BookMapper {
    BookPO selectById(@Param("id") Long id);
    List<BookPO> selectByTitle(@Param("title") String title);
    int insert(BookPO bookPO);
    int update(BookPO bookPO);
    int deleteById(@Param("id") Long id);
}
```

### 6. 仓储实现类

**路径**: `infrastructure/repository/impl/{TableName}RepositoryImpl.java`

**规范**:
- 使用`@Repository`注解
- 实现Domain层的Repository接口
- 注入Mapper和Convert
- 方法内：PO -> Convert转换 -> Domain

**示例**:
```java
@Repository
public class BookRepositoryImpl implements BookRepository {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookPOConvert bookPOConvert;
    
    @Override
    public Book findById(Long id) {
        BookPO po = bookMapper.selectById(id);
        return bookPOConvert.toDomain(po);
    }
    // ...
}
```

### 7. Mapper XML

**路径**: `src/main/resources/mapper/{TableName}Mapper.xml`

**规范**:
- namespace为Mapper接口全路径
- resultMap的type为PO类全路径

**示例**:
```xml
<mapper namespace="...mapper.BookMapper">
    <resultMap id="BookResultMap" type="...po.BookPO">
        <id column="id" property="id"/>
        <result column="create_time" property="createTime"/>
    </resultMap>
    
    <select id="selectById" resultMap="BookResultMap">
        SELECT * FROM book WHERE id = #{id} AND deleted = 0
    </select>
    
    <update id="deleteById">
        UPDATE book SET deleted = 1 WHERE id = #{id}
    </update>
</mapper>
```

## 数据流向

```
Domain实体 (domain/model/entity/)
    ↑ ↓ 转换 (convert/)
PO (infrastructure/repository/po/)
    ↑ ↓ Mapper操作
数据库表
```

## 命名规范

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| Domain实体 | 表名驼峰 | `Book`, `UserOrder` |
| Repository接口 | 实体名+Repository | `BookRepository` |
| Repository实现 | 接口名+Impl | `BookRepositoryImpl` |
| PO类 | 实体名+PO | `BookPO` |
| Convert类 | 实体名+POConvert | `BookPOConvert` |
| Mapper接口 | 实体名+Mapper | `BookMapper` |
| Mapper XML | 实体名+Mapper.xml | `BookMapper.xml` |

## 注意事项
2. **字段映射**: 数据库下划线命名（create_time）映射为PO驼峰命名（createTime）
