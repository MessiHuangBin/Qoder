# Superpowers Skills 协作使用指南

## 概述

Superpowers 是一套结构化的 AI 辅助开发流程，包含 14 个核心 skill。这些 skill 按照软件开发生命周期组织，形成完整的工作流。本文档介绍各 skill 的职责以及它们如何相互配合。

---

## 14个 Skill 分类

### 一、流程控制类 (Process Skills)

| Skill | 职责 | 触发时机 |
|-------|------|----------|
| **using-superpowers** | 所有 skill 的入口，建立如何发现和使用 skill 的规则 | 每次对话开始时 |
| **brainstorming** | 需求分析、方案设计、规格文档编写 | 任何创造性工作之前 |
| **writing-plans** | 将设计规格转化为可执行的实施计划 | 设计完成后 |

### 二、开发执行类 (Implementation Skills)

| Skill | 职责 | 触发时机 |
|-------|------|----------|
| **using-git-worktrees** | 创建隔离的 Git 工作区 | 开始任何开发工作前 |
| **subagent-driven-development** | 使用子代理并行执行任务（推荐方式） | 有实施计划且支持子代理时 |
| **executing-plans** | 在当前会话中执行计划（备选方式） | 有实施计划但不支持子代理时 |
| **test-driven-development** | 测试驱动开发规范 | 实现任何功能或修复 bug 时 |

### 三、质量保障类 (Quality Assurance Skills)

| Skill | 职责 | 触发时机 |
|-------|------|----------|
| **requesting-code-review** | 发起代码审查 | 完成任务、实现主要功能、合并前 |
| **receiving-code-review** | 处理代码审查反馈 | 收到代码审查反馈时 |
| **systematic-debugging** | 系统化调试流程 | 遇到任何 bug 或测试失败时 |
| **verification-before-completion** | 完成前的验证检查 | 声称工作完成之前 |

### 四、工作收尾类 (Completion Skills)

| Skill | 职责 | 触发时机 |
|-------|------|----------|
| **finishing-a-development-branch** | 完成开发分支，提供合并/PR/清理选项 | 实现完成且测试通过后 |
| **dispatching-parallel-agents** | 并行分派多个独立任务的代理 | 面对 2+ 独立任务时 |

### 五、Skill 开发类 (Meta Skills)

| Skill | 职责 | 触发时机 |
|-------|------|----------|
| **writing-skills** | 创建、编辑和验证 skill | 创建新 skill 或修改现有 skill 时 |

---

## 标准工作流程

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         阶段 1: 需求与设计                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌──────────────────┐     ┌──────────────────┐     ┌──────────────────┐   │
│   │ using-superpowers │ --> │  brainstorming   │ --> │  writing-plans   │   │
│   │   (入口/规则)     │     │  (需求分析设计)   │     │  (编写实施计划)   │   │
│   └──────────────────┘     └──────────────────┘     └──────────────────┘   │
│                                │                                            │
│                                ▼                                            │
│                         ┌──────────────────┐                               │
│                         │ 输出: 设计规格文档  │                               │
│                         │ docs/superpowers/  │                               │
│                         │ specs/YYYY-MM-DD-  │                               │
│                         │ <topic>-design.md  │                               │
│                         └──────────────────┘                               │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         阶段 2: 开发准备                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌──────────────────┐                                                     │
│   │ using-git-worktrees│                                                    │
│   │  (创建隔离工作区)  │                                                    │
│   └──────────────────┘                                                     │
│          │                                                                  │
│          ▼                                                                  │
│   创建独立分支 + 工作树，运行测试确保基线干净                                   │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         阶段 3: 实施执行                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   方式 A: 子代理驱动开发 (推荐)                    方式 B: 计划执行          │
│   ┌─────────────────────────────┐              ┌──────────────────┐         │
│   │ subagent-driven-development │              │  executing-plans │         │
│   │    (子代理并行开发)          │              │  (当前会话执行)   │         │
│   └─────────────────────────────┘              └──────────────────┘         │
│                                                                             │
│   每个任务循环:                                                               │
│   ┌──────────┐    ┌──────────┐    ┌──────────┐                             │
│   │ 分派实现者 │ -> │ 规格审查  │ -> │ 代码质量  │                             │
│   │  子代理   │    │  子代理   │    │  审查子代理 │                            │
│   └──────────┘    └──────────┘    └──────────┘                             │
│                                                                             │
│   子代理使用:                                                                 │
│   - test-driven-development (TDD 规范)                                      │
│   - requesting-code-review (代码审查模板)                                    │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         阶段 4: 问题处理                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   遇到 Bug 或测试失败时:                                                      │
│   ┌─────────────────────┐                                                   │
│   │ systematic-debugging │                                                  │
│   │    (系统化调试)      │                                                  │
│   └─────────────────────┘                                                   │
│                                                                             │
│   完成前验证:                                                                │
│   ┌─────────────────────────────┐                                           │
│   │ verification-before-completion│                                         │
│   │      (完成前验证)            │                                          │
│   └─────────────────────────────┘                                           │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                         阶段 5: 工作收尾                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌─────────────────────────────┐                                           │
│   │ finishing-a-development-branch│                                         │
│   │    (完成开发分支)            │                                          │
│   └─────────────────────────────┘                                           │
│                                                                             │
│   提供选项:                                                                  │
│   1. 本地合并到基础分支                                                       │
│   2. 推送并创建 Pull Request                                                 │
│   3. 保持分支现状                                                            │
│   4. 丢弃此工作                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Skill 协作关系详解

### 1. brainstorming → writing-plans

**协作方式:**
- brainstorming 完成设计后，必须调用 writing-plans 创建实施计划
- 这是 brainstorming 流程的终点（第9步）

**关键规则:**
```
brainstorming 的终端状态是调用 writing-plans
不要调用任何其他实现 skill
```

### 2. writing-plans → subagent-driven-development / executing-plans

**协作方式:**
- writing-plans 创建的计划文档头部明确要求使用哪个 skill 执行
- 如果支持子代理：必须使用 subagent-driven-development
- 如果不支持子代理：使用 executing-plans

**计划文档头部示例:**
```markdown
> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development 
> (if subagents available) or superpowers:executing-plans to implement this plan.
```

### 3. using-git-worktrees 作为前置条件

**必须调用的场景:**
- brainstorming 阶段4（设计批准后实施）
- subagent-driven-development（执行任何任务前）
- executing-plans（执行任何任务前）

**协作流程:**
```
1. 检查现有工作树目录 (.worktrees/ 或 worktrees/)
2. 验证目录被 .gitignore 忽略
3. 创建新分支 + 工作树
4. 运行项目设置和基线测试
5. 报告工作树位置
```

### 4. subagent-driven-development 内部协作

**每个任务的完整流程:**
```
┌─────────────────────────────────────────────────────────────┐
│                      任务执行循环                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. 分派实现者子代理 (implementer-prompt.md)                  │
│     ↓                                                       │
│  2. 实现者询问问题？ → 是 → 回答问题 → 返回步骤1              │
│     ↓ 否                                                    │
│  3. 实现者实现、测试、提交、自审查                            │
│     ↓                                                       │
│  4. 分派规格审查子代理 (spec-reviewer-prompt.md)              │
│     ↓                                                       │
│  5. 规格符合？ → 否 → 实现者修复 → 返回步骤4                  │
│     ↓ 是                                                    │
│  6. 分派代码质量审查子代理 (code-quality-reviewer-prompt.md)  │
│     ↓                                                       │
│  7. 质量通过？ → 否 → 实现者修复 → 返回步骤6                  │
│     ↓ 是                                                    │
│  8. 标记任务完成                                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**子代理使用的 skill:**
- **test-driven-development**: 每个任务必须遵循 TDD（红-绿-重构）
- **requesting-code-review**: 代码审查模板

### 5. systematic-debugging 与其他 skill 的协作

**触发时机:**
- 任何测试失败
- Bug 报告
- 意外行为
- 性能问题

**与 TDD 的协作:**
```
systematic-debugging 阶段4.1:
"创建失败的测试用例"
→ 使用 superpowers:test-driven-development 编写正确的失败测试
```

**与 verification-before-completion 的协作:**
```
修复后必须验证:
1. 运行测试命令
2. 读取完整输出
3. 确认修复有效
→ 使用 verification-before-completion 确保不虚假声称完成
```

### 6. requesting-code-review ↔ receiving-code-review

**请求审查的流程:**
```
1. 获取 git SHA: BASE_SHA 和 HEAD_SHA
2. 分派 code-reviewer 子代理
3. 根据反馈行动:
   - Critical: 立即修复
   - Important: 继续前修复
   - Minor: 记录稍后处理
