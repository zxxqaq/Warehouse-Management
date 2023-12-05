# login

| key | username | password | userFullName |
| --- | -------- | -------- | ------------ |
| 0   | zz       | 1234     | 张显         |
| 1   | sun      | 666      | 孙敬轩       |
|     |          |          |              |

```
POST http://localhost:8080/login
```

#### Request body

```json
{
  "username": "example_user",
  "password": "example_password"
}
```

#### Response

```json
{
    "code": 200,
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0ODBmOThmYmJkNmI0NjM0OWUyZjY2NTM0NGNjZWY2NSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY0Mzg3NDMxNiwiZXhwIjoxNjQzOTYwNzE2fQ.ldLBUvNIxQCGemkCoMgT_0YsjsWndTg5tqfJb77pabk",
        "userInfo": {
            "avatar": " ",
			"userFullName": "张显"
        }
    },
    "message": "success"
}

```

# Overview

| key | name                 | money | taxNum | taxRate | invoiceCode | invoiceNum |
| --- | -------------------- | ----- | ------ | ------- | ----------- | ---------- |
| 0   | 嘉兴博羽股份有限公司 | 2000  | 001    | 0.3     |     001     |001            |
|  1   |     嘉兴猪猪股份有限公司  | 2000  | 001    | 0.3 |  001   |  001          |

## getOverview

```
GET http://localhost:8080/getOverview
```

### Response


```JSON
{
	"code": 200,
	"data": [
			{
				"key": "1",   
				"name": "嘉兴博羽股份有限公司",  
				"money": "20000",  
				"taxNum": "001",  
				"taxRate": "0.3",  
				"invoiceCode": "001",  
				"invoiceNum": "001"
			},
			{
				"key": "2",   
				"name": "嘉兴猪猪股份有限公司",  
				"money": "20000",  
				"taxNum": "001",  
				"taxRate": "0.3",  
				"invoiceCode": "001",  
				"invoiceNum": "001"
			}
			],
	"message": "success"
}
```

## deleteOverview

```
DELETE http://localhost:8080/deleteOverview/${key}
```

请求参数在path中

例如： http://localhost:8080/deleteOverview/6 代表删除key=6的这条数据

### Response

```json
{ 
	"code":200, 
	"message":"操作成功" 
}
```

注意测试删除后在列表中是否查看不到该条数据
数据库中该条数据还是存在的，只是修改了逻辑删除字段的值
