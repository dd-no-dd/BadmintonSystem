# 羽毛球系统商城模块API文档

## 1. 商品分类接口

### 1.1 获取商品分类列表

**接口描述**：获取所有有效的商品分类

**请求URL**：`/api/mall/categories`

**请求方式**：`GET`

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Array | 分类列表 |
| &emsp;id | Integer | 分类ID |
| &emsp;name | String | 分类名称 |
| &emsp;status | Integer | 分类状态：1-正常，2-已废弃 |
| &emsp;sortOrder | Integer | 排序编号 |
| &emsp;createTime | Date | 创建时间 |
| &emsp;updateTime | Date | 更新时间 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "success",
  "data": [
    {
      "id": 100001,
      "name": "球拍",
      "status": 1,
      "sortOrder": 1,
      "createTime": "2023-05-10 12:00:00",
      "updateTime": "2023-05-10 12:00:00"
    },
    {
      "id": 100002,
      "name": "羽毛球",
      "status": 1,
      "sortOrder": 2,
      "createTime": "2023-05-10 12:00:00",
      "updateTime": "2023-05-10 12:00:00"
    }
  ]
}
```

## 2. 商品接口

### 2.1 获取商品列表

**接口描述**：获取商品列表，支持分类筛选、关键词搜索、排序和分页

**请求URL**：`/api/mall/products`

**请求方式**：`GET`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| categoryId | 否 | String | 分类ID，默认值为"all"，表示所有分类 |
| keyword | 否 | String | 搜索关键词，匹配商品名称或副标题 |
| pageNum | 否 | Integer | 页码，默认为1 |
| pageSize | 否 | Integer | 每页数量，默认为10 |
| orderBy | 否 | String | 排序方式：price_asc（价格升序）、price_desc（价格降序）、sales_desc（销量降序） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 商品列表数据 |
| &emsp;pageNum | Integer | 当前页码 |
| &emsp;pageSize | Integer | 每页数量 |
| &emsp;size | Integer | 当前页实际数量 |
| &emsp;total | Long | 总数量 |
| &emsp;list | Array | 商品列表 |
| &emsp;&emsp;id | Integer | 商品ID |
| &emsp;&emsp;categoryId | Integer | 分类ID |
| &emsp;&emsp;categoryName | String | 分类名称 |
| &emsp;&emsp;name | String | 商品名称 |
| &emsp;&emsp;subtitle | String | 商品副标题 |
| &emsp;&emsp;mainImage | String | 主图URL |
| &emsp;&emsp;price | Decimal | 价格 |
| &emsp;&emsp;sales | Integer | 销量 |
| &emsp;&emsp;status | Integer | 状态：1-在售，2-下架 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "size": 2,
    "total": 2,
    "list": [
      {
        "id": 1,
        "categoryId": 100001,
        "categoryName": "球拍",
        "name": "YONEX弓箭11",
        "subtitle": "专业比赛羽毛球拍",
        "mainImage": "http://example.com/images/1.jpg",
        "price": 850.00,
        "sales": 128,
        "status": 1
      },
      {
        "id": 2,
        "categoryId": 100002,
        "categoryName": "羽毛球",
        "name": "YONEX AS-50",
        "subtitle": "耐打比赛用球",
        "mainImage": "http://example.com/images/2.jpg",
        "price": 120.00,
        "sales": 256,
        "status": 1
      }
    ]
  }
}
```

### 2.2 获取商品详情

**接口描述**：获取商品详细信息

**请求URL**：`/api/mall/products/{productId}`

**请求方式**：`GET`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 商品详情 |
| &emsp;id | Integer | 商品ID |
| &emsp;categoryId | Integer | 分类ID |
| &emsp;categoryName | String | 分类名称 |
| &emsp;name | String | 商品名称 |
| &emsp;subtitle | String | 商品副标题 |
| &emsp;mainImage | String | 主图URL |
| &emsp;subImages | String | 子图URL，逗号分隔 |
| &emsp;detail | String | 商品详情，支持HTML |
| &emsp;price | Decimal | 价格 |
| &emsp;stock | Integer | 库存 |
| &emsp;sales | Integer | 销量 |
| &emsp;status | Integer | 状态：1-在售，2-下架 |
| &emsp;hasSpecification | Integer | 是否有规格：0-无规格，1-有规格 |
| &emsp;specifications | Array | 商品规格列表，hasSpecification=1时返回 |
| &emsp;&emsp;id | Integer | 规格ID |
| &emsp;&emsp;specifications | Object | 规格信息，如{"color":"红色","size":"S"} |
| &emsp;&emsp;priceAdjustment | Decimal | 价格调整 |
| &emsp;&emsp;stock | Integer | 该规格库存 |
| &emsp;specOptions | Object | 规格选项，如{"color":["红色","蓝色"],"size":["S","M","L"]} |
| &emsp;createTime | Date | 创建时间 |
| &emsp;updateTime | Date | 更新时间 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "success",
  "data": {
    "id": 1,
    "categoryId": 100001,
    "categoryName": "球拍",
    "name": "YONEX弓箭11",
    "subtitle": "专业比赛羽毛球拍",
    "mainImage": "http://example.com/images/1.jpg",
    "subImages": "http://example.com/images/1_1.jpg,http://example.com/images/1_2.jpg",
    "detail": "<p>商品详情，支持HTML</p>",
    "price": 850.00,
    "stock": 100,
    "sales": 128,
    "status": 1,
    "hasSpecification": 1,
    "specifications": [
      {
        "id": 1,
        "specifications": {"color": "红色", "size": "S"},
        "priceAdjustment": 0.00,
        "stock": 30
      },
      {
        "id": 2,
        "specifications": {"color": "蓝色", "size": "M"},
        "priceAdjustment": 10.00,
        "stock": 20
      }
    ],
    "specOptions": {
      "color": ["红色", "蓝色", "黑色"],
      "size": ["S", "M", "L"]
    },
    "createTime": "2023-05-10 12:00:00",
    "updateTime": "2023-05-10 12:00:00"
  }
}
```

### 2.3 上传商品图片

**接口描述**：上传商品图片（需要管理员权限）

**请求URL**：`/api/mall/products/upload`

**请求方式**：`POST`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| mainImage | 是 | File | 商品主图 |
| subImages | 否 | File[] | 商品子图，可多个 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | String | 上传成功的图片JSON字符串，包含图片URL |

**响应示例**：

```json
{
  "status": 0,
  "msg": "上传成功",
  "data": "{\"mainImage\":\"http://example.com/images/1.jpg\",\"subImages\":[\"http://example.com/images/1_1.jpg\",\"http://example.com/images/1_2.jpg\"]}"
}
```

### 2.4 添加商品

**接口描述**：添加新商品（需要管理员权限）

**请求URL**：`/api/mall/products`

**请求方式**：`POST`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| categoryId | 是 | Integer | 分类ID |
| name | 是 | String | 商品名称 |
| subtitle | 否 | String | 商品副标题 |
| mainImage | 是 | String | 主图URL，通过上传图片接口获取 |
| subImages | 否 | String | 子图URL，通过上传图片接口获取，多张图片用逗号分隔 |
| detail | 否 | String | 商品详情，支持HTML |
| price | 是 | Decimal | 价格，必须大于0 |
| stock | 是 | Integer | 库存，必须大于等于0 |
| status | 否 | Integer | 状态：1-在售，2-下架，默认为1 |

**请求示例**：

```json
{
  "categoryId": 100001,
  "name": "YONEX弓箭11",
  "subtitle": "专业比赛羽毛球拍",
  "mainImage": "http://example.com/images/1.jpg",
  "subImages": "http://example.com/images/1_1.jpg,http://example.com/images/1_2.jpg",
  "detail": "<p>商品详情，支持HTML</p>",
  "price": 850.00,
  "stock": 100,
  "status": 1
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 返回数据 |
| &emsp;productId | Integer | 新添加的商品ID |

**响应示例**：

```json
{
  "status": 0,
  "msg": "添加商品成功",
  "data": {
    "productId": 1
  }
}
```

### 2.5 更新商品

**接口描述**：更新商品信息（需要管理员权限）

**请求URL**：`/api/mall/products/{productId}`

**请求方式**：`PUT`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |
| categoryId | 否 | Integer | 分类ID |
| name | 否 | String | 商品名称 |
| subtitle | 否 | String | 商品副标题 |
| mainImage | 否 | String | 主图URL |
| subImages | 否 | String | 子图URL，多张图片用逗号分隔 |
| detail | 否 | String | 商品详情，支持HTML |
| price | 否 | Decimal | 价格，必须大于0 |
| stock | 否 | Integer | 库存，必须大于等于0 |
| status | 否 | Integer | 状态：1-在售，2-下架 |

**请求示例**：

```json
{
  "name": "YONEX弓箭11 Pro",
  "subtitle": "专业比赛羽毛球拍升级版",
  "price": 899.00
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "更新商品成功",
  "data": null
}
```

### 2.6 商品上架

**接口描述**：将商品状态改为在售（需要管理员权限）

**请求URL**：`/api/mall/products/{productId}/on_sale`

**请求方式**：`PUT`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "上架商品成功",
  "data": null
}
```

### 2.7 商品下架

**接口描述**：将商品状态改为下架（需要管理员权限）

**请求URL**：`/api/mall/products/{productId}/off_sale`

**请求方式**：`PUT`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "下架商品成功",
  "data": null
}
```

