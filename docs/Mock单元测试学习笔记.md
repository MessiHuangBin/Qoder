# Mock 单元测试学习笔记

> 基于 Spring Boot + JUnit 5 + Mockito 的单元测试实践总结

---

## 一、基础概念

### 1.1 什么是单元测试

单元测试是对软件中的最小可测试单元（通常是类或方法）进行验证的测试。它的目的是：
- 验证代码逻辑的正确性
- 隔离外部依赖（数据库、网络、其他服务）
- 快速反馈问题
- 作为代码的文档

### 1.2 什么是 Mock

Mock（模拟）是指在测试中创建对象的假实现，用于：
- 替代真实的依赖对象
- 控制依赖对象的行为
- 验证对象之间的交互
- 避免外部资源（数据库、网络）的依赖

---

## 二、核心注解详解

### 2.1 @ExtendWith(MockitoExtension.class)

**作用**：启用 Mockito 对 JUnit 5 的支持

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    // 测试代码
}
```

**关键点**：
- ✅ **不加载 Spring 上下文** - 纯 Java 对象测试
- ✅ **自动初始化 @Mock 和 @InjectMocks** 注解的字段
- ✅ **测试速度极快** - 毫秒级启动
- ✅ **专注于单个类的逻辑测试**

**适用场景**：服务层、数据访问层的单元测试

---

### 2.2 @WebMvcTest

**作用**：进行 Web 层的切片测试，只加载与 Web MVC 相关的组件

```java
@WebMvcTest(UserController.class)
@Import({GlobalResponseHandler.class, GlobalExceptionHandler.class})
class UserControllerTest {
    // 测试代码
}
```

**关键点**：
- ✅ **加载 Web 层上下文** - 包含 DispatcherServlet、MockMvc 等
- ✅ **不加载完整应用上下文** - 比 @SpringBootTest 快
- ❌ **不会自动扫描 @ControllerAdvice** - 需要手动 @Import 导入
- ✅ **自动配置 MockMvc** - 用于模拟 HTTP 请求

**适用场景**：控制器层的单元测试

**重要提醒**：如果项目使用了全局响应处理器或异常处理器，必须使用 @Import 显式导入：
```java
@Import({GlobalResponseHandler.class, GlobalExceptionHandler.class})
```

---

### 2.3 @Mock vs @MockBean

#### @Mock（纯 Mockito）

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserMapper userMapper;  // 纯 Mockito 模拟对象
}
```

**特点**：
- 由 Mockito 创建和管理
- 不进入 Spring 上下文
- 用于纯单元测试

#### @MockBean（Spring 集成）

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;  // Spring 上下文中的模拟 Bean
}
```

**特点**：
- 由 Spring 创建并放入应用上下文
- 会替换上下文中同类型的真实 Bean
- 用于 Spring 集成测试

**对比总结**：

| 特性 | @Mock | @MockBean |
|------|-------|-----------|
| 管理方 | Mockito | Spring |
| 上下文 | 无 | 有 |
| 用途 | 纯单元测试 | Spring 集成测试 |
| 速度 | 极快 | 较快 |

---

### 2.4 @Mock vs @InjectMocks

#### @Mock - 创建模拟对象

```java
@Mock
private UserMapper userMapper;  // 创建 UserMapper 的模拟实现
```

**作用**：
- 为接口或类创建模拟实现
- 替代真实的依赖
- 可以设置期望的行为和返回值

#### @InjectMocks - 创建被测试对象

```java
@InjectMocks
private UserService userService;  // 创建真实的 UserService 实例
```

**作用**：
- 创建被测试类的真实实例
- **自动将 @Mock 对象注入到被测试对象中**
- 执行真实的业务逻辑

**工作流程**：

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserMapper userMapper;      // 1. 创建模拟对象
    
    @InjectMocks
    private UserService userService;    // 2. 创建真实对象，并自动注入 userMapper
    
    @Test
    void test() {
        // 3. 设置模拟行为
        when(userMapper.selectById(1L)).thenReturn(testUser);
        
        // 4. 调用真实方法（userService 是真实的）
        User result = userService.getUserById(1L);
        
        // 5. 验证模拟对象被调用（userMapper 是模拟的）
        verify(userMapper, times(1)).selectById(1L);
    }
}
```

**关键区别**：

| 特性 | @Mock | @InjectMocks |
|------|-------|--------------|
| 对象类型 | 模拟对象 | 真实对象 |
| 执行真实代码 | ❌ 否 | ✅ 是 |
| 用途 | 替代依赖 | 被测试目标 |
| 设置行为 | ✅ 可以 | ❌ 不可以 |

---

### 2.5 @DisplayName

**作用**：为测试类或测试方法设置友好的显示名称

```java
@DisplayName("UserController 单元测试")
class UserControllerTest {

    @Test
    @DisplayName("GET /api/users/{id} - 查询用户成功")
    void getUserById_WhenUserExists_ShouldReturnWrappedUser() {
        // 测试代码
    }
}
```

**优势**：
- ✅ 提高可读性 - 比 Java 方法名更易理解
- ✅ 支持中文 - 适合国内团队
- ✅ 测试报告友好 - 在 IDE 和 CI/CD 中显示更清晰
- ✅ 文档化 - 测试名称本身就是文档

**效果对比**：

| 无 @DisplayName | 有 @DisplayName |
|----------------|-----------------|
| `getUserById_WhenUserExists_ShouldReturnWrappedUser` | `GET /api/users/{id} - 查询用户成功` |

---

### 2.6 @Import

**作用**：显式导入配置类到测试上下文中

```java
@WebMvcTest(UserController.class)
@Import({GlobalResponseHandler.class, GlobalExceptionHandler.class})
class UserControllerTest {
    // 测试代码
}
```

**为什么需要**：
- `@WebMvcTest` 是切片测试，不会自动扫描所有组件
- `@ControllerAdvice` 等全局处理器需要显式导入
- 确保测试环境与生产环境行为一致

**常见使用场景**：
- 导入全局响应处理器（统一包装响应格式）
- 导入全局异常处理器（统一处理异常）
- 导入安全配置、拦截器等

---

## 三、MockMvc 使用详解

### 3.1 什么是 MockMvc

MockMvc 是 Spring 提供的用于测试 Web 层的工具，可以在不启动真实服务器的情况下模拟 HTTP 请求。

### 3.2 基本使用流程

```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;  // 自动注入
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        // 1. 设置模拟行为
        when(userService.getUserById(1L)).thenReturn(testUser);
        
        // 2. 执行请求并验证
        mockMvc.perform(get("/api/users/{id}", 1L))  // 发起 GET 请求
                .andExpect(status().isOk())                      // 验证状态码 200
                .andExpect(jsonPath("$.data.id").value(1))       // 验证 JSON 数据
                .andExpect(jsonPath("$.data.username").value("testuser"));
        
        // 3. 验证服务层被调用
        verify(userService, times(1)).getUserById(1L);
    }
}
```

### 3.3 常用方法

#### 请求构建

```java
// GET 请求
mockMvc.perform(get("/api/users/{id}", 1L))

// POST 请求（带 JSON  body）
mockMvc.perform(post("/api/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user)))

// PUT 请求
mockMvc.perform(put("/api/users/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonString))

// DELETE 请求
mockMvc.perform(delete("/api/users/{id}", 1L))
```

#### 响应验证

```java
// HTTP 状态码
.andExpect(status().isOk())        // 200
.andExpect(status().isNotFound())  // 404
.andExpect(status().isBadRequest()) // 400

// JSON 数据验证
.andExpect(jsonPath("$.success").value(true))
.andExpect(jsonPath("$.data.id").value(1))
.andExpect(jsonPath("$.data.username").value("testuser"))
.andExpect(jsonPath("$.data", hasSize(2)))  // 数组大小

// 响应内容
.andExpect(content().string("expected content"))
```

---

## 四、Mockito 常用方法

### 4.1 设置模拟行为

```java
// 基本返回值
when(userMapper.selectById(1L)).thenReturn(testUser);

// 返回 null
when(userMapper.selectById(999L)).thenReturn(null);

// 抛出异常
when(userMapper.insert(any())).thenThrow(new RuntimeException("DB error"));

// 多次调用返回不同值
when(userMapper.selectById(1L))
    .thenReturn(user1)
    .thenReturn(user2)
    .thenReturn(null);

// 任意参数匹配
when(userService.addUser(any(User.class))).thenReturn(1);
when(userService.getUserByUsername(anyString())).thenReturn(testUser);
when(userService.getUsersByStatus(anyInt())).thenReturn(userList);
```

### 4.2 验证方法调用

```java
// 验证调用次数
verify(userMapper, times(1)).selectById(1L);      // 调用 1 次
verify(userMapper, never()).selectById(2L);       // 从未调用
verify(userMapper, atLeast(1)).selectById(1L);    // 至少 1 次
verify(userMapper, atMost(2)).selectById(1L);     // 最多 2 次

// 验证调用顺序
InOrder inOrder = inOrder(userMapper);
inOrder.verify(userMapper).selectById(1L);
inOrder.verify(userMapper).insert(any());

// 验证无其他交互
verifyNoMoreInteractions(userMapper);
```

### 4.3 参数匹配器

```java
// 任意对象
any(User.class)
anyString()
anyInt()
anyLong()
anyList()

// 特定值
eq("expected")
eq(1L)

// 组合使用
when(userService.updateUser(argThat(user -> user.getId() != null)))
    .thenReturn(1);
```

---

## 五、常见问题与解决方案

### 5.1 any() 方法歧义问题

**问题**：在使用 `any()` 时出现编译错误
```java
// 错误：The method any(Class<User>) is ambiguous
when(userService.addUser(any(User.class))).thenReturn(1);
```

**原因**：`Mockito.any()` 与 `MockMvcResultMatchers.any()` 冲突

