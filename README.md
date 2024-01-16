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

## delete company list

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