### 2.8 删除商品

**接口描述**：删除商品（需要管理员权限）

**请求URL**：`/api/mall/products/{productId}`

**请求方式**：`DELETE`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "删除商品成功",
  "data": null
}
```

### 2.9 更新商品库存

**接口描述**：更新商品库存（需要管理员权限）

**请求URL**：`/api/mall/products/{productId}/stock`

**请求方式**：`PUT`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |
| stock | 是 | Integer | 新的库存数量，必须大于等于0 |

**请求示例**：

```json
{
  "stock": 50
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "更新库存成功",
  "data": null
}
```

## 3. 商品规格接口

### 3.1 获取商品规格列表

**接口描述**：获取商品的所有规格信息

**请求URL**：`/api/mall/products/{productId}/specifications`

**请求方式**：`GET`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Array | 规格列表 |
| &emsp;id | Integer | 规格ID |
| &emsp;productId | Integer | 商品ID |
| &emsp;specifications | Object | 规格信息，如{"color":"红色","size":"S"} |
| &emsp;priceAdjustment | Decimal | 价格调整，可以为负数表示折扣 |
| &emsp;stock | Integer | 该规格库存 |
| &emsp;sales | Integer | 该规格销量 |
| &emsp;status | Integer | 状态：1-正常，0-禁用 |
| &emsp;createTime | Date | 创建时间 |
| &emsp;updateTime | Date | 更新时间 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "specifications": {"color": "红色", "size": "S"},
      "priceAdjustment": 0.00,
      "stock": 30,
      "sales": 10,
      "status": 1,
      "createTime": "2023-05-10 12:00:00",
      "updateTime": "2023-05-10 12:00:00"
    },
    {
      "id": 2,
      "productId": 1,
      "specifications": {"color": "蓝色", "size": "M"},
      "priceAdjustment": 10.00,
      "stock": 20,
      "sales": 5,
      "status": 1,
      "createTime": "2023-05-10 12:00:00",
      "updateTime": "2023-05-10 12:00:00"
    }
  ]
}
```

### 3.2 根据规格条件获取特定商品规格

**接口描述**：根据规格条件（如颜色、尺寸等）获取匹配的商品规格信息

**请求URL**：`/api/mall/products/{productId}/specification`

**请求方式**：`POST`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |
| specifications | 是 | Object | 规格条件，如{"color":"红色","size":"S"} |

**请求示例**：

```json
{
  "color": "红色",
  "size": "S"
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 规格信息 |
| &emsp;id | Integer | 规格ID |
| &emsp;productId | Integer | 商品ID |
| &emsp;specifications | Object | 规格信息，如{"color":"红色","size":"S"} |
| &emsp;priceAdjustment | Decimal | 价格调整 |
| &emsp;stock | Integer | 该规格库存 |
| &emsp;sales | Integer | 该规格销量 |
| &emsp;status | Integer | 状态 |
| &emsp;createTime | Date | 创建时间 |
| &emsp;updateTime | Date | 更新时间 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "success",
  "data": {
    "id": 1,
    "productId": 1,
    "specifications": {"color": "红色", "size": "S"},
    "priceAdjustment": 0.00,
    "stock": 30,
    "sales": 10,
    "status": 1,
    "createTime": "2023-05-10 12:00:00",
    "updateTime": "2023-05-10 12:00:00"
  }
}
```

### 3.3 获取商品规格选项

**接口描述**：获取商品的规格选项，用于前端展示可选规格

**请求URL**：`/api/mall/products/{productId}/spec_options`

