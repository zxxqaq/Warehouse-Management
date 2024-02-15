# login

| key | name | password | 
|-----|----------|----------| 
| 1   | sjx      | 123abc   |

```
POST http://localhost:7779/login
```

## Request body

```json
{
  "username": "sjx",
  "password": "123abc"
}
```

## Response


```json
{
  "code": 200,
  "msg": "操作成功",
  "data": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OTFjZmU4OTE3NDU0MGE1YjM0NzIxY2I1MjUwNThmMCIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTcwNjA3OTY4NSwiZXhwIjoxNzA2MTY2MDg1fQ.Pr0KbSFU90wJdRVztP99Kv4qFo98___tgA1X8HxL3YI"
}

```

data 中存有该用户的 token，除了 login 和 register 操作以外均需要在请求头中携带 token 字段，如果没有 token 字段则报错如下

```JSON
{
  "timestamp": "2024-01-24",
  "status": 403,
  "error": "Forbidden",
  "path": "/historyRecord/1/1"
}
```


# Register

```
POST http://localhost:7779/register
```

## Request body

```json
{
  "username": "sjx",
  "password": "123abc"
}
```

## Response

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

# Company overview

| key | name                 | taxNum |
| --- | -------------------- |--------|
| 0   | 嘉兴博羽股份有限公司 | 10000000   |
|  1   |     嘉兴猪猪股份有限公司  | 20000000   |

## get company list

```
GET http://localhost:7779/overview/companyList
```

### Response


```JSON
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "companyId": 1,
      "companyName": "嘉兴博羽股份有限公司",
      "taxNum": "10000000"
    },
    {
      "companyId": 2,
      "companyName": "嘉兴猪猪股份有限公司",
      "taxNum": "20000000"
    }
  ]
}
```

## delete company

```
DELETE http://localhost:7779/overview/${companyId}
```

请求参数在path中

例如： http://localhost:7779/overview/2 代表删除companyId=2的这条数据

### Response

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

注意测试删除后在列表中是否查看不到该条数据
数据库中该条数据还是存在的，只是修改了逻辑删除字段的值

## add company 

``` json
POST http://localhost:7779/overview/addCompany
```

### Request body

```json
{
  "companyName": "嘉兴海豹股份有限公司",
  "taxNum": "20000003"
}
```
### Response

在成功后还要传回来新建公司的companyId，不然前端怎么知道

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```
## update company

```json
  PUT http://localhost:7779/overview/updateCompany
```
### Request body

```json
{
  "companyId": 3,
  "companyName": "嘉兴海豹股份有限公司",
  "taxNum": "20000005"
}
```

### Response

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```
# Management


## total item

```JSON
GET http://localhost:7779/management
```

### Response Body

```JSON
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "itemId": 4,
      "itemName": "六角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*15",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "unit": "个",
      "inCount": 110,
      "outCount": 0,
      "initialCount": 200,
      "totalCount": 310
    },
    {
      "itemId": 5,
      "itemName": "螺母",
      "standard": "GB6170",
      "specification": "M8*30",
      "material": "-1",
      "level": "5级",
      "surface": "热镀锌",
      "unitWeight": -1.0,
      "unit": "个",
      "inCount": 0,
      "outCount": 0,
      "initialCount": 18510,
      "totalCount": 18510
    },
    {
      "itemId": 6,
      "itemName": "螺母",
      "standard": "GB6170",
      "specification": "M8*31",
      "material": "-1",
      "level": "5级",
      "surface": "热镀锌",
      "unitWeight": -1.0,
      "unit": "个",
      "inCount": 0,
      "outCount": 0,
      "initialCount": 200,
      "totalCount": 200
    },
    {
      "itemId": 7,
      "itemName": "八角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*35",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "unit": "个",
      "inCount": 30,
      "outCount": 40,
      "initialCount": 200,
      "totalCount": 190
    },
    {
      "itemId": 8,
      "itemName": "六角螺栓",
      "standard": " GB/T 5783-2016",
      "specification": "M16*40",
      "material": "锌",
      "level": "5.6",
      "surface": "35K 光身",
      "unitWeight": 94.1,
      "unit": "个",
      "inCount": 0,
      "outCount": 0,
      "initialCount": 100,
      "totalCount": 100
    },
    {
      "itemId": 9,
      "itemName": "8.8级外六角螺栓",
      "standard": " GB/T 5783-2016",
      "specification": "M16×45",
      "material": "锌",
      "level": "40Cr  8.8",
      "surface": "40Cr 没热处理",
      "unitWeight": 94.6,
      "unit": "个",
      "inCount": 0,
      "outCount": 0,
      "initialCount": 0,
      "totalCount": 0
    }
  ]
}

```

