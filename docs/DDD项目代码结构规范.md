# Spring Boot + DDD 项目代码结构规范

## 1. 目录结构

```
src/main/java/com/example/project/
├── adapter/              # 适配器层（接收请求、返回响应）
│   └── controller/       # REST API 控制器
│
├── application/          # 应用层（编排业务逻辑）
│   ├── dto/              # X包（上游业务DTO）
│   ├── service/          # 应用服务
│   ├── support/          # 防腐接口（调用外部系统）
│   └── convert/          # X包与Domain对象转换
│
├── domain/               # 领域层（核心业务逻辑）
│   ├── model/            # 领域模型
│   │   ├── entity/       # 实体
│   │   └── valueobject/  # 值对象
│   ├── service/          # 领域服务（跨实体业务）
│   └── support/          # 仓储接口定义
│
└── infrastructure/       # 基础设施层（技术实现）
    ├── repository/       # 仓储实现
    │   ├── impl/         # 仓储接口具体实现类
    │   ├── mapper/       # MyBatis Mapper接口（只操作PO）
    │   ├── po/           # 数据库持久化对象
    │   └── convert/      # Domain对象与PO转换器
    ├── remote/           # 外部系统调用实现
    │   ├── impl/         # 外部调用实现类
    │   ├── dto/          # 下游系统的X包/Z包
    │   └── convert/      # Domain对象与下游X包/Z包转换器
    └── common/           # 基础设施通用工具
        ├── config/       # 配置类
        └── util/         # 工具类
```

## 2. 各层职责

| 层级 | 职责 | 关键元素 |
|------|------|----------|
| **adapter** | 接收外部请求，返回响应 | Controller |
| **application** | 编排业务逻辑，协调各领域对象 | service、dto、support、convert |
| **domain** | 核心业务逻辑 | entity、valueobject、domain service、support |
| **infrastructure** | 技术实现细节 | repository、remote、common |

## 3. 依赖方向

```
adapter → application → domain ← infrastructure
```

- 依赖只能向内指向 domain 层
- domain 层不依赖任何其他层

## 4. 数据流向规范

### 4.1 写入流程（Controller → DB）

```
Controller (ZZ1JsonMessage)
    ↓ getDto(X.class)
X包 (application/dto/)
    ↓ application/convert/ (X包 → Domain)
Domain实体
    ↓ domain/support/ (仓储接口)
infrastructure/repository/impl
    ↓ convert/ (Domain → PO)
PO
    ↓ mapper
数据库
```

### 4.2 查询流程（DB → Controller）

```
数据库
    ↓ mapper
PO
    ↓ convert/ (PO → Domain)
Domain实体
    ↓ supportimpl返回Domain
应用层处理
    ↓ Controller包装返回
```

### 4.3 调用外部系统流程

```
应用服务
    ↓ application/support/ (接口)
infrastructure/remote/impl (实现)
    ↓ convert/ (Domain → X包)
X包
    ↓ HTTP调用
外部系统
    ↓ 返回Z包 (remote/dto/)
convert/ (Z包 → 更新Domain)
Domain实体返回
```

## 5. 防腐接口（Anti-Corruption Layer）

- **接口定义**：`application/support/` 存放调用外部系统的接口
- **接口实现**：`infrastructure/remote/impl/` 存放具体实现类

示例：
```java
// application/support/PaymentGateway.java
public interface PaymentGateway {
    PaymentResult charge(Order order, Money amount);
}

// infrastructure/remote/impl/AlipayGatewayImpl.java
@Component
public class AlipayGatewayImpl implements PaymentGateway {
    // 调用支付宝SDK
}
```

## 6. 仓储模式（Repository Pattern）

- **接口定义**：`domain/support/` 存放仓储接口，返回/接收Domain实体
- **接口实现**：`infrastructure/repository/impl/` 存放具体实现类

示例：
```java
// domain/support/OrderRepository.java
public interface OrderRepository {
    Order findById(Long id);      // 返回Domain实体
    void save(Order order);       // 接收Domain实体
}

// infrastructure/repository/impl/OrderRepositoryImpl.java
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    @Autowired
    private OrderMapper mapper;   // 只操作PO
    
    @Override
    public Order findById(Long id) {
        OrderPO po = mapper.selectById(id);           // mapper操作PO
        return OrderConvert.toDomain(po);             // PO → Domain
    }
    
    @Override
    public void save(Order order) {
        OrderPO po = OrderConvert.toPO(order);        // Domain → PO
        mapper.insert(po);                            // mapper操作PO
    }
}
```

## 7. 转换器（Converter）规范

| 位置 | 职责 | 转换方向 |
|------|------|----------|
| `application/convert/` | X包与Domain转换 | X包 → Domain |
| `repository/convert/` | Domain与PO转换 | Domain ↔ PO |
| `remote/convert/` | Domain与下游包转换 | Domain ↔ X包/Z包 |

## 8. DTO 分层规范

| 位置 | 用途 | 说明 |
|------|------|------|
| 框架提供 | 外层协议包装 | ZZ1JsonMessage，包含header、body等 |
| `application/dto/` | X包（上游业务DTO） | Controller取出的业务数据 |
| `remote/dto/` | 下游系统的X包/Z包 | 调用外部系统时的请求/响应DTO |

## 9. 关键约束

- **mapper只操作PO**，不直接操作Domain实体
- **所有support接口返回的都是Domain实体**
- **repository/impl中**：select时mapper返回PO，convert转成Domain返回；insert/update时Domain转成PO，mapper操作PO
- **remote/impl中**：调用外部系统前Domain转成X包；收到Z包后转成Domain返回



## 12. 可选组件

以下组件根据实际需要添加：

- `domain/service/`：当存在跨实体业务逻辑时添加
- `infrastructure/common/util/`：通用工具类