**请求方式**：`GET`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Array | 规格选项列表 |
| &emsp;id | Integer | 选项ID |
| &emsp;productId | Integer | 商品ID |
| &emsp;specKey | String | 规格类型，如"color"、"size" |
| &emsp;specValues | Array | 可选值列表，如["红色","蓝色","黑色"] |
| &emsp;createTime | Date | 创建时间 |
| &emsp;updateTime | Date | 更新时间 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "specKey": "color",
      "specValues": ["红色", "蓝色", "黑色"],
      "createTime": "2023-05-10 12:00:00",
      "updateTime": "2023-05-10 12:00:00"
    },
    {
      "id": 2,
      "productId": 1,
      "specKey": "size",
      "specValues": ["S", "M", "L"],
      "createTime": "2023-05-10 12:00:00",
      "updateTime": "2023-05-10 12:00:00"
    }
  ]
}
```

### 3.4 添加商品规格

**接口描述**：为商品添加规格（需要管理员权限）

**请求URL**：`/api/mall/products/{productId}/specifications`

**请求方式**：`POST`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| productId | 是 | Integer | 商品ID（路径参数） |
| specifications | 是 | Object | 规格信息，如{"color":"红色","size":"S"} |
| priceAdjustment | 是 | Decimal | 价格调整，可以为负数表示折扣 |
| stock | 是 | Integer | 该规格库存，必须大于等于0 |
| status | 否 | Integer | 状态：1-正常，0-禁用，默认为1 |

**请求示例**：

```json
{
  "specifications": {"color": "黑色", "size": "L"},
  "priceAdjustment": 20.00,
  "stock": 15
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 返回数据 |
| &emsp;specificationId | Integer | 新添加的规格ID |

**响应示例**：

```json
{
  "status": 0,
  "msg": "添加商品规格成功",
  "data": {
    "specificationId": 3
  }
}
```

### 3.5 更新商品规格

**接口描述**：更新商品规格信息（需要管理员权限）

**请求URL**：`/api/mall/specifications/{specificationId}`

**请求方式**：`PUT`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| specificationId | 是 | Integer | 规格ID（路径参数） |
| specifications | 否 | Object | 规格信息，如{"color":"红色","size":"S"} |
| priceAdjustment | 否 | Decimal | 价格调整 |
| stock | 否 | Integer | 库存，必须大于等于0 |
| status | 否 | Integer | 状态：1-正常，0-禁用 |

**请求示例**：

```json
{
  "priceAdjustment": 15.00,
  "stock": 25
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "更新商品规格成功",
  "data": null
}
```

### 3.6 删除商品规格

**接口描述**：删除商品规格（需要管理员权限）

**请求URL**：`/api/mall/specifications/{specificationId}`

**请求方式**：`DELETE`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| specificationId | 是 | Integer | 规格ID（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "删除商品规格成功",
  "data": null
}
```

### 3.7 更新商品规格库存

**接口描述**：更新商品规格库存（需要管理员权限）

**请求URL**：`/api/mall/specifications/{specificationId}/stock`

**请求方式**：`PUT`

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| specificationId | 是 | Integer | 规格ID（路径参数） |
| stock | 是 | Integer | 新的库存数量，必须大于等于0 |

**请求示例**：

```json
{
  "stock": 50
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | null | 无返回数据 |

**响应示例**：

```json
{
  "status": 0,
  "msg": "更新规格库存成功",
  "data": null
}
```

## 3. 错误码说明

| 错误码 | 说明 |
| ------ | ------ |
| 0 | 成功 |
| 401 | 未登录 |
| 4001 | 参数错误，分类不存在 |
| 4002 | 参数错误，排序方式无效 |
| 4003 | 没有权限进行此操作/商品不存在或已下架 |
| 4004 | 参数验证错误（如：主图不能为空，商品名称不能为空等） |
| 5000 | 操作失败 |
| 999 | 服务器错误 |


## 前端实现指南

### 1. 商品展示功能

#### 商品分类展示
1. 使用`GET /api/mall/categories`获取所有有效商品分类
2. 将分类列表渲染为导航菜单或筛选选项
3. 实现示例:
```javascript
// 获取分类列表
axios.get('/api/mall/categories')
  .then(response => {
    if(response.data.status === 0) {
      this.categories = response.data.data;
    }
  })
  .catch(error => {
    console.error('获取分类失败:', error);
  });
```

#### 商品列表展示
1. 使用`GET /api/mall/products`获取商品列表，支持分页、搜索和排序
2. 实现筛选面板，包含分类选择、关键词搜索和排序选项
3. 实现分页控件，显示当前页码和跳转功能
4. 实现示例:
```javascript
// 获取商品列表
function getProductList(params = {}) {
  const defaultParams = {
    categoryId: 'all',
    pageNum: 1,
    pageSize: 10
  };
  const queryParams = {...defaultParams, ...params};
  
  axios.get('/api/mall/products', {params: queryParams})
    .then(response => {
      if(response.data.status === 0) {
        this.products = response.data.data.list;
        this.pagination = {
          pageNum: response.data.data.pageNum,
          pageSize: response.data.data.pageSize,
          total: response.data.data.total
        };
      }
    })
    .catch(error => {
      console.error('获取商品列表失败:', error);
    });
}
```

#### 商品详情展示
1. 使用`GET /api/mall/products/{productId}`获取商品详情
2. 展示商品详细信息，包括图片、价格、库存等
3. 显示商品详情HTML内容（注意安全处理）
4. 实现示例:
```javascript
// 获取商品详情
function getProductDetail(productId) {
  axios.get(`/api/mall/products/${productId}`)
    .then(response => {
      if(response.data.status === 0) {
        this.productDetail = response.data.data;
        // 解析子图列表
        if(this.productDetail.subImages) {
          this.subImageList = this.productDetail.subImages.split(',');
        }
      }
    })
    .catch(error => {
      console.error('获取商品详情失败:', error);
    });
}
```

### 2. 商品管理功能（管理员）

#### 上传商品图片
1. 创建表单上传组件，支持选择主图和多个子图
2. 使用`POST /api/mall/products/upload`上传图片
3. 将返回的图片URL保存用于商品添加/编辑
4. 实现示例:
```javascript
// 上传商品图片
function uploadProductImages(formData) {
  axios.post('/api/mall/products/upload', formData, {
    headers: {'Content-Type': 'multipart/form-data'}
  })
    .then(response => {
      if(response.data.status === 0) {
        // 解析JSON字符串获取图片URL
        const imageData = JSON.parse(response.data.data);
        this.mainImageUrl = imageData.mainImage;
        this.subImageUrls = imageData.subImages.join(',');
      }
    })
    .catch(error => {
      console.error('上传图片失败:', error);
    });
}
```

#### 添加商品
1. 创建商品表单，包含分类选择、名称、价格等字段
2. 集成图片上传功能
3. 使用`POST /api/mall/products`添加商品
4. 实现示例:
```javascript
// 添加商品
function addProduct(productData) {
  axios.post('/api/mall/products', productData)
    .then(response => {
      if(response.data.status === 0) {
        this.$message.success('添加商品成功');
        this.productId = response.data.data.productId;
        // 跳转到商品列表或详情页
      }
    })
    .catch(error => {
      console.error('添加商品失败:', error);
    });
}
```

#### 编辑商品
1. 创建与添加商品相似的表单，但预填充现有商品数据
2. 使用`PUT /api/mall/products/{productId}`更新商品
3. 实现示例:
```javascript
// 更新商品
function updateProduct(productId, productData) {
  axios.put(`/api/mall/products/${productId}`, productData)
    .then(response => {
      if(response.data.status === 0) {
        this.$message.success('更新商品成功');
        // 刷新商品数据或返回列表
      }
    })
    .catch(error => {
      console.error('更新商品失败:', error);
    });
}
```

#### 商品状态管理
1. 实现上架/下架按钮
2. 使用`PUT /api/mall/products/{productId}/on_sale`和`PUT /api/mall/products/{productId}/off_sale`切换状态
3. 实现示例:
```javascript
// 上架商品
function onSaleProduct(productId) {
  axios.put(`/api/mall/products/${productId}/on_sale`)
    .then(response => {
      if(response.data.status === 0) {
        this.$message.success('商品上架成功');
        // 刷新商品状态
      }
    })
    .catch(error => {
      console.error('商品上架失败:', error);
    });
}

// 下架商品
function offSaleProduct(productId) {
  axios.put(`/api/mall/products/${productId}/off_sale`)
    .then(response => {
      if(response.data.status === 0) {
        this.$message.success('商品下架成功');
        // 刷新商品状态
      }
    })
    .catch(error => {
      console.error('商品下架失败:', error);
    });
}
```

### 3. 实现建议

1. **使用组件化开发**：
   - 将商品卡片、分页控件、筛选面板等抽象为可复用组件
   - 使用Vue、React等前端框架实现状态管理

2. **响应式布局**：
   - 使用Grid或Flex布局实现商品列表的响应式显示
   - 针对移动设备优化界面

3. **图片处理**：
   - 实现图片预览放大功能
   - 商品详情页实现图片轮播

4. **表单验证**：
   - 对添加/编辑商品表单进行前端验证
   - 价格必须大于0，库存不能为负数等

5. **权限控制**：
   - 根据用户角色显示/隐藏管理功能
   - 对管理操作进行权限验证

6. **错误处理**：
   - 统一处理API错误响应
   - 友好展示错误信息

7. **缓存优化**：
   - 缓存分类列表等不常变化的数据
   - 使用LocalStorage存储用户偏好（如排序方式、每页显示数量）

## 4. 商品规格功能

商品规格功能允许商家为同一商品添加不同的规格组合（如颜色、尺寸等），每种组合可以有不同的价格调整和库存量。以下是实现商品规格功能的详细指南。

### 4.1 规格功能概述

商品规格系统由三个主要部分组成：
1. **规格选项**：可选的规格属性和值（如颜色：红色、蓝色、黑色；尺寸：S、M、L等）
2. **规格组合**：特定规格属性值的组合（如红色S码、蓝色M码等）及其对应的价格调整和库存
3. **规格展示与选择**：前端用户界面，允许用户选择需要的规格组合

### 4.2 后端实现

后端已经实现以下功能：
- 在商品表中添加`has_specification`字段标识商品是否有规格
- 创建规格表存储具体规格组合信息
- 创建规格选项表存储可选规格值信息
- 提供相关API供前端调用

### 4.3 前端规格展示实现

#### 4.3.1 检测商品是否有规格

```javascript
function checkProductSpecifications(product) {
  if (product.hasSpecification === 1) {
    // 商品有规格，加载规格选项
    this.loadSpecificationOptions(product.id);
  } else {
    // 商品无规格，直接显示价格和库存
    this.selectedProduct = product;
    this.canAddToCart = product.stock > 0;
  }
}
```

#### 4.3.2 加载规格选项

```javascript
function loadSpecificationOptions(productId) {
  axios.get(`/api/mall/products/${productId}/spec_options`)
    .then(response => {
      if (response.data.status === 0) {
        this.specOptions = response.data.data;
        // 初始化选中规格
        this.initSelectedSpecs();
      }
    })
    .catch(error => {
      console.error('获取规格选项失败:', error);
    });
}

function initSelectedSpecs() {
  // 初始化一个空的已选规格对象
  this.selectedSpecs = {};
  
  // 可以默认选中第一个选项
  this.specOptions.forEach(option => {
    if (option.specValues && option.specValues.length > 0) {
      this.selectedSpecs[option.specKey] = option.specValues[0];
    }
  });
  
  // 根据初始化的规格选择更新价格和库存
  this.updateSpecificationDetails();
}
```

#### 4.3.3 规格选择变更处理

```javascript
function changeSpecification(specKey, specValue) {
  // 更新选中的规格
  this.selectedSpecs[specKey] = specValue;
  
  // 获取更新后的价格和库存
  this.updateSpecificationDetails();
}

function updateSpecificationDetails() {
  // 发送请求获取选中规格组合的价格和库存
  axios.post(`/api/mall/products/${this.productId}/specification`, this.selectedSpecs)
    .then(response => {
      if (response.data.status === 0) {
        const specData = response.data.data;
        this.selectedSpecification = specData;
        
        // 更新显示的价格（基础价格+调整值）
        this.displayPrice = this.productDetail.price + specData.priceAdjustment;
        
        // 更新库存状态
        this.canAddToCart = specData.stock > 0;
        
        // 存储规格ID，用于后续加入购物车
        this.selectedSpecificationId = specData.id;
      } else {
        // 未找到匹配的规格组合
        this.canAddToCart = false;
        this.$message.warning('所选规格组合不可用');
      }
    })
    .catch(error => {
      console.error('获取规格详情失败:', error);
      this.canAddToCart = false;
    });
}
```

#### 4.3.4 规格选择UI实现

```html
<template>
  <div class="spec-selection" v-if="productDetail.hasSpecification === 1">
    <div v-for="option in specOptions" :key="option.id" class="spec-group">
      <div class="spec-title">{{ formatSpecKey(option.specKey) }}:</div>
      <div class="spec-values">
        <span 
          v-for="value in option.specValues" 
          :key="value"
          :class="['spec-value', selectedSpecs[option.specKey] === value ? 'selected' : '']"
          @click="changeSpecification(option.specKey, value)">
          {{ value }}
        </span>
      </div>
    </div>
    
    <div class="spec-price" v-if="selectedSpecification">
      <span class="label">价格:</span>
      <span class="value">¥{{ displayPrice.toFixed(2) }}</span>
      <span class="original" v-if="selectedSpecification.priceAdjustment !== 0">
        ({{ selectedSpecification.priceAdjustment > 0 ? '+' : '' }}{{ selectedSpecification.priceAdjustment }})
      </span>
    </div>
    
    <div class="spec-stock">
      <span class="label">库存:</span>
      <span class="value" :class="{ 'out-of-stock': !canAddToCart }">
        {{ canAddToCart ? selectedSpecification.stock : '无货' }}
      </span>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    formatSpecKey(key) {
      // 将规格键转换为用户友好的显示文本
      const keyMap = {
        'color': '颜色',
        'size': '尺码',
        'material': '材质',
        'style': '款式'
      };
      return keyMap[key] || key;
    }
  }
}
</script>

<style scoped>
.spec-group {
  margin-bottom: 15px;
}
.spec-title {
  font-weight: bold;
  margin-bottom: 8px;
}
.spec-values {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.spec-value {
  padding: 6px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}
.spec-value:hover {
  border-color: #ff6700;
}
.spec-value.selected {
  border-color: #ff6700;
  background-color: #ff67001a;
  color: #ff6700;
}
.spec-price {
  font-size: 18px;
  margin: 15px 0;
}
.spec-price .value {
  color: #ff6700;
  font-weight: bold;
}
.spec-price .original {
  font-size: 14px;
  color: #666;
}
.out-of-stock {
  color: #ff0000;
}
</style>
```

### 4.4 商品规格管理（管理员）

#### 4.4.1 添加商品规格

```javascript
function addSpecification(productId, specData) {
  axios.post(`/api/mall/products/${productId}/specifications`, specData)
    .then(response => {
      if (response.data.status === 0) {
        this.$message.success('添加规格成功');
        this.loadProductSpecifications(productId);
      }
    })
    .catch(error => {
      console.error('添加规格失败:', error);
    });
}
```

#### 4.4.2 更新商品规格

```javascript
function updateSpecification(specificationId, specData) {
  axios.put(`/api/mall/specifications/${specificationId}`, specData)
    .then(response => {
      if (response.data.status === 0) {
        this.$message.success('更新规格成功');
        this.loadProductSpecifications(this.productId);
      }
    })
    .catch(error => {
      console.error('更新规格失败:', error);
    });
}
```

#### 4.4.3 删除商品规格

```javascript
function deleteSpecification(specificationId) {
  this.$confirm('确定要删除该规格吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    axios.delete(`/api/mall/specifications/${specificationId}`)
      .then(response => {
        if (response.data.status === 0) {
          this.$message.success('删除规格成功');
          this.loadProductSpecifications(this.productId);
        }
      })
      .catch(error => {
        console.error('删除规格失败:', error);
      });
  }).catch(() => {
    this.$message.info('已取消删除');
  });
}
```

#### 4.4.4 规格批量添加UI

```html
<template>
  <div class="specification-manager">
    <h3>规格管理</h3>
    
    <!-- 规格选项管理 -->
    <div class="spec-options-section">
      <h4>步骤1: 添加规格类型和选项</h4>
      <div v-for="(options, index) in specOptionsForm" :key="index" class="spec-option-row">
        <el-select v-model="options.key" placeholder="规格类型">
          <el-option label="颜色" value="color"></el-option>
          <el-option label="尺码" value="size"></el-option>
          <el-option label="材质" value="material"></el-option>
          <el-option label="款式" value="style"></el-option>
          <el-option label="自定义" value="custom"></el-option>
        </el-select>
        
        <el-input 
          v-if="options.key === 'custom'" 
          v-model="options.customKey" 
          placeholder="自定义规格类型"
          style="width: 150px; margin: 0 10px;">
        </el-input>
        
        <el-select 
          v-model="options.values" 
          multiple
          filterable
          allow-create
          default-first-option
          placeholder="规格选项值">
          <el-option 
            v-for="item in getDefaultOptions(options.key)"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
        
        <el-button 
          v-if="index === specOptionsForm.length - 1" 
          type="primary" 
          icon="el-icon-plus" 
          circle
          @click="addSpecOptionRow">
        </el-button>
        
        <el-button 
          v-if="specOptionsForm.length > 1" 
          type="danger" 
          icon="el-icon-delete" 
          circle
          @click="removeSpecOptionRow(index)">
        </el-button>
      </div>
    </div>
    
    <!-- 规格组合生成 -->
    <div class="spec-combinations-section">
      <h4>步骤2: 生成规格组合</h4>
      <el-button type="primary" @click="generateSpecCombinations">生成规格组合</el-button>
      
      <div v-if="specCombinations.length > 0" class="combinations-table">
        <el-table :data="specCombinations" border style="width: 100%; margin-top: 20px;">
          <el-table-column label="规格组合">
            <template slot-scope="scope">
              <div v-for="(value, key) in scope.row.specs" :key="key">
                {{ formatSpecKey(key) }}: {{ value }}
              </div>
            </template>
          </el-table-column>
          
          <el-table-column label="价格调整" width="150">
            <template slot-scope="scope">
              <el-input-number 
                v-model="scope.row.priceAdjustment" 
                :precision="2" 
                :step="10"
                :min="-1000"
                :max="1000">
              </el-input-number>
            </template>
          </el-table-column>
          
          <el-table-column label="库存" width="150">
            <template slot-scope="scope">
              <el-input-number 
                v-model="scope.row.stock" 
                :min="0"
                :max="9999">
              </el-input-number>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="actions">
          <el-button type="primary" @click="saveAllSpecifications">保存所有规格</el-button>
        </div>
      </div>
    </div>
    
    <!-- 现有规格管理 -->
    <div class="existing-specs-section" v-if="existingSpecifications.length > 0">
      <h4>现有规格组合</h4>
      <el-table :data="existingSpecifications" border style="width: 100%">
        <el-table-column label="规格组合">
          <template slot-scope="scope">
            <div v-for="(value, key) in scope.row.specifications" :key="key">
              {{ formatSpecKey(key) }}: {{ value }}
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="价格调整" width="120">
          <template slot-scope="scope">
            {{ scope.row.priceAdjustment }}
          </template>
        </el-table-column>
        
        <el-table-column label="库存" width="100">
          <template slot-scope="scope">
            {{ scope.row.stock }}
          </template>
        </el-table-column>
        
        <el-table-column label="销量" width="100">
          <template slot-scope="scope">
            {{ scope.row.sales }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button 
              type="primary" 
              icon="el-icon-edit" 
              size="mini" 
              @click="editSpecification(scope.row)">
            </el-button>
            <el-button 
              type="danger" 
              icon="el-icon-delete" 
              size="mini" 
              @click="deleteSpecification(scope.row.id)">
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      productId: null,
      specOptionsForm: [{ key: 'color', customKey: '', values: [] }],
      specCombinations: [],
      existingSpecifications: []
    };
  },
  methods: {
    getDefaultOptions(key) {
      const optionsMap = {
        'color': ['红色', '蓝色', '黑色', '白色', '灰色'],
        'size': ['S', 'M', 'L', 'XL', 'XXL', '均码'],
        'material': ['棉', '涤纶', '锦纶', '尼龙'],
        'style': ['经典款', '修身款', '宽松款']
      };
      return optionsMap[key] || [];
    },
    
    formatSpecKey(key) {
      const keyMap = {
        'color': '颜色',
        'size': '尺码',
        'material': '材质',
        'style': '款式'
      };
      return keyMap[key] || key;
    },
    
    addSpecOptionRow() {
      this.specOptionsForm.push({ key: '', customKey: '', values: [] });
    },
    
    removeSpecOptionRow(index) {
      this.specOptionsForm.splice(index, 1);
    },
    
    generateSpecCombinations() {
      // 过滤有效的规格选项
      const validOptions = this.specOptionsForm.filter(opt => {
        const key = opt.key === 'custom' ? opt.customKey : opt.key;
        return key && opt.values && opt.values.length > 0;
      });
      
      if (validOptions.length === 0) {
        this.$message.warning('请至少添加一个有效的规格选项');
        return;
      }
      
      // 生成规格选项数据结构
      const optionsData = {};
      validOptions.forEach(opt => {
        const key = opt.key === 'custom' ? opt.customKey : opt.key;
        optionsData[key] = opt.values;
      });
      
      // 生成所有可能的组合
      this.specCombinations = this.generateCombinations(optionsData);
    },
    
    generateCombinations(optionsData) {
      const keys = Object.keys(optionsData);
      if (keys.length === 0) return [];
      
      // 递归生成所有组合
      const combine = (index, current) => {
        if (index === keys.length) {
          return [{ 
            specs: { ...current }, 
            priceAdjustment: 0, 
            stock: 100 
          }];
        }
        
        const key = keys[index];
        const values = optionsData[key];
        const result = [];
        
        values.forEach(value => {
          const newCurrent = { ...current };
          newCurrent[key] = value;
          result.push(...combine(index + 1, newCurrent));
        });
        
        return result;
      };
      
      return combine(0, {});
    },
    
    saveAllSpecifications() {
      // 先保存规格选项
      const specOptions = {};
      this.specOptionsForm.forEach(opt => {
        if (opt.values && opt.values.length > 0) {
          const key = opt.key === 'custom' ? opt.customKey : opt.key;
          if (key) {
            specOptions[key] = opt.values;
          }
        }
      });
      
      // 更新商品以启用规格
      const productUpdate = {
        hasSpecification: 1,
        specOptions: specOptions
      };
      
      // 保存所有生成的规格组合
      const savePromises = this.specCombinations.map(combo => {
        return axios.post(`/api/mall/products/${this.productId}/specifications`, {
          specifications: combo.specs,
          priceAdjustment: combo.priceAdjustment,
          stock: combo.stock
        });
      });
      
      // 执行所有请求
      Promise.all([
        axios.put(`/api/mall/products/${this.productId}`, productUpdate),
        ...savePromises
      ])
        .then(responses => {
          this.$message.success('所有规格保存成功');
          this.loadProductSpecifications();
        })
        .catch(error => {
          console.error('保存规格失败:', error);
          this.$message.error('保存规格失败');
        });
    },
    
    loadProductSpecifications() {
      axios.get(`/api/mall/products/${this.productId}/specifications`)
        .then(response => {
          if (response.data.status === 0) {
            this.existingSpecifications = response.data.data;
          }
        })
        .catch(error => {
          console.error('获取现有规格失败:', error);
        });
    }
  },
  created() {
    // 初始化时获取商品ID和现有规格
    this.productId = this.$route.params.productId;
    if (this.productId) {
      this.loadProductSpecifications();
    }
  }
}
</script>

<style scoped>
.specification-manager {
  padding: 20px;
}
.spec-options-section, .spec-combinations-section, .existing-specs-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}
.spec-option-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.actions {
  margin-top: 20px;
  text-align: right;
}
</style>
```

### 4.5 多规格业务逻辑

#### 4.5.1 处理商品规格数据结构

```javascript
// 将规格数据结构化为前端可用格式
function processSpecData(product, specifications, specOptions) {
  // 基本商品信息
  const result = {
    ...product,
    specGroups: []
  };
  
  // 处理规格选项组
  if (specOptions) {
    Object.keys(specOptions).forEach(key => {
      result.specGroups.push({
        key: key,
        title: formatSpecKey(key),
        options: specOptions[key].map(value => ({
          value: value,
          disabled: !isSpecValueAvailable(specifications, key, value)
        }))
      });
    });
  }
  
  // 处理规格组合
  result.specCombinations = specifications.map(spec => ({
    id: spec.id,
    specs: spec.specifications,
    price: product.price + spec.priceAdjustment,
    priceAdjustment: spec.priceAdjustment,
    stock: spec.stock,
    available: spec.stock > 0 && spec.status === 1
  }));
  
  return result;
}

// 检查规格值是否有可用组合
function isSpecValueAvailable(specifications, key, value) {
  return specifications.some(spec => 
    spec.specifications[key] === value && spec.stock > 0 && spec.status === 1
  );
}
```

#### 4.5.2 根据选择规格更新UI

```javascript
// 根据已选规格筛选可选择的其他规格选项
function updateAvailableOptions() {
  // 深拷贝当前选择的规格
  const currentSelected = { ...this.selectedSpecs };
  
  // 遍历每个规格组
  this.specData.specGroups.forEach(group => {
    const specKey = group.key;
    
    // 遍历该组的每个选项
    group.options.forEach(option => {
      // 暂时忽略当前规格组的选择
      const tempSelected = { ...currentSelected };
      delete tempSelected[specKey];
      
      // 创建新的选择状态，包含当前评估的值
      const testSelection = { 
        ...tempSelected, 
        [specKey]: option.value 
      };
      
      // 检查是否有匹配的组合
      option.disabled = !this.hasMatchingCombination(testSelection);
    });
  });
}

// 检查是否有匹配的规格组合
function hasMatchingCombination(selection) {
  // 获取选择的键
  const selectedKeys = Object.keys(selection);
  
  // 查找匹配的组合
  return this.specData.specCombinations.some(combo => {
    // 检查每个已选规格是否匹配
    return selectedKeys.every(key => 
      combo.specs[key] === selection[key]
    ) && combo.available;
  });
}

// 自动选择第一个可用选项（默认选择）
function autoSelectFirstAvailable() {
  this.specData.specGroups.forEach(group => {
    // 如果该规格类型还未选择
    if (!this.selectedSpecs[group.key]) {
      // 查找第一个可用选项
      const firstAvailable = group.options.find(opt => !opt.disabled);
      if (firstAvailable) {
        this.selectedSpecs[group.key] = firstAvailable.value;
      }
    }
  });
}
```

#### 4.5.3 根据选择的规格找到匹配的规格组合

```javascript
// 获取当前选择的规格组合
function getCurrentSpecCombination() {
  // 检查是否已选择所有必要规格
  const requiredKeys = this.specData.specGroups.map(g => g.key);
  const selectedKeys = Object.keys(this.selectedSpecs);
  
  // 如果未选择所有必要规格，返回null
  if (requiredKeys.some(key => !selectedKeys.includes(key))) {
    return null;
  }
  
  // 查找完全匹配的组合
  return this.specData.specCombinations.find(combo => {
    return requiredKeys.every(key => 
      combo.specs[key] === this.selectedSpecs[key]
    );
  });
}
```

### 4.6 最佳实践和注意事项

1. **规格选择用户体验**：
   - 禁用无效的规格组合选项，避免用户选择无库存的规格
   - 提供直观的视觉反馈，如已选中的规格样式突出显示
   - 动态更新价格和库存信息，帮助用户理解选择的影响

2. **规格管理优化**：
   - 提供批量添加和编辑规格的功能，提高管理效率
   - 实现规格价格和库存的批量更新
   - 提供规格库存预警和销售统计功能

3. **性能考虑**：
   - 对规格选项和组合进行缓存，减少不必要的API请求
   - 使用前端计算避免频繁请求后端
   - 大量规格组合时，考虑分页加载或按需加载

4. **移动端适配**：
   - 确保规格选择UI在移动设备上易于操作
   - 优化触摸交互，提供足够大的点击区域
   - 响应式设计确保在不同尺寸屏幕上的良好体验

5. **错误处理**：
   - 规格组合找不到时提供明确的错误提示
   - 库存不足时及时通知用户
   - 规格选择不完整时禁用"加入购物车"按钮

这些指南和示例代码将帮助开发者实现一个功能完善的商品规格系统，提升用户购物体验和商家管理效率。

## 购物车模块接口文档

### 购物车API接口列表

| 接口URL                           | 方法   | 说明                      | 需要登录 |
|----------------------------------|--------|---------------------------|---------|
| `/api/cart`                      | GET    | 获取购物车列表              | 是      |
| `/api/cart`                      | POST   | 添加商品到购物车            | 是      |
| `/api/cart/{productId}`          | PUT    | 更新购物车商品数量          | 是      |
| `/api/cart/{productId}`          | DELETE | 删除购物车商品              | 是      |
| `/api/cart/select/{productId}`   | PUT    | 选择/取消选择单个商品       | 是      |
| `/api/cart/select-all`           | PUT    | 全选/取消全选              | 是      |
| `/api/cart`                      | DELETE | 清空购物车                 | 是      |

### 接口详细说明

#### 1. 获取购物车列表

**请求方法**：GET

**URL**：`/api/cart`

**请求头**：
```
Authorization: Bearer {token}
```

**响应示例**：
```json
{
    "code": 0,
    "data": {
        "cartItems": [
            {
                "productId": 2,
                "productName": "LINING N9II",
                "productImage": "http://example.com/images/2.jpg",
                "productPrice": 750.00,
                "priceAdjustment": 0.00,
                "specs": {},
                "quantity": 2,
                "selected": true,
                "stock": 85,
                "totalPrice": 1500.00,
                "specificationId": null
            },
            {
                "productId": 100006,
                "productName": "YONEX羽毛球鞋SHB-65Z2",
                "productImage": "images/shoes1.jpg",
                "productPrice": 799.00,
                "priceAdjustment": 30.00,
                "specs": {
                    "color": "蓝色",
                    "size": "40"
                },
                "quantity": 1,
                "selected": true,
                "stock": 8,
                "totalPrice": 829.00,
                "specificationId": 14
            }
        ],
        "totalQuantity": 3,
        "totalPrice": 2329.00,
        "allSelected": true
    }
}
```

> 注意：`productPrice`表示商品的基础价格，`priceAdjustment`表示因规格选择产生的价格调整值，`totalPrice`等于`(productPrice + priceAdjustment) * quantity`。无规格商品的`priceAdjustment`为0。

#### 2. 添加商品到购物车

**请求方法**：POST

**URL**：`/api/cart`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**：
```json
{
    "productId": 100006,
    "quantity": 1,
    "specs": {
        "color": "蓝色",
        "size": "40"
    }
}
```
```json
{
    "productId": 1,
    "quantity": 2,
    "specs": {}  
}
```
// 空对象表示无规格

**响应示例**：
```json
{
    "code": 0,
    "msg": "添加成功",
    "data": null
}
```

#### 3. 更新购物车商品数量

**请求方法**：PUT

**URL**：`/api/cart/{productId}`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**：
```json
{
    "quantity": 3,
    "specs": {
        "color": "蓝色",
        "weight": "轻量级"
    }
}
```
```json
{
    "quantity": 3
}
```

**响应示例**：
```json
{
    "code": 0,
    "msg": "更新成功",
    "data": null
}
```

#### 4. 删除购物车商品

**接口描述**：从购物车中删除指定商品

**请求方法**：DELETE

**URL**：`/api/cart/{productId}`

**支持两种传递规格信息的方式：**

**方式一：使用请求体（推荐）**

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**：
```json
{
    "specs": {
        "color": "蓝色",
        "size": "40"
    }
}
```

**方式二：使用URL查询参数**（可选）

**URL**：`/api/cart/{productId}?specs={"color":"蓝色","size":"40"}`

**请求头**：
```
Authorization: Bearer {token}
```

> 注意：URL查询参数方式中，JSON字符串需要进行URL编码。在Postman中，可以在Params选项卡中添加key为"specs"，value为`{"color":"蓝色","size":"40"}`的参数，Postman会自动进行编码。

**响应示例**：
```json
{
    "code": 0,
    "msg": "删除成功",
    "data": null
}
```

**错误响应**：
```json
{
    "code": 4002,
    "msg": "删除失败，商品可能已不在购物车中",
    "data": null
}
```
或
```json
{
    "code": 401,
    "msg": "请先登录",
    "data": null
}
```

#### 5. 选择/取消选择购物车商品

**请求方法**：PUT

**URL**：`/api/cart/select/{productId}`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**：
```json
{
    "selected": true,
    "specs": {
        "color": "蓝色",
        "size": "40"
    }
}
```

**响应示例**：
```json
{
    "code": 0,
    "msg": "操作成功",
    "data": {
        "cartItems": ["..."],
        "totalQuantity": 2,
        "totalPrice": 598.00,
        "allSelected": true
    }
}
```

#### 6. 全选/取消全选购物车

**请求方法**：PUT

**URL**：`/api/cart/select-all`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求体**：
```json
{
    "selected": true
}
```
```json
{
    "selected": false
}
```

**响应示例**：
```json
{
    "code": 0,
    "msg": "操作成功",
    "data": {
        "cartItems": ["..."],
        "totalQuantity": 5,
        "totalPrice": 1299.00,
        "allSelected": true
    }
}
```

#### 7. 清空购物车

**请求方法**：DELETE

**URL**：`/api/cart`

**请求头**：
```
Authorization: Bearer {token}
```

**响应示例**：
```json
{
    "code": 0,
    "msg": "清空成功",
    "data": null
}
```
1. **常见问题排查**
   
   a. **401 未授权错误**
   - 检查token是否正确设置
   - 检查token是否过期

   b. **404 接口不存在**
   - 检查请求URL是否正确
   - 检查后端服务是否正常启动

   c. **400 参数错误**
   - 检查请求体格式是否正确
   - 检查请求参数是否符合要求

   d. **500 服务器错误**
   - 检查服务器日志查找原因
   - 确认Redis服务是否正常运行

# 订单与支付系统接口文档

## 1. 订单接口

### 1.1 创建订单

**接口描述**：根据购物车中已选中的商品创建新订单

**请求URL**：`/orders`

**请求方式**：`POST`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Long | 订单号 |

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": 1694325687651
}
```

### 1.2 获取订单列表

**接口描述**：获取当前用户的订单列表

**请求URL**：`/orders`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| pageNum | 否 | Integer | 页码，默认为1 |
| pageSize | 否 | Integer | 每页数量，默认为10 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 订单列表数据 |
| &emsp;pageNum | Integer | 当前页码 |
| &emsp;pageSize | Integer | 每页数量 |
| &emsp;size | Integer | 当前页实际数量 |
| &emsp;total | Long | 总数量 |
| &emsp;list | Array | 订单列表 |
| &emsp;&emsp;orderNo | Long | 订单号 |
| &emsp;&emsp;userId | Long | 用户ID |
| &emsp;&emsp;totalPrice | BigDecimal | 订单总价 |
| &emsp;&emsp;paymentType | Integer | 支付类型：1-在线支付 |
| &emsp;&emsp;status | Integer | 订单状态：10-未支付，20-已支付，40-已取消 |
| &emsp;&emsp;statusDesc | String | 订单状态描述 |
| &emsp;&emsp;paymentTime | Date | 支付时间 |
| &emsp;&emsp;pickupCode | String | 取货码 |
| &emsp;&emsp;createTime | Date | 创建时间 |
| &emsp;&emsp;updateTime | Date | 更新时间 |
| &emsp;&emsp;orderItemList | Array | 订单项列表 |
| &emsp;&emsp;&emsp;id | Integer | 订单项ID |
| &emsp;&emsp;&emsp;orderNo | Long | 订单号 |
| &emsp;&emsp;&emsp;productId | Integer | 商品ID |
| &emsp;&emsp;&emsp;productName | String | 商品名称 |
| &emsp;&emsp;&emsp;productImage | String | 商品图片 |
| &emsp;&emsp;&emsp;currentUnitPrice | BigDecimal | 商品单价 |
| &emsp;&emsp;&emsp;quantity | Integer | 商品数量 |
| &emsp;&emsp;&emsp;totalPrice | BigDecimal | 商品总价 |
| &emsp;&emsp;&emsp;priceAdjustment | BigDecimal | 价格调整 |
| &emsp;&emsp;&emsp;specificationId | Integer | 规格ID |
| &emsp;&emsp;&emsp;specs | String | 规格信息 |

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "size": 1,
    "total": 1,
    "list": [
      {
        "orderNo": 1694325687651,
        "userId": 1,
        "totalPrice": 2329.00,
        "paymentType": 1,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": null,
        "pickupCode": null,
        "createTime": "2023-09-10 15:28:07",
        "updateTime": "2023-09-10 15:28:07",
        "orderItemList": [
          {
            "id": 1,
            "orderNo": 1694325687651,
            "productId": 2,
            "productName": "LINING N9II",
            "productImage": "http://example.com/images/2.jpg",
            "currentUnitPrice": 750.00,
            "quantity": 2,
            "totalPrice": 1500.00,
            "priceAdjustment": 0.00,
            "specificationId": null,
            "specs": null
          },
          {
            "id": 2,
            "orderNo": 1694325687651,
            "productId": 100006,
            "productName": "YONEX羽毛球鞋SHB-65Z2",
            "productImage": "images/shoes1.jpg",
            "currentUnitPrice": 799.00,
            "quantity": 1,
            "totalPrice": 829.00,
            "priceAdjustment": 30.00,
            "specificationId": 14,
            "specs": "{color=蓝色, size=40}"
          }
        ]
      }
    ]
  }
}
```

