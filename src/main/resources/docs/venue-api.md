# 场地状态管理模块 API 接口文档

## 概述

本文档描述了羽毛球场地预约系统中场地状态管理模块的所有API接口。该模块负责场地基础信息管理、时间表生成、特殊日期配置等功能。

## 基础信息

- **Base URL**: `http://localhost:8080`
- **认证方式**: JWT Token (放在请求头 Authorization: Bearer {token})
- **响应格式**: JSON

### 统一响应格式
```json
{
  "code": 0,
  "msg": "成功",
  "data": {...}
}
```

**状态码说明:**
- `0`: 成功
- `20001`: 场地不存在
- `20002`: 该时段已被预约
- `20003`: 场地维护中，暂不可预约
- `20004`: 预约时间无效
- `20005`: 预约时间已过
- `20006`: 不在可预约时间范围内
- `20012`: 特殊日期配置不存在

---

## 1. 场地管理接口

### 1.1 获取场地列表

**接口描述**: 获取所有场地的基础信息

**请求URL**: `/api/venue/list`

**请求方式**: `GET`

**请求参数**: 无

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "id": 1,
      "name": "羽毛球场1号",
      "description": "标准羽毛球场地，配备专业羽毛球网和照明设备",
      "location": "体育馆1号场地",
      "pricePerHour": 30.00,
      "type": 1,
      "typeDesc": "羽毛球场",
      "status": 1,
      "statusDesc": "可用",
      "createTime": "2024-01-15 10:00:00",
      "updateTime": "2024-01-15 10:00:00"
    }
  ]
}
```

### 1.2 获取场地详情

**接口描述**: 根据ID获取单个场地的详细信息

**请求URL**: `/api/venue/{id}`

**请求方式**: `GET`

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Integer | 是 | 场地ID |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "id": 1,
    "name": "羽毛球场1号",
    "description": "标准羽毛球场地，配备专业羽毛球网和照明设备",
    "location": "体育馆1号场地",
    "pricePerHour": 30.00,
    "type": 1,
    "typeDesc": "羽毛球场",
    "status": 1,
    "statusDesc": "可用",
    "createTime": "2024-01-15 10:00:00",
    "updateTime": "2024-01-15 10:00:00"
  }
}
```


### 1.4 更新场地状态 🔒

**接口描述**: 更新场地的可用状态 (管理员权限)

**请求URL**: `/api/venue/{id}/status`

**请求方式**: `PUT`

**权限要求**: 管理员

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Integer | 是 | 场地ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | Integer | 是 | 状态：1-可用，0-不可用 |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "场地状态更新成功"
}
```

---

## 2. 场地时间表接口

### 2.1 获取场地预约矩阵图

**接口描述**: 获取指定日期所有场地的时间段预约状况，用于前端展示矩阵图

**请求URL**: `/api/venue/schedule/matrix`

**请求方式**: `GET`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| scheduleDate | String | 是 | 查询日期，格式：yyyy-MM-dd |
| venueId | Integer | 否 | 场地ID，不传则查询所有场地 |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "venues": [
      {
        "id": 1,
        "name": "羽毛球场1号",
        "status": 1
      }
    ],
    "timeSlots": [
      {
        "startTime": "08:00",
        "endTime": "09:00"
      },
      {
        "startTime": "09:00",
        "endTime": "10:00"
      }
    ],
    "scheduleMatrix": {
      "1": {
        "08:00": {
          "id": 1,
          "venueId": 1,
          "venueName": "羽毛球场1号",
          "scheduleDate": "2024-01-16",
          "startTime": "08:00",
          "endTime": "09:00",
          "timeSlot": "08:00-09:00",
          "status": 2,
          "statusDesc": "使用中",
          "bookable": 0,
          "bookableDesc": "不可预约"
        }
      }
    }
  }
}
```

### 2.2 查询场地时间表

**接口描述**: 根据条件查询场地时间表详细信息

**请求URL**: `/api/venue/schedule/list`

**请求方式**: `GET`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| scheduleDate | String | 是 | 查询日期，格式：yyyy-MM-dd |
| venueId | Integer | 否 | 场地ID |
| startTime | String | 否 | 开始时间，格式：HH:mm |
| endTime | String | 否 | 结束时间，格式：HH:mm |
| status | Integer | 否 | 状态筛选 |
| bookable | Integer | 否 | 是否可预约：1-是，0-否 |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "id": 1,
      "venueId": 1,
      "venueName": "羽毛球场1号",
      "scheduleDate": "2024-01-16",
      "startTime": "18:00",
      "endTime": "19:00",
      "timeSlot": "18:00-19:00",
      "status": 1,
      "statusDesc": "空闲中",
      "bookable": 1,
      "bookableDesc": "可预约",
      "reservationId": null,
      "remark": null
    }
  ]
}
```

### 2.3 生成指定日期时间表 🔒

**接口描述**: 为指定日期生成所有场地的时间表 (管理员权限)

**请求URL**: `/api/venue/schedule/generate`

**请求方式**: `POST`

**权限要求**: 管理员

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| date | String | 是 | 日期，格式：yyyy-MM-dd |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "成功生成117条时间表记录"
}
```