## get item list

```JSON
GET http://localhost:7779/management/${companyId}
```

### Response

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "itemId": 4,
            "itemName": "六角螺栓",
            "standard": "GB/T 5783-2016",
            "specification": "M8*15",
            "material": "铁",
            "level": "6.8",
            "surface": "35K 光身",
            "unitWeight": 16.2,
            "unit": "个",
            "inCount": null,
            "outCount": null,
            "initialCount": 200,
            "totalCount": null
        },
        {
            "itemId": 5,
            "itemName": "螺母",
            "standard": "GB6170",
            "specification": "M8*30",
            "material": "-1",
            "level": "5级",
            "surface": "热镀锌",
            "unitWeight": -1.0,
            "unit": "个",
            "inCount": null,
            "outCount": null,
            "initialCount": 18500,
            "totalCount": null
        },
        {
            "itemId": 6,
            "itemName": "螺母",
            "standard": "GB6170",
            "specification": "M8*31",
            "material": "-1",
            "level": "5级",
            "surface": "热镀锌",
            "unitWeight": -1.0,
            "unit": "个",
            "inCount": null,
            "outCount": null,
            "initialCount": 200,
            "totalCount": null
        },
        {
            "itemId": 7,
            "itemName": "八角螺栓",
            "standard": "GB/T 5783-2016",
            "specification": "M8*35",
            "material": "铁",
            "level": "6.8",
            "surface": "35K 光身",
            "unitWeight": 16.2,
            "unit": "个",
            "inCount": null,
            "outCount": null,
            "initialCount": 200,
            "totalCount": null
        }
    ]
}
```



## initialize item


```JSON
POST http://localhost:7779/management/initializeItem
```

### Request body

```JSON
{
  "companyId": 1, // 这个删掉了
  "itemName": "六角螺栓",
  "standard": "GB/T 5783-2016",
  "specification": "M8*35",
  "surface": "35K 光身",
  "material": "铁",
  "level": "6.8",
  "unitWeight": 16.2, //kg
  "unit": "个",
  "amount": 200,
  "date": "2020-01-01"
}
```

### Response

#### 添加成功
```JSON
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```
#### 零件重复
零件重复是根据哪几个数据决定的？
```JSON
{
  "code": 501,
  "msg": "零件已存在",
  "data": null
}
```


## input item

```JSON
POST http://localhost:7779/management/inputItem
```

### Request Body

```JSON
{
  "companyId": 1,
  "itemId": 1,
  "totalWeight": 1620,
  "unitPrice": 500,
  "date": "2020-01-02"
}
```

### Response Body

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

## output item

```JSON
POST http://localhost:7779/management/outputItem
```
### Request Body

```JSON
{
  "companyId": 1,
  "itemId": 1,
  "amount": 20,
  "date": "2020-01-03",
  "direction": "北京"
}
```
### Response Body

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```



# History Record

## get record list by company Id

```JSON
GET http://localhost:7779/historyRecord/companyId/${companyId}
```

### Response

```JSON
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "recordId": 15,
      "type": "Initialization",
      "date": "2024-01-21",
      "amount": 10,
      "unit": "个",
      "totalWeight": null,
      "direction": null,
      "userName": "sjx",
      "unitPrice": null,
      "itemName": "螺母",
      "standard": "GB6170",
      "specification": "M8*30",
      "material": "-1",
      "level": "5级",
      "surface": "热镀锌",
      "unitWeight": -1.0,
      "isCheck": false,
      "companyName": "嘉兴海豹股份有限公司"
    },
    {
      "recordId": 17,
      "type": "Initialization",
      "date": "2024-01-23",
      "amount": 100,
      "unit": "个",
      "totalWeight": null,
      "direction": null,
      "userName": "sjx",
      "unitPrice": null,
      "itemName": "六角螺栓",
      "standard": " GB/T 5783-2016",
      "specification": "M16*40",
      "material": "锌",
      "level": "5.6",
      "surface": "35K 光身",
      "unitWeight": 94.1,
      "isCheck": false,
      "companyName": "嘉兴海豹股份有限公司"
    }
  ]
}
```

## get record list by item id

```JSON
GET http://localhost:7779/historyRecord/itemId/${itemId}
```