### 1.3 获取订单详情

**接口描述**：根据订单号获取订单详情

**请求URL**：`/orders/{orderNo}`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号（路径参数） |

**响应参数**：

与获取订单列表接口中的单个订单对象相同

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "orderNo": 1694325687651,
    "userId": 1,
    "totalPrice": 2329.00,
    "paymentType": 1,
    "status": 10,
    "statusDesc": "未支付",
    "paymentTime": null,
    "pickupCode": null,
    "createTime": "2023-09-10 15:28:07",
    "updateTime": "2023-09-10 15:28:07",
    "orderItemList": [
      {
        "id": 1,
        "orderNo": 1694325687651,
        "productId": 2,
        "productName": "LINING N9II",
        "productImage": "http://example.com/images/2.jpg",
        "currentUnitPrice": 750.00,
        "quantity": 2,
        "totalPrice": 1500.00,
        "priceAdjustment": 0.00,
        "specificationId": null,
        "specs": null
      },
      {
        "id": 2,
        "orderNo": 1694325687651,
        "productId": 100006,
        "productName": "YONEX羽毛球鞋SHB-65Z2",
        "productImage": "images/shoes1.jpg",
        "currentUnitPrice": 799.00,
        "quantity": 1,
        "totalPrice": 829.00,
        "priceAdjustment": 30.00,
        "specificationId": 14,
        "specs": "{color=蓝色, size=40}"
      }
    ]
  }
}
```

## 2. 支付接口

### 2.1 创建支付订单

**接口描述**：创建支付订单

**请求URL**：`/payments`

**请求方式**：`POST`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号 |
| paymentType | 是 | Integer | 支付类型：1-在线支付 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 支付信息 |
| &emsp;paymentNo | Long | 支付单号 |
| &emsp;orderNo | Long | 订单号 |
| &emsp;totalPrice | BigDecimal | 支付金额 |
| &emsp;paymentType | Integer | 支付类型：1-在线支付 |
| &emsp;status | Integer | 支付状态：10-未支付，20-已支付 |
| &emsp;statusDesc | String | 支付状态描述 |
| &emsp;createTime | Date | 创建时间 |
| &emsp;updateTime | Date | 更新时间 |

**请求示例**：
```
{
  "orderNo": 1694325687651,
  "paymentType": 1
}
```

**响应示例**：

```
{
  "code": 0,
  "msg": "success",
  "data": {
    "paymentNo": 1694325687651,
    "orderNo": 1694325687651,
    "totalPrice": 2329.00,
    "paymentType": 1,
    "status": 10,
    "statusDesc": "未支付",
    "createTime": "2023-09-10 15:28:07",
    "updateTime": "2023-09-10 15:28:07"
  }
}
```

### 2.2 获取支付订单列表

**接口描述**：获取当前用户的支付订单列表

**请求URL**：`/payments`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| pageNum | 否 | Integer | 页码，默认为1 |
| pageSize | 否 | Integer | 每页数量，默认为10 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 支付订单列表数据 |
| &emsp;pageNum | Integer | 当前页码 |
| &emsp;pageSize | Integer | 每页数量 |
| &emsp;size | Integer | 当前页实际数量 |
| &emsp;total | Long | 总数量 |
| &emsp;list | Array | 支付订单列表 |
| &emsp;&emsp;paymentNo | Long | 支付单号 |
| &emsp;&emsp;orderNo | Long | 订单号 |
| &emsp;&emsp;totalPrice | BigDecimal | 支付金额 |
| &emsp;&emsp;paymentType | Integer | 支付类型：1-在线支付 |
| &emsp;&emsp;status | Integer | 支付状态：10-未支付，20-已支付 |
| &emsp;&emsp;statusDesc | String | 支付状态描述 |
| &emsp;&emsp;createTime | Date | 创建时间 |
| &emsp;&emsp;updateTime | Date | 更新时间 |

**响应示例**：

```
{
  "code": 0,
  "msg": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "size": 1,
    "total": 1,
    "list": [
      {
        "paymentNo": 1694325687651,
        "orderNo": 1694325687651,
        "totalPrice": 2329.00,
        "paymentType": 1,
        "status": 10,
        "statusDesc": "未支付",
        "createTime": "2023-09-10 15:28:07",
        "updateTime": "2023-09-10 15:28:07"
      }
    ]
  }
}
```

### 2.3 获取支付订单详情

**接口描述**：根据支付单号获取支付订单详情

**请求URL**：`/payments/{paymentNo}`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| paymentNo | 是 | Long | 支付单号（路径参数） |

**响应参数**：

与获取支付订单列表接口中的单个支付订单对象相同

**响应示例**：

```
{
  "code": 0,
  "msg": "success",
  "data": {
    "paymentNo": 1694325687651,
    "orderNo": 1694325687651,
    "totalPrice": 2329.00,
    "paymentType": 1,
    "status": 10,
    "statusDesc": "未支付",
    "createTime": "2023-09-10 15:28:07",
    "updateTime": "2023-09-10 15:28:07"
  }
}
```

## 3. 订单接口

### 3.1 创建订单

**接口描述**：根据购物车中的商品创建订单

**请求URL**：`/api/mall/orders`

**请求方式**：`POST`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| remark | 否 | String | 订单备注 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Long | 订单号 |

**请求示例**：
``json
{
  "remark": "请尽快发货"
}
```