### 2.4 批量生成时间表 🔒

**接口描述**: 批量生成未来几天的场地时间表 (管理员权限)

**请求URL**: `/api/venue/schedule/generate/batch`

**请求方式**: `POST`

**权限要求**: 管理员

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| days | Integer | 是 | 生成天数 |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "批量生成完成，共处理7天"
}
```

### 2.5 更新时间段状态 🔒

**接口描述**: 更新指定时间段的状态 (管理员权限)

**请求URL**: `/api/venue/schedule/{scheduleId}/status`

**请求方式**: `PUT`

**权限要求**: 管理员

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| scheduleId | Long | 是 | 时间表记录ID |

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | Integer | 是 | 状态：1-空闲中，2-使用中，3-已预约，4-维护中 |
| reservationId | Long | 否 | 预约订单ID（状态为已预约时必填） |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "状态更新成功"
}
```

### 2.6 检查时间段可用性

**接口描述**: 检查指定场地和时间段是否可以预约

**请求URL**: `/api/venue/schedule/check`

**请求方式**: `GET`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| venueId | Integer | 是 | 场地ID |
| date | String | 是 | 日期，格式：yyyy-MM-dd |
| startTime | String | 是 | 开始时间，格式：HH:mm |
| endTime | String | 是 | 结束时间，格式：HH:mm |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": true
}
```

### 1. 测试场地预约矩阵

```bash
# 查看今天的矩阵（工作日场景）
curl "http://localhost:8080/api/venue/schedule/matrix?scheduleDate=2024-12-09"

# 查看明天的矩阵（周末场景）
curl "http://localhost:8080/api/venue/schedule/matrix?scheduleDate=2024-12-10"
```

**预期结果**：
- 今天：1-3号场地白天使用中，4号场地维护中
- 明天：1-5号场地全天开放，5号场地大部分已预约

### 2. 测试场地时间表查询

```bash
# 查看所有时间表
curl "http://localhost:8080/api/venue/schedule/list?scheduleDate=2024-12-10"

# 查看可预约的时段
curl "http://localhost:8080/api/venue/schedule/list?scheduleDate=2024-12-10&status=1&bookable=1"

# 查看已预约的时段
curl "http://localhost:8080/api/venue/schedule/list?scheduleDate=2024-12-10&status=3"

# 查看特定场地
curl "http://localhost:8080/api/venue/schedule/list?scheduleDate=2024-12-10&venueId=1"
```

### 3. 测试时间段可用性检查

```bash
# 检查可用时段（应该返回true）
curl "http://localhost:8080/api/venue/schedule/check?venueId=1&date=2024-12-10&startTime=11:00&endTime=12:00"

# 检查已预约时段（应该返回false）
curl "http://localhost:8080/api/venue/schedule/check?venueId=1&date=2024-12-10&startTime=09:00&endTime=10:00"

# 检查维护中时段（应该返回false）
curl "http://localhost:8080/api/venue/schedule/check?venueId=4&date=2024-12-09&startTime=18:00&endTime=19:00"
---

## 3. 特殊日期配置接口

### 3.1 创建特殊日期配置 🔒

**接口描述**: 创建节假日、维护日等特殊日期配置 (管理员权限)

**请求URL**: `/api/venue/special-config`

**请求方式**: `POST`

**权限要求**: 管理员

**请求体**:
```json
{
  "configName": "元旦节",
  "specialDate": "2024-01-01",
  "configType": 1,
  "affectedVenueIds": "1,2,3",
  "startTime": "08:00",
  "endTime": "21:00",
  "venueStatus": 4,
  "bookable": 0,
  "description": "元旦节全天停止预约",
  "enabled": 1
}
```

**请求参数说明**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| configName | String | 是 | 配置名称 |
| specialDate | String | 是 | 特殊日期，格式：yyyy-MM-dd |
| configType | Integer | 是 | 配置类型：1-节假日，2-维护日，3-特殊开放日 |
| affectedVenueIds | String | 否 | 影响的场地ID，多个用逗号分隔，不填表示全部场地 |
| startTime | String | 否 | 影响开始时间，格式：HH:mm |
| endTime | String | 否 | 影响结束时间，格式：HH:mm |
| venueStatus | Integer | 是 | 特殊日期场地状态：1-空闲中，2-使用中，4-维护中 |
| bookable | Integer | 是 | 是否可预约：1-可预约，0-不可预约 |
| description | String | 否 | 配置描述 |
| enabled | Integer | 否 | 是否启用：1-启用，0-禁用，默认1 |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "特殊日期配置创建成功"
}
```

### 3.2 获取特殊日期配置列表 🔒

**接口描述**: 分页获取所有特殊日期配置 (管理员权限)

**请求URL**: `/api/venue/special-config/list`

**请求方式**: `GET`

**权限要求**: 管理员

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 页大小，默认10 |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "size": 2,
    "total": 2,
    "pages": 1,
    "list": [
      {
        "id": 1,
        "configName": "元旦节",
        "specialDate": "2024-01-01T00:00:00.000+00:00",
        "configType": 1,
        "affectedVenueIds": "1,2,3",
        "startTime": "08:00",
        "endTime": "21:00",
        "venueStatus": 4,
        "bookable": 0,
        "description": "元旦节全天停止预约",
        "enabled": 1,
        "createTime": "2024-01-15T10:00:00.000+00:00",
        "updateTime": "2024-01-15T10:00:00.000+00:00"
      }
    ]
  }
}
```

