# login

| key | name | password | 
|-----|----------|----------| 
| 1   | sjx      | 123abc   |

```
POST http://localhost:7779/login
```

#### Request body

```json
{
  "name": "sjx",
  "password": "123abc"
}
```

#### Response

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

1. 向后端发送companyId，查询对应的itemId和信息

需要返回的数据：

```json
const columns: TableColumnsType = [
{ title: '名称', dataIndex: 'itemName', fixed: 'left',},
{ title: '标准', dataIndex: 'standard', fixed: 'left'},
{ title: '规格', dataIndex: 'specification', width: 100},
{ title: '表面处理', dataIndex: 'surface', width: 100},
{ title: '材质', dataIndex: 'material', width: 100},
{ title: '等级', dataIndex: 'level', width: 100},
{ title: '单重', dataIndex: 'unitWeight', width: 100},
{ title: '单位', dataIndex: 'unit', width: 80},

{ title: '总进库数', dataIndex: 'inCount', width: 100},
{ title: '总出库数', dataIndex: 'outCount', width: 100},
{ title: '初始化数', dataIndex: 'initialCount', width: 100},
{ title: '总库存数', dataIndex: 'totalCount', fixed: "right"},

];
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

TODO LIST 1

```JSON
POST http://localhost:7779/management/initializeItem
```

### Request body

```JSON
{
  "companyId": 1,
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
GET http://localhost:7779/historyRecord/${companyId}
```

### Response

```JSON
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "recordId": 1,
      "itemName": "六角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*15",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "type": "Initialization",
      "date": "2020-01-01",
      "amount": 200,
      "unit": "个",
      "totalWeight": 0.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 0.0
    },
    {
      "recordId": 2,
      "itemName": "螺母",
      "standard": "GB6170",
      "specification": "M8*30",
      "material": "-1",
      "level": "5级",
      "surface": "热镀锌",
      "unitWeight": -1.0,
      "type": "Initialization",
      "date": "2024-01-19",
      "amount": 18500,
      "unit": "个",
      "totalWeight": 0.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 0.0
    },
    {
      "recordId": 3,
      "itemName": "螺母",
      "standard": "GB6170",
      "specification": "M8*31",
      "material": "-1",
      "level": "5级",
      "surface": "热镀锌",
      "unitWeight": -1.0,
      "type": "Initialization",
      "date": "2024-01-19",
      "amount": 200,
      "unit": "个",
      "totalWeight": 0.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 0.0
    },
    {
      "recordId": 4,
      "itemName": "八角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*35",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "type": "Initialization",
      "date": "2020-01-01",
      "amount": 200,
      "unit": "个",
      "totalWeight": 0.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 0.0
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
      "itemName": "六角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*15",
      "material": "铁",
      "level": "6.8",
      "surface": "35K 光身",
      "unitWeight": 16.2,
      "type": "Initialization",
      "date": "2020-01-01",
      "amount": 200,
      "unit": "个",
      "totalWeight": 0.0,
      "direction": null,
      "userName": "sjx",
      "unitPrice": 0.0
    }
  ]
}
```

## delete the latest record

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


# Invoice

## add invoice

```JSON
POST http://localhost:7779/invoice/addInvoice
```

### Request Body

```JSON
{
  "companyId": 1,
  "invoiceNum": "1234",
  "invoiceCode": "abcd",
  "taxRate": 0.45,
  "comment": "123",
  "startDate": "2020-1-1",
  "endDate": "2020-1-5"
}
```

### Response

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": 100000.0
}
```

## update invoice

```JSON
PUT http://localhost:7779/invoice/updateInvoice
```

### Request Body

```JSON
{
  "invoiceId": 1,
  "companyId": 1,
  "invoiceNum": "1234",
  "invoiceCode": "abcd",
  "taxRate": 0.45,
  "comment": "123",
  "startDate": "2020-1-1",
  "endDate": "2020-1-5",
  "money": 200000.0
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


## delete invoce

```JSON
DELETE http://localhost:7779/invoice/${invoiceId}
``` 

### Response

```JSON
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

Feedback:
1. 初始化有bug
2. 出库/入库可以改（自己输入的数据）
3. 历史可以删除
4. 分页
5. 一种零件可以看到从哪些公司进货
6. 入库可以选择输入重量还是数量



TODO:
1. 初始化入库（创建Item+创建Record） Done
2. Record List（按照公司ID查询，按照itemId+companyId查询）Done
2. 添加Record（入库+出库）(入库直接用公斤数来算amount) Done
3. 删除Record（只能删除第一个） Done
4. 发票添加、删除、更新、列表
5. excel导入导出
6. 用户登录
7. 潜在的删除数据导致的查询bug