**响应示例**：

```
{
  "status": 0,
  "msg": "success",
  "data": 1694325687651
}
```

### 3.2 获取订单列表

**接口描述**：获取当前用户的订单列表

**请求URL**：`/api/mall/orders`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| status | 否 | Integer | 订单状态：10-未付款，20-已付款，30-已取消，40-已完成，50-已关闭 |
| pageNum | 否 | Integer | 页码，默认为1 |
| pageSize | 否 | Integer | 每页数量，默认为10 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| status | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 分页订单数据 |
| &emsp;pageNum | Integer | 当前页码 |
| &emsp;pageSize | Integer | 每页数量 |
| &emsp;size | Integer | 当前页实际数量 |
| &emsp;total | Long | 总数量 |
| &emsp;list | Array | 订单列表 |
| &emsp;&emsp;orderNo | Long | 订单号 |
| &emsp;&emsp;userId | Integer | 用户ID |
| &emsp;&emsp;totalPrice | BigDecimal | 订单总价 |
| &emsp;&emsp;paymentType | Integer | 支付类型：1-在线支付 |
| &emsp;&emsp;status | Integer | 订单状态：10-未付款，20-已付款，30-已取消，40-已完成，50-已关闭 |
| &emsp;&emsp;statusDesc | String | 状态描述 |
| &emsp;&emsp;paymentTime | Date | 支付时间 |
| &emsp;&emsp;pickupCode | String | 提货码 |
| &emsp;&emsp;createTime | Date | 创建时间 |
| &emsp;&emsp;updateTime | Date | 更新时间 |
| &emsp;&emsp;orderItemList | Array | 订单明细列表 |
| &emsp;&emsp;&emsp;id | Integer | 明细ID |
| &emsp;&emsp;&emsp;orderNo | Long | 订单号 |
| &emsp;&emsp;&emsp;productId | Integer | 商品ID |
| &emsp;&emsp;&emsp;productName | String | 商品名称 |
| &emsp;&emsp;&emsp;productImage | String | 商品图片 |
| &emsp;&emsp;&emsp;currentUnitPrice | BigDecimal | 商品单价 |
| &emsp;&emsp;&emsp;quantity | Integer | 商品数量 |
| &emsp;&emsp;&emsp;totalPrice | BigDecimal | 商品总价 |
| &emsp;&emsp;&emsp;priceAdjustment | BigDecimal | 价格调整 |
| &emsp;&emsp;&emsp;specificationId | Integer | 规格ID |
| &emsp;&emsp;&emsp;specs | String | 规格信息 |