### 3.3 获取特殊日期配置详情 🔒

**接口描述**: 根据ID获取特殊日期配置详情 (管理员权限)

**请求URL**: `/api/venue/special-config/{id}`

**请求方式**: `GET`

**权限要求**: 管理员

**路径参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 配置ID |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "id": 1,
    "configName": "元旦节",
    "specialDate": "2024-01-01T00:00:00.000+00:00",
    "configType": 1,
    "affectedVenueIds": "1,2,3",
    "startTime": "08:00",
    "endTime": "21:00",
    "venueStatus": 4,
    "bookable": 0,
    "description": "元旦节全天停止预约",
    "enabled": 1,
    "createTime": "2024-01-15T10:00:00.000+00:00",
    "updateTime": "2024-01-15T10:00:00.000+00:00"
  }
}
```

### 3.4 更新特殊日期配置 🔒

**接口描述**: 更新特殊日期配置 (管理员权限)

**请求URL**: `/api/venue/special-config/{id}`

**请求方式**: `PUT`

**权限要求**: 管理员

**请求体**: 同创建接口

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "特殊日期配置更新成功"
}
```

### 3.5 删除特殊日期配置 🔒

**接口描述**: 删除特殊日期配置 (管理员权限)

**请求URL**: `/api/venue/special-config/{id}`

**请求方式**: `DELETE`

**权限要求**: 管理员

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "特殊日期配置删除成功"
}
```

### 3.6 启用/禁用特殊日期配置 🔒

**接口描述**: 启用或禁用特殊日期配置 (管理员权限)

**请求URL**: `/api/venue/special-config/{id}/toggle`

**请求方式**: `PUT`

**权限要求**: 管理员

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| enabled | Integer | 是 | 启用状态：1-启用，0-禁用 |

**响应示例**:
```json
{
  "code": 0,
  "msg": "成功",
  "data": "配置启用成功"
}
```

---

## 4. 业务规则说明

### 4.1 默认时间规则

**工作日（周一至周五）**:
- 08:00-18:00: 使用中，不可预约（教学时间）
- 18:00-21:00: 空闲中，可预约
- 其他时间: 维护中，不可预约

**周末（周六日）**:
- 08:00-21:00: 空闲中，可预约
- 其他时间: 维护中，不可预约

### 4.2 预约时间限制

- 用户只能在前一天18:00后预约明天的场地
- 当天18:00后可以预约当天剩余的还未开始的时段和明天的时段
- 不能预约过去的时间

### 4.3 特殊日期配置优先级

特殊日期配置会覆盖默认规则，按照以下优先级：
1. 启用的特殊日期配置
2. 默认时间规则

---

## 5. 前端开发指南

### 5.1 场地预约矩阵图实现

1. 调用 `/api/venue/schedule/matrix` 获取数据
2. 解析返回的 `venues`、`timeSlots` 和 `scheduleMatrix`
3. 使用表格展示，场地作为行，时间段作为列
4. 根据 `status` 和 `bookable` 设置不同颜色：
   - 空闲中且可预约: 绿色
   - 使用中: 灰色
   - 已预约: 红色
   - 维护中: 橙色

### 5.2 状态颜色建议

```css
.venue-available { background-color: #52c41a; } /* 绿色-可预约 */
.venue-in-use { background-color: #d9d9d9; }    /* 灰色-使用中 */
.venue-booked { background-color: #ff4d4f; }    /* 红色-已预约 */
.venue-maintenance { background-color: #fa8c16; } /* 橙色-维护中 */
```

---

## 6. 错误处理

### 6.1 常见错误码

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 20001 | 场地不存在 | 检查场地ID是否正确 |
| 20002 | 该时段已被预约 | 提示用户选择其他时段 |
| 20003 | 场地维护中 | 提示用户该时段不可预约 |
| 20006 | 不在可预约时间范围内 | 检查预约时间规则 |
| 20012 | 特殊日期配置不存在 | 检查配置ID是否正确 |

### 6.2 权限错误

所有标记为 🔒 的接口都需要管理员权限，如果返回 `401` 或 `403` 错误，请检查：
1. 是否已登录
2. 当前用户是否有管理员权限
3. Token是否有效

---

## 7. 测试数据

### 7.1 测试用例

1. **场地列表查询**: 确保返回9个羽毛球场
2. **时间表生成**: 生成今天的时间表，验证工作日/周末规则
3. **特殊日期配置**: 创建节假日配置，验证是否生效
4. **预约矩阵图**: 查看可视化效果

### 7.2 性能要求

- 场地列表查询: < 100ms
- 时间表查询: < 200ms
- 矩阵图查询: < 500ms
- 时间表生成: < 2s 