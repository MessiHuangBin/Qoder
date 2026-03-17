# HTTPS 学习笔记

## 一、HTTPS 基础

### 1.1 什么是 HTTPS
HTTPS = HTTP + SSL/TLS，通过 TLS 协议对通信进行加密，确保数据传输的安全性。

### 1.2 单向认证 vs 双向认证

| 特性 | 单向认证 | 双向认证 (mTLS) |
|------|---------|----------------|
| 服务器证书 | 需要 | 需要 |
| 客户端证书 | 不需要 | 需要 |
| 适用场景 | 普通网站 | 内部服务、API网关、IoT设备 |

---

## 二、TLS 握手流程

### 2.1 单向认证流程

```
客户端                              服务器
  │                                   │
  │ ───── 1. Client Hello ─────────> │
  │     (支持的TLS版本、加密套件、Client Random)
  │                                   │
  │ <──── 2. Server Hello ────────── │
  │     (选定TLS版本、加密套件、Server Random)
  │ <──── 3. Server Certificate ──── │
  │     (服务器证书，包含公钥)
  │ <──── 4. Server Hello Done ───── │
  │                                   │
  │ ───── 5. Client Key Exchange ──> │
  │     (用服务器公钥加密的 Pre-master Secret)
  │ ───── 6. Change Cipher Spec ───> │
  │ ───── 7. Finished ─────────────> │
  │                                   │
  │ <──── 8. Change Cipher Spec ──── │
  │ <──── 9. Finished ────────────── │
  │                                   │
  │ ════════════════════════════════════ │
  │     🔒 加密通道建立，开始安全通信        │
  │ ════════════════════════════════════ │
```

### 2.2 双向认证流程

在单向认证基础上，服务器会**请求客户端证书**并验证：

```
服务器额外发送：
  <──── CertificateRequest ──────  (请求客户端证书)

客户端额外发送：
  ───── Client Certificate ────>  (客户端证书)
  ───── CertificateVerify ─────>  (用私钥签名证明身份)
```

### 2.3 三个随机数的作用

| 随机数 | 生成方 | 作用 |
|--------|--------|------|
| **Client Random** | 客户端 | 防止重放攻击 |
| **Server Random** | 服务器 | 增加随机性 |
| **Pre-master Secret** | 客户端生成 | 生成会话密钥的基础 |

三个随机数通过 PRF 函数生成 **Master Secret**，再派生出会话密钥。

---

## 三、JKS（Java KeyStore）

### 3.1 什么是 JKS

JKS = Java KeyStore，Java 专门用来存储密钥和证书的文件格式，类似于"保险柜"。

### 3.2 JKS 文件结构

```
┌─────────────────────────────────────────┐
│              JKS 文件                    │
│         （受 storepass 保护）             │
├─────────────────────────────────────────┤
│                                         │
│  ┌─────────────────────────────────┐    │
│  │  Entry: "server" (别名)          │    │
│  │  ─────────────────────────────  │    │
│  │  类型: PrivateKeyEntry           │    │
│  │                                 │    │
│  │  🔐 加密存储的内容:               │    │
│  │  ├── 私钥 (Private Key)          │    │  ← 用 keypass 加密
│  │  ├── 证书 (Certificate)          │    │
│  │  │   └── 包含公钥                 │    │
│  │  └── 证书链 (Certificate Chain)  │    │
│  │                                 │    │
│  │  需要: keypass 才能读取私钥       │    │
│  └─────────────────────────────────┘    │
│                                         │
│  ┌─────────────────────────────────┐    │
│  │  Entry: "ca1" (别名)             │    │
│  │  ─────────────────────────────  │    │
│  │  类型: TrustedCertificateEntry   │    │
│  │                                 │    │
│  │  📜 只存证书（公钥）:             │    │
│  │  └── CA 根证书                   │    │
│  │                                 │    │
│  │  不需要密码读取（已公开）          │    │
│  └─────────────────────────────────┘    │
│                                         │
└─────────────────────────────────────────┘
```

### 3.3 Entry 类型

| 类型 | 包含内容 | 用途 |
|------|---------|------|
| **PrivateKeyEntry** | 私钥 + 证书链 | 证明自己的身份 |
| **TrustedCertificateEntry** | 仅证书（公钥） | 验证别人的身份 |