**响应示例**：

```
{
  "status": 0,
  "msg": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "size": 1,
    "total": 1,
    "list": [
      {
        "orderNo": 1694325687651,
        "userId": 1,
        "totalPrice": 2329.00,
        "paymentType": 1,
        "status": 10,
        "statusDesc": "未支付",
        "paymentTime": null,
        "pickupCode": null,
        "createTime": "2023-09-10 15:28:07",
        "updateTime": "2023-09-10 15:28:07",
        "orderItemList": [
          {
            "id": 1,
            "orderNo": 1694325687651,
            "productId": 2,
            "productName": "LINING N9II",
            "productImage": "http://example.com/images/2.jpg",
            "currentUnitPrice": 750.00,
            "quantity": 2,
            "totalPrice": 1500.00,
            "priceAdjustment": 0.00,
            "specificationId": null,
            "specs": null
          },
          {
            "id": 2,
            "orderNo": 1694325687651,
            "productId": 100006,
            "productName": "YONEX羽毛球鞋SHB-65Z2",
            "productImage": "images/shoes1.jpg",
            "currentUnitPrice": 799.00,
            "quantity": 1,
            "totalPrice": 829.00,
            "priceAdjustment": 30.00,
            "specificationId": 14,
            "specs": "{color=蓝色, size=40}"
          }
        ]
      }
    ]
  }
}
```