### Response Body

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "recordId": 2,
            "type": "Initialization",
            "date": "2024-01-19",
            "amount": 18500,
            "unit": "个",
            "totalWeight": 0.0,
            "direction": null,
            "userName": "sjx",
            "unitPrice": 0.0,
            "itemName": "螺母",
            "standard": "GB6170",
            "specification": "M8*30",
            "material": "-1",
            "level": "5级",
            "surface": "热镀锌",
            "unitWeight": -1.0,
            "isCheck": false,
            "companyName": "嘉兴博羽股份有限公司"
        },
        {
            "recordId": 15,
            "type": "Initialization",
            "date": "2024-01-21",
            "amount": 10,
            "unit": "个",
            "totalWeight": null,
            "direction": null,
            "userName": "sjx",
            "unitPrice": null,
            "itemName": "螺母",
            "standard": "GB6170",
            "specification": "M8*30",
            "material": "-1",
            "level": "5级",
            "surface": "热镀锌",
            "unitWeight": -1.0,
            "isCheck": false,
            "companyName": "嘉兴海豹股份有限公司"
        }
    ]
}
```

## get record list by company Id and item Id

```JSON
GET http://localhost:7779/historyRecord/${companyId}/${itemId}
```

### Response

```JSON
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "recordId": 1,
      "type": "Initialization",
      "date": "2020-01-01",
      "amount": 200,
      "unit": "个",
      "totalWeight": 0.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 0.0,
      "itemName": "六角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*15",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "isCheck": false,
      "companyName": "嘉兴博羽股份有限公司"
    },
    {
      "recordId": 7,
      "type": "Input",
      "date": "2020-01-02",
      "amount": 100,
      "unit": "个",
      "totalWeight": 1620.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 500.0,
      "itemName": "六角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*15",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "isCheck": false,
      "companyName": "嘉兴博羽股份有限公司"
    },
    {
      "recordId": 16,
      "type": "Input",
      "date": "2024-01-21",
      "amount": 10,
      "unit": "个",
      "totalWeight": 162.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 10.0,
      "itemName": "六角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*15",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "isCheck": false,
      "companyName": "嘉兴博羽股份有限公司"
    }
  ]
}
```

## delete record

```JSON
DELETE http://localhost:7779/historyRecord/${recordId}
```

### Response 

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```


## update record

```JSON
PUT http://localhost:7779/historyRecord/updateRecord
```

### Request Body

这里的JSON需要更新哪些字段就添加进来哪些字段。
```JSON
{
  "recordId": 5,
  "totalWeight": 100
}
```

### Response

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

# 后端待修改
1. (check) Management 新建一个零件的时候不用companyId 什么叫不用不用？ done 
2. (有报错）Management 入库的时候如果是数量的话先把重量输成0，再调用update的接口 （不行 - 后端给一个入库用'数量'的接口）done 可以直接用原来的接口
3. (check) Management 出库不用companyId done 
4. (check) Management item可以编辑 done 
   5. UpdateItemDto Management controller
5. (check) History initialize类型的不用返回 哪里不用返回？ 分公司查询record的时候不用返回
6. (check) History 要总的所有操作历史的接口 都需要返回什么？done (getHistoryRecordList)
7. excel 导入/导出




# 修改

1. 新建的时候新建一个零件，不用companyId （后端改一下）
2. 锁死总库存 done
3. 在操作显示入库 出库栏 done
4. 在入库加入选择公司 done
5. 在入库可以选择数量还是质量 done
6. 入库的时候如果是数量的话先把重量输成0，再调用update的接口 （不行 - 后端给一个入库用数量的接口）
7. 出库不用companyId （后端改一下） 
8. 要总的所有操作历史的接口（后端提供）
9. item可以编辑 （后端）
9. initialize类型的在History的不用返回（后端提供）
10. 总览点击详情，跳转的是操作历史该companyId的所有入库记录（因为出库没有记companyId）
11. 全部的筛选排序management done
12. history的筛选排序 

# Feedback:

1. 入库可以选入库数量还是重量
2. 出库是从总库存里出库
3. 排序和筛选（所有）
4. 库存管理： （分类tag） 原材料、包装物、半成品、成品
5. excel导出/导入


# TODO
0. 服务器开机自动执行命令行，启动后端和nginx代理
1. 表格筛选和排序（需要后端把仓库里的切换回开发环境）
2. excel导入导出
3. 不同用户授权（需要再问清楚一下需求）
1. item可以编辑
3. 前端接口封装
4. 重构
5. 代码规范
6. react
7. 组件和响应式布局