### 3.4 两个密码的作用

| 密码 | 用途 | 保护对象 |
|------|------|---------|
| **storepass** (密钥库密码) | 打开 JKS 文件 | 整个密钥库文件 |
| **keypass** (密钥密码) | 解密私钥 | 私钥本身 |

**访问权限规则：**
- 打开 JKS 文件 → 需要 **storepass**
- 查看证书 → 不需要额外密码（证书是公开的）
- 获取私钥 → 需要 **keypass**（如果未设置，默认使用 storepass）

### 3.5 服务端 JKS 配置

**单向认证场景（最常见）：**

服务端 JKS 只需要 **1 个 Entry**（包含私钥 + 证书）：

```
┌─────────────────────────────────────────┐
│           服务端 server.jks              │
│         （单向认证场景）                  │
├─────────────────────────────────────────┤
│                                         │
│  只有 1 个 Entry:                        │
│                                         │
│  ┌─────────────────────────────────┐    │
│  │  Entry: "server"                │    │
│  │  ─────────────────────────────  │    │
│  │  类型: PrivateKeyEntry           │    │
│  │                                 │    │
│  │  🔐 包含:                        │    │
│  │  ├── 服务器私钥                  │    │  ← TLS 握手时签名用
│  │  ├── 服务器证书                  │    │  ← 发给客户端验证
│  │  └── 证书链（如有中间CA）         │    │
│  └─────────────────────────────────┘    │
│                                         │
└─────────────────────────────────────────┘
```

---

## 四、加密套件（Cipher Suite）

### 4.1 什么是加密套件

加密套件是一套加密算法的组合，用于 TLS 握手和数据传输时的安全保护。

### 4.2 加密套件的组成（TLS 1.2）

以 `TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256` 为例：

```
TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
      ↑      ↑        ↑      ↑     ↑
      │      │        │      │     │
    密钥交换  认证   对称加密  加密模式  哈希算法
```

| 组件 | 示例 | 作用 |
|------|------|------|
| **密钥交换算法** | ECDHE | 安全协商会话密钥 |
| **身份认证算法** | RSA | 验证通信双方身份 |
| **对称加密算法** | AES_128 | 加密实际传输的数据 |
| **加密模式** | GCM | 定义对称加密的工作方式 |
| **消息摘要算法** | SHA256 | 完整性校验 |

### 4.3 TLS 1.3 的简化

TLS 1.3 简化了加密套件，固定了密钥交换和认证算法，只需协商：

```
TLS_AES_128_GCM_SHA256
    ↑       ↑      ↑
    │       │      │
  对称加密  加密模式  哈希算法
```

---

## 五、GCM 加密模式

### 5.1 什么是 GCM

GCM = Galois Counter Mode（伽罗瓦计数器模式）

是一种**认证加密模式（AEAD）**，同时提供：
- ✅ **保密性**（加密数据）
- ✅ **完整性**（防篡改）

### 5.2 GCM 的优势

| 特性 | 说明 |
|------|------|
| **认证加密** | 加密和认证一次性完成 |
| **并行计算** | CTR 模式支持并行，速度快 |
| **在线处理** | 不需要预先知道数据总长度 |

### 5.3 GCM vs 其他模式

| 模式 | 加密 | 完整性 | 推荐度 |
|------|:---:|:---:|:---:|
| ECB | ✅ | ❌ | ❌ 不安全 |
| CBC | ✅ | ❌ | ⚠️ 需要额外 MAC |
| CTR | ✅ | ❌ | ⚠️ 需要额外 MAC |
| **GCM** | ✅ | ✅ | ⭐⭐⭐ 推荐 |

---

## 六、信任库（TrustStore）

### 6.1 什么是信任库

信任库存储**信任的 CA 证书**，用于验证对方身份是否可信。

### 6.2 KeyStore vs TrustStore

| | KeyStore（密钥库） | TrustStore（信任库） |
|---|---|---|
| **存什么** | 自己的私钥 + 自己的证书 | 信任的 CA 证书 |
| **用途** | 证明"我是谁" | 验证"你是谁" |
| **比喻** | 我的身份证 | 我信任的发证机关列表 |