### 3.3 获取订单详情

**接口描述**：根据订单号获取订单详情

**请求URL**：`/api/mall/orders/{orderNo}`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号（路径参数） |

**响应参数**：

与获取订单列表接口中的单个订单对象相同

**响应示例**：

```
{
  "status": 0,
  "msg": "success",
  "data": {
    "orderNo": 1694325687651,
    "userId": 1,
    "totalPrice": 2329.00,
    "paymentType": 1,
    "status": 10,
    "statusDesc": "未支付",
    "paymentTime": null,
    "pickupCode": null,
    "createTime": "2023-09-10 15:28:07",
    "updateTime": "2023-09-10 15:28:07",
    "orderItemList": [
      {
        "id": 1,
        "orderNo": 1694325687651,
        "productId": 2,
        "productName": "LINING N9II",
        "productImage": "http://example.com/images/2.jpg",
        "currentUnitPrice": 750.00,
        "quantity": 2,
        "totalPrice": 1500.00,
        "priceAdjustment": 0.00,
        "specificationId": null,
        "specs": null
      },
      {
        "id": 2,
        "orderNo": 1694325687651,
        "productId": 100006,
        "productName": "YONEX羽毛球鞋SHB-65Z2",
        "productImage": "images/shoes1.jpg",
        "currentUnitPrice": 799.00,
        "quantity": 1,
        "totalPrice": 829.00,
        "priceAdjustment": 30.00,
        "specificationId": 14,
        "specs": "{color=蓝色, size=40}"
      }
    ]
  }
}
```

### 1.4 取消订单

**接口描述**：取消未支付的订单

**请求URL**：`/orders/{orderNo}/cancel`

**请求方式**：`POST`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | String | 成功提示信息 |

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": "取消订单成功"
}
```

### 1.5 获取订单状态

**接口描述**：获取订单当前状态

**请求URL**：`/orders/{orderNo}/status`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号（路径参数） |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Integer | 订单状态：10-未支付，20-已支付，40-已取消 |

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": 20
}
```

## 2. 支付接口

### 2.1 创建支付

**接口描述**：创建支付请求，获取支付链接或二维码

**请求URL**：`/pay/create`

**请求方式**：`POST`

**请求头**：
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号 |
| amount | 是 | BigDecimal | 支付金额 |
| businessType | 是 | String | 业务类型：MALL-商城，RESERVATION-预约 |

**请求示例**：

```json
{
  "orderNo": 1694325687651,
  "amount": 2329.00,
  "businessType": "MALL"
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 支付响应信息 |
| &emsp;codeUrl | String | 二维码URL（微信支付） |
| &emsp;orderId | String | 订单ID |
| &emsp;orderAmount | Double | 订单金额 |
| &emsp;outTradeNo | String | 支付流水号 |
| &emsp;qrCodeUrl | String | 二维码图片URL |

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "codeUrl": "weixin://wxpay/bizpayurl?pr=xXKHR7Bzz",
    "orderId": "1694325687651",
    "orderAmount": 2329.00,
    "outTradeNo": "4200001234202309101234567890",
    "qrCodeUrl": "https://example.com/qrcode/1694325687651.png"
  }
}
```

### 2.2 支付结果通知

**接口描述**：接收支付平台的异步通知，更新订单状态（由支付平台回调，前端不需调用）

**请求URL**：`/pay/notify`

**请求方式**：`POST`

**请求参数**：微信或支付宝的XML/JSON通知数据

**响应参数**：根据支付平台要求返回成功标识

### 2.3 查询支付状态

**接口描述**：查询订单支付状态