**解决方案**：显式导入 Mockito 的 any
```java
import static org.mockito.ArgumentMatchers.any;  // 添加这行
import static org.mockito.Mockito.*;
```

### 5.2 统一响应格式的 JSON 路径

**问题**：测试中 JSON 路径不匹配

**原因**：项目使用了全局响应处理器，响应被包装在 Result 对象中

**原始响应**：
```json
{
  "id": 1,
  "username": "testuser"
}
```

**包装后的响应**：
```json
{
  "success": true,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "testuser"
  },
  "timestamp": 1234567890
}
```

**解决方案**：
```java
// 错误
.andExpect(jsonPath("$.id").value(1))

// 正确
.andExpect(jsonPath("$.data.id").value(1))
.andExpect(jsonPath("$.success").value(true))
```

### 5.3 @WebMvcTest 不加载全局处理器

**问题**：测试中发现响应没有被统一包装

**原因**：`@WebMvcTest` 不会自动扫描 `@ControllerAdvice`

**解决方案**：使用 @Import 显式导入
```java
@WebMvcTest(UserController.class)
@Import({GlobalResponseHandler.class, GlobalExceptionHandler.class})
class UserControllerTest {
    // ...
}
```

### 5.4 异常处理的 HTTP 状态码

**问题**：期望 500 状态码，但实际返回 200

**原因**：全局异常处理器将异常转换为统一响应，HTTP 状态码仍为 200

**解决方案**：
```java
// 错误期望
.andExpect(status().isInternalServerError())  // 期望 500

// 正确期望
.andExpect(status().isOk())  // 实际是 200
.andExpect(jsonPath("$.success").value(false))  // 通过 success 判断失败
.andExpect(jsonPath("$.message").value(containsString("错误信息")))
```

---

## 六、测试编写最佳实践

### 6.1 命名规范

**测试类名**：`被测试类名 + Test`
```
UserService -> UserServiceTest
UserController -> UserControllerTest
```

**测试方法名**：`方法名_条件_期望结果`
```java
void getUserById_WhenUserExists_ShouldReturnUser()
void addUser_WhenFail_ShouldThrowException()
```

**@DisplayName**：清晰描述测试场景
```java
@DisplayName("GET /api/users/{id} - 查询用户成功")
@DisplayName("POST /api/users - 添加用户失败")
```

### 6.2 AAA 模式

每个测试方法遵循 Arrange-Act-Assert（准备-执行-断言）模式：

```java
@Test
void testExample() {
    // Arrange - 准备
    when(userMapper.selectById(1L)).thenReturn(testUser);
    
    // Act - 执行
    User result = userService.getUserById(1L);
    
    // Assert - 断言
    assertEquals("testuser", result.getUsername());
    verify(userMapper, times(1)).selectById(1L);
}
```

### 6.3 测试覆盖率

每个方法应覆盖：
- ✅ **正常路径** - 成功场景
- ✅ **异常路径** - 失败场景
- ✅ **边界条件** - 空值、空列表等

### 6.4 测试分层策略

| 层级 | 注解 | 范围 | 速度 |
|------|------|------|------|
| 单元测试 | @ExtendWith(MockitoExtension.class) | 单个类 | 极快 |
| Web 层测试 | @WebMvcTest | 控制器层 | 快 |
| 集成测试 | @SpringBootTest | 完整应用 | 较慢 |

---

## 七、快速参考卡片

### 常用注解

```java
@ExtendWith(MockitoExtension.class)  // 纯单元测试，无 Spring 上下文
@WebMvcTest(Controller.class)        // Web 层测试
@MockBean                           // Spring 上下文中的模拟 Bean
@Mock                               // 纯 Mockito 模拟对象
@InjectMocks                        // 被测试对象，自动注入 @Mock
@DisplayName("描述")                 // 测试显示名称
@Import({Config.class})             // 导入配置类
```

### 常用静态导入

```java
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
```

### 常用验证

```java
// Mockito 验证
verify(mock, times(1)).method();
verify(mock, never()).method();

// MockMvc 验证
status().isOk()
jsonPath("$.data.id").value(1)
content().string(containsString("text"))
```

---

## 八、总结

1. **选择合适的测试注解**：
   - 服务层用 `@ExtendWith(MockitoExtension.class)`
   - 控制器层用 `@WebMvcTest`

2. **正确使用 Mock 注解**：
   - `@Mock` 用于依赖
   - `@InjectMocks` 用于被测试对象
   - `@MockBean` 用于 Spring 集成测试

3. **注意全局处理器的影响**：
   - 响应格式可能被包装
   - 需要显式导入全局处理器
   - 异常处理可能改变 HTTP 状态码行为

4. **编写清晰的测试**：
   - 使用 `@DisplayName` 提高可读性
   - 遵循 AAA 模式
   - 覆盖正常和异常场景

---

*文档创建时间：2026-03-01*
*基于项目：AnyView Spring Boot 项目*