```

**处理反馈的流程:**
```
1. 完整阅读反馈（不立即反应）
2. 用自己的话重述需求
3. 根据代码库现实验证
4. 技术上是否合理？
5. 技术确认或有理有据地反驳
6. 逐一实现，每项测试
```

### 7. finishing-a-development-branch 的集成点

**被调用的场景:**
- subagent-driven-development 步骤7（所有任务完成后）
- executing-plans 步骤3（所有批次完成后）

**清理工作树:**
```
finishing-a-development-branch 与 using-git-worktrees 配对:
- 选项1（本地合并）→ 清理工作树
- 选项2（创建PR）→ 保留工作树
- 选项3（保持现状）→ 保留工作树
- 选项4（丢弃）→ 清理工作树
```

### 8. dispatching-parallel-agents 的使用场景

**独立问题的并行处理:**
```
场景: 6个测试失败分布在3个文件中

决策: 独立领域 - 中止逻辑、批处理完成、竞态条件是独立的

并行分派:
- 代理1 → 修复 agent-tool-abort.test.ts
- 代理2 → 修复 batch-completion-behavior.test.ts
- 代理3 → 修复 tool-approval-race-conditions.test.ts
```

**与 systematic-debugging 的区别:**
- systematic-debugging: 单一问题的深入调查
- dispatching-parallel-agents: 多个独立问题的并行解决

---

## 典型开发场景流程示例

### 场景1: 新功能开发

```
用户: "帮我实现一个用户认证功能"

using-superpowers (检查是否有适用的 skill)
    ↓
brainstorming (需求分析和设计)
    - 探索项目上下文
    - 澄清问题
    - 提出2-3种方案
    - 呈现设计
    - 编写规格文档
    - 规格审查循环
    - 用户审查规格
    ↓
writing-plans (创建实施计划)
    - 映射文件结构
    - 创建 bite-sized 任务
    - 计划审查循环
    ↓
using-git-worktrees (创建隔离工作区)
    ↓
subagent-driven-development (子代理执行每个任务)
    任务1: 实现登录接口
        - 分派实现者子代理
        - 规格审查
        - 代码质量审查
    任务2: 实现注册接口
        - 分派实现者子代理
        - 规格审查
        - 代码质量审查
    ...
    最终代码审查
    ↓
finishing-a-development-branch (完成开发)
    - 验证测试
    - 呈现选项
    - 执行选择
    - 清理工作树
```

### 场景2: Bug 修复

```
用户: "修复这个测试失败"

using-superpowers
    ↓
systematic-debugging (系统化调试)
    阶段1: 根本原因调查
    阶段2: 模式分析
    阶段3: 假设和测试
    阶段4: 实施
        - 创建失败测试
        - 使用 test-driven-development
    ↓
verification-before-completion
    - 运行测试命令
    - 确认修复有效
    ↓
requesting-code-review (可选)
    - 分派审查子代理
    ↓
finishing-a-development-branch
```

### 场景3: 多个独立 Bug 修复

```
用户: "这里有5个测试文件失败了"

分析: 失败是否独立？
    ↓ 是
using-git-worktrees (创建工作区)
    ↓
dispatching-parallel-agents (并行分派)
    代理1 → 修复文件A
        - systematic-debugging
        - test-driven-development
    代理2 → 修复文件B
        - systematic-debugging
        - test-driven-development
    代理3 → 修复文件C
        - systematic-debugging
        - test-driven-development
    ↓
