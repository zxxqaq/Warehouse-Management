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

需要取得的数据：

```JSON
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    { //Item
      "itemId": 1,
      "itemName": "六角螺栓",
      "standard": "GB/T 5783-2016",
      "specification": "M8*35",
      "surface": "35K 光身",
      "material": "铁",
      "level": "6.8",
      "unitWeight": 16.2, //kg
      "unit": "个",
      // 统计的结果
      "inCount": 200,
      "outCount": 100,
      "weightCount": 1620, //totalCount * unitWeight
      "totalCount": 100, //inCount-outCount
    },

  ]
}
```


TODO:
1. 初始化入库（创建Item+创建Record）
2. 添加Record（入库+出库）
3. 删除Record
4. Record List（按照公司ID查询，按照itemId+companyId查询）