### 6.3 使用场景

**场景1：服务器验证客户端（双向认证）**

```yaml
server:
  ssl:
    # 服务器证书
    key-store: classpath:server.jks
    key-store-password: changeit
    
    # 信任库：验证客户端证书
    trust-store: classpath:truststore.jks
    trust-store-password: changeit
    client-auth: need  # 必须验证客户端
```

**场景2：客户端验证服务器**

```java
// 调用 HTTPS 接口时验证服务器证书
KeyStore trustStore = KeyStore.getInstance("JKS");
trustStore.load(new FileInputStream("truststore.jks"), "password".toCharArray());

SSLContext sslContext = SSLContexts.custom()
    .loadTrustMaterial(trustStore, null)  // 使用信任库验证服务器
    .build();
```

### 6.4 信任库格式

信任库可以用与 KeyStore 相同的格式：
- **JKS** (`.jks`) - Java 传统格式
- **PKCS12** (`.p12`/`.pfx`) - 行业标准，推荐

**关键区别不在格式，而在内容：**
- 信任库只存证书（公钥），不存私钥
- 只有证书（或其签发 CA）在 TrustStore 中，才允许继续通信

---

## 七、Spring Boot HTTPS 配置

### 7.1 单向认证配置

```yaml
server:
  port: 8443
  ssl:
    # 密钥库配置
    key-store: classpath:server.jks          # JKS 文件路径
    key-store-password: changeit             # storepass（打开 JKS）
    key-store-type: JKS                      # 密钥库类型
    key-alias: server                        # Entry 别名
    key-password: changeit                   # keypass（获取私钥，可选）
```

### 7.2 双向认证配置

```yaml
server:
  port: 8443
  ssl:
    # 密钥库（证明"我是谁"）
    key-store: classpath:server.jks
    key-store-password: changeit
    key-alias: server
    
    # 信任库（验证"你是谁"）
    trust-store: classpath:truststore.jks
    trust-store-password: changeit
    trust-store-type: JKS
    
    # 客户端认证模式
    client-auth: need     # need=必须验证, want=可选, none=不验证（默认）
```

### 7.3 配置项说明

| 配置项 | 必填 | 说明 |
|--------|:---:|:---|
| `key-store` | ✅ | 服务器证书文件路径 |
| `key-store-password` | ✅ | 密钥库密码（storepass） |
| `key-alias` | ❌ | Entry 别名（JKS 中有多个时需要） |
| `key-password` | ❌ | 私钥密码（keypass，默认用 storepass） |
| `trust-store` | ❌ | 信任库路径（双向认证时需要） |
| `client-auth` | ❌ | 客户端认证模式（默认 none） |

---

## 八、常用命令

### 8.1 生成 JKS

```bash
# 生成自签名证书（开发测试用）
keytool -genkeypair \
  -alias server \
  -keyalg RSA \
  -keysize 2048 \
  -validity 365 \
  -keystore server.jks \
  -storepass changeit \
  -dname "CN=localhost, OU=IT, O=MyCompany, L=Shenzhen, ST=Guangdong, C=CN"
```

### 8.2 查看 JKS 内容

```bash
# 查看 JKS 中的所有 Entry
keytool -list -v -keystore server.jks -storepass changeit
```

### 8.3 导出证书

```bash
# 导出证书（公钥可以公开）
keytool -export -alias server -keystore server.jks -file server.cer -storepass changeit
```

### 8.4 导入证书到信任库

```bash
# 将 CA 证书导入信任库
keytool -import -alias ca -file ca.crt -keystore truststore.jks -storepass changeit
```

---

## 九、总结

1. **HTTPS** = HTTP + TLS 加密，保障数据传输安全
2. **JKS** = Java 的密钥库格式，存私钥和证书
3. **单向认证** 只需服务器证书，**双向认证** 还需验证客户端
4. **storepass** 打开 JKS，**keypass** 获取私钥
5. **TrustStore** 存信任的 CA 证书，用于验证对方身份
6. **加密套件** 定义 TLS 使用的算法组合
7. **GCM 模式** 同时提供加密和完整性保护

---

*整理时间：2025年*