审查和整合所有修复
    ↓
verification-before-completion
    - 运行完整测试套件
    ↓
finishing-a-development-branch
```

---

## Skill 使用优先级规则

### 1. 总是首先检查 skill

```
using-superpowers 规则:
"如果你认为有 1% 的可能性某个 skill 适用，
 你必须绝对调用该 skill 来检查"
```

### 2. 流程 skill 优先于实现 skill

```
顺序:
1. Process skills (brainstorming, debugging) - 决定如何执行任务
2. Implementation skills - 指导具体执行

示例:
"让我们构建 X" → 先 brainstorming，再实现 skill
"修复这个 bug" → 先 debugging，再领域特定 skill
```

### 3. 严格 skill vs 灵活 skill

```
严格 skill (必须完全遵循):
- test-driven-development
- systematic-debugging
- verification-before-completion

灵活 skill (可适应上下文):
- brainstorming
- writing-plans
```

---

## 常见错误和避免方法

### 错误1: 跳过 brainstorming 直接实现

```
❌ 错误:
用户: "添加一个功能"
你: "好的，我现在就开始写代码..."

✅ 正确:
用户: "添加一个功能"
你: "我将使用 brainstorming skill 来理解需求并设计解决方案"
```

### 错误2: 忘记创建隔离工作区

```
❌ 错误:
直接在当前分支开始修改

✅ 正确:
使用 using-git-worktrees 创建隔离工作区
```

### 错误3: 跳过测试验证就声称完成

```
❌ 错误:
"应该可以工作了"

✅ 正确:
使用 verification-before-completion:
- 运行测试命令
- 读取完整输出
- 然后才声称结果
```

### 错误4: 同时分派多个实现子代理

```
❌ 错误:
同时分派多个实现者子代理（会导致冲突）

✅ 正确:
subagent-driven-development 规则:
- 一次只有一个实现者子代理
- 完成当前任务后再开始下一个
```

### 错误5: 在规格审查前进行代码质量审查

```
❌ 错误:
实现 → 代码质量审查 → 规格审查

✅ 正确:
实现 → 规格审查（先确认符合规格）→ 代码质量审查
```

---

## 总结: Skill 调用决策树

```
开始对话
    ↓
using-superpowers: 检查适用的 skill
    ↓
需要设计/分析？
    ├─ 是 → brainstorming → writing-plans
    └─ 否 → 继续
    ↓
需要调试？
    ├─ 是 → systematic-debugging
    └─ 否 → 继续
    ↓
需要实现？
    ├─ 是 → using-git-worktrees → 
    │       (subagent-driven-development 或 executing-plans)
    └─ 否 → 继续
    ↓
完成任务后？
    ├─ 是 → requesting-code-review → 
    │       verification-before-completion → 
    │       finishing-a-development-branch
    └─ 否 → 继续
    ↓
多个独立问题？
    └─ 是 → dispatching-parallel-agents
```

---

## 附录: Skill 依赖关系图

```
using-superpowers (入口)
    │
    ├───> brainstorming
    │       └───> writing-plans
    │               ├───> subagent-driven-development ───┐
    │               │       ├───> using-git-worktrees    │
    │               │       ├───> test-driven-development │
    │               │       ├───> requesting-code-review  │
    │               │       └───> systematic-debugging    │
    │               │                                     │
    │               └───> executing-plans ────────────────┤
    │                       └───> using-git-worktrees     │
    │                                                     │
    ├───> systematic-debugging ───────────────────────────┤
    │       └───> test-driven-development                 │
    │                                                     │
    ├───> dispatching-parallel-agents                     │
    │       └───> systematic-debugging (每个代理)          │
    │                                                     │
    └───> receiving-code-review                           │
                                                            │
                    ┌───────────────────────────────────────┘
                    ▼
        verification-before-completion
                    │
                    ▼
        finishing-a-development-branch
                    │
                    └───> using-git-worktrees (清理)

writing-skills (独立，用于开发 skill 本身)
```

---

Mr黄，你参考一下，如果有不对的地方要及时和我说哦！