**请求URL**：`/pay/query`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | Object | 支付信息 |
| &emsp;id | Integer | 支付记录ID |
| &emsp;orderNo | Long | 订单号 |
| &emsp;userId | Long | 用户ID |
| &emsp;payPlatform | Integer | 支付平台：1-支付宝，2-微信 |
| &emsp;platformNumber | String | 支付平台流水号 |
| &emsp;platformStatus | String | 支付平台状态 |
| &emsp;payAmount | BigDecimal | 支付金额 |
| &emsp;status | Integer | 支付状态：0-未支付，1-已支付 |
| &emsp;createTime | Date | 创建时间 |
| &emsp;updateTime | Date | 更新时间 |

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "id": 1,
    "orderNo": 1694325687651,
    "userId": 1,
    "payPlatform": 2,
    "platformNumber": "4200001234202309101234567890",
    "platformStatus": "SUCCESS",
    "payAmount": 2329.00,
    "status": 1,
    "createTime": "2023-09-10 15:30:07",
    "updateTime": "2023-09-10 15:31:28"
  }
}
```

### 2.4 获取支付成功跳转URL

**接口描述**：获取支付成功后的跳转URL

**请求URL**：`/pay/return_url`

**请求方式**：`GET`

**请求头**：
```
Authorization: Bearer {token}
```

**请求参数**：

| 参数名 | 必选 | 类型 | 说明 |
| ------ | ------ | ------ | ------ |
| orderNo | 是 | Long | 订单号 |

**响应参数**：

| 参数名 | 类型 | 说明 |
| ------ | ------ | ------ |
| code | Integer | 响应状态码，0表示成功 |
| msg | String | 响应消息 |
| data | String | 跳转URL |

**响应示例**：

```json
{
  "code": 0,
  "msg": "success",
  "data": "http://localhost:8080/orders?orderNo=1694325687651"
}
```

## 3. 当前实现流程

订单与支付系统的实现流程如下：

### 3.1 订单创建流程

1. 用户在购物车中选择商品并点击"结算"
2. 前端调用创建订单API
3. 后端从购物车中获取已选中的商品
4. 生成订单号（时间戳+随机数）
5. 计算订单总价
6. 创建订单和订单项
7. 清空购物车中已下单的商品
8. 返回订单号给前端

### 3.2 支付流程

1. 前端获取订单号后，调用创建支付API
2. 后端创建支付记录，调用第三方支付平台（微信/支付宝）
3. 返回支付链接或二维码给前端
4. 用户扫码或点击链接完成支付
5. 支付平台通过异步通知接口通知支付结果
6. 后端接收通知，验证签名和金额
7. 更新支付记录状态，通过RabbitMQ发送支付成功消息
8. 支付通知监听器接收消息，根据业务类型处理：
   - 如果是商城订单，调用`mallOrderService.paySuccess(orderNo)`
   - 如果是预约订单，调用`reservationService.paySuccess(orderNo)`
9. 订单服务更新订单状态，生成取货码，扣减商品库存

### 3.3 RabbitMQ消息队列

1. 系统使用RabbitMQ实现支付成功后的异步通知
2. `RabbitMQConfig`类配置了支付通知队列`pay.notify`
3. `PayServiceImpl`在支付成功后发送包含订单号和业务类型的消息
4. `PayNotifyListener`监听队列并根据业务类型分发处理

## 4. 扩展支持预约模块

要扩展支付系统以支持预约模块，需要做以下工作：

### 4.1 预约模块集成

1. **创建预约订单服务**：实现`ReservationService`接口
   ```java
   public interface ReservationService {
       // 创建预约订单
       Long createReservation(ReservationDto dto);
       
       // 查询预约详情
       ReservationVo getReservationDetail(Long reservationId);
       
       // 支付成功回调
       void paySuccess(Long reservationId);
       
       // 其他预约相关方法...
   }
   ```

2. **实现支付成功处理方法**：
   ```java
   @Override
   public void paySuccess(Long reservationId) {
       // 更新预约状态为已支付
       // 生成预约确认码
       // 锁定场地时间段
       // 发送预约成功通知
   }
   ```

3. **更新PayServiceImpl**：
   ```java
   // 添加预约服务依赖
//   @Autowired(required = false)
//   private ReservationService reservationService;
//   
//   // 在异步通知处理中添加预约业务类型处理
//   if (PayService.BUSINESS_TYPE_RESERVATION.equals(businessType)) {
//       if (reservationService != null) {
//           reservationService.paySuccess(payInfo.getOrderNo());
//       } else {
//           logger.error("【支付结果通知】预约服务未注入");
//       }
//   }
   ```

### 4.2 预约模块接口设计

1. **创建预约**：
   ```
   POST /api/reservations
   {
     "courtId": 1,
     "date": "2023-09-15",
     "startTime": "10:00",
     "endTime": "12:00",
     "remark": "双打训练"
   }
   ```
   
2. **获取预约详情**：
   ```
   GET /api/reservations/{reservationId}
   ```
   
3. **支付预约订单**：
   ```
   POST /pay/create
   {
     "orderNo": 1694325687651,
     "amount": 120.00,
     "businessType": "RESERVATION"
   }
   ```

### 4.3 消息队列复用

1. **保持PayNotifyMessage结构**：
   ```java
   public class PayNotifyMessage {
       private Long orderNo;
       private String businessType;
       private Integer payPlatform;
       private String platformNumber;
       private BigDecimal payAmount;
       // getter/setter...
   }
   ```

2. **业务类型常量**：
   ```java
   public interface PayService {
       String BUSINESS_TYPE_MALL = "MALL";
       String BUSINESS_TYPE_RESERVATION = "RESERVATION";
       // 其他方法...
   }
   ```

3. **处理多种业务类型**：
   ```java
   @RabbitListener(queues = RabbitMQConfig.QUEUE_PAY_NOTIFY)
   public void processPayNotify(String message) {
       // 解析消息
       PayNotifyMessage payNotifyMessage = objectMapper.readValue(message, PayNotifyMessage.class);
       
       // 根据业务类型分发处理
       String businessType = payNotifyMessage.getBusinessType();
       Long orderNo = payNotifyMessage.getOrderNo();
       
       if (PayService.BUSINESS_TYPE_MALL.equals(businessType)) {
           // 处理商城订单支付
           mallOrderService.paySuccess(orderNo);
       } else if (PayService.BUSINESS_TYPE_RESERVATION.equals(businessType)) {
           // 处理预约订单支付
           reservationService.paySuccess(orderNo);
       } else {
           log.warn("未知的业务类型: {}, orderNo={}", businessType, orderNo);
       }
   }
   ```

### 4.4 数据库设计

预约模块需要添加以下表：

1. **reservation** - 预约主表
   ```sql
   CREATE TABLE `reservation` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `reservation_no` bigint(20) NOT NULL COMMENT '预约编号',
     `user_id` bigint(20) NOT NULL COMMENT '用户ID',
     `court_id` int(11) NOT NULL COMMENT '场地ID',
     `court_name` varchar(50) NOT NULL COMMENT '场地名称',
     `reservation_date` date NOT NULL COMMENT '预约日期',
     `start_time` time NOT NULL COMMENT '开始时间',
     `end_time` time NOT NULL COMMENT '结束时间',
     `duration` int(11) NOT NULL COMMENT '时长(分钟)',
     `price` decimal(20,2) NOT NULL COMMENT '价格',
     `status` int(11) NOT NULL COMMENT '状态：10-未支付，20-已支付，30-已使用，40-已取消',
     `confirmation_code` varchar(10) DEFAULT NULL COMMENT '确认码',
     `remark` varchar(500) DEFAULT NULL COMMENT '备注',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_time` datetime NOT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `idx_reservation_no` (`reservation_no`),
     KEY `idx_user_id` (`user_id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地预约表';
   ```

2. **court** - 场地表
   ```sql
   CREATE TABLE `court` (
     `id` int(11) NOT NULL AUTO_INCREMENT,
     `name` varchar(50) NOT NULL COMMENT '场地名称',
     `description` varchar(500) DEFAULT NULL COMMENT '场地描述',
     `location` varchar(100) NOT NULL COMMENT '位置',
     `price_per_hour` decimal(10,2) NOT NULL COMMENT '每小时价格',
     `image` varchar(200) DEFAULT NULL COMMENT '场地图片',
     `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-可用，0-不可用',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_time` datetime NOT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地表';
   ```

3. **court_schedule** - 场地排期表
   ```sql
   CREATE TABLE `court_schedule` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `court_id` int(11) NOT NULL COMMENT '场地ID',
     `date` date NOT NULL COMMENT '日期',
     `time_slot` time NOT NULL COMMENT '时间段',
     `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：1-可预约，0-已预约',
     `reservation_id` bigint(20) DEFAULT NULL COMMENT '预约ID',
     `create_time` datetime NOT NULL COMMENT '创建时间',
     `update_time` datetime NOT NULL COMMENT '更新时间',
     PRIMARY KEY (`id`),
     UNIQUE KEY `idx_court_date_time` (`court_id`,`date`,`time_slot`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场地排期表';
   ```

## 5. 最佳实践与注意事项

1. **事务管理**：
   - 订单创建和支付处理需要使用事务确保数据一致性
   - 在支付成功回调中，应使用分布式事务或最终一致性方案

2. **幂等性处理**：
   - 支付通知可能会重复发送，需要确保处理逻辑的幂等性
   - 可以通过检查订单状态或记录已处理的通知来实现

3. **异常处理**：
   - 支付流程中的异常需要有完善的日志记录和通知机制
   - 对于关键操作失败应有补偿机制

4. **安全性**：
   - 支付相关接口需要严格的权限控制
   - 敏感信息（如支付密钥）不应在前端暴露

5. **扩展性**：
   - 使用策略模式处理不同的支付方式和业务类型
   - 接口设计应考虑未来的扩展需求

6. **性能优化**：
   - 支付结果查询可以考虑使用缓存
   - 订单列表接口应支持分页和条件筛选

7. **监控与预警**：
   - 支付流程应有完善的监控指标
   - 关键节点异常应触发预警通知

通过以上设计和实现，系统可以灵活支持商城订单和预约订单的支付处理，同时保持代码的清晰和可维护性。
