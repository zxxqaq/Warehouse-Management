# API Doc

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

##### success

- status code: `200 OK`

```json
{
  "userFullName": "张显"
}
```

##### fail

- status code: `4xx` or `5xx`
- error message should be included in the response body

```json
{
  "error": "Invalid credentials"
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

#### success

- status code: `200 OK`

```JSON
[
	{
		key: '1',   
		name: '嘉兴博羽股份有限公司',  
		money: '20000',  
		taxNum: '001',  
		taxRate: '0.3',  
		invoiceCode: '001',  
		invoiceNum: '001'
	},
	{
		key: '2',   
		name: '嘉兴猪猪股份有限公司',  
		money: '20000',  
		taxNum: '001',  
		taxRate: '0.3',  
		invoiceCode: '001',  
		invoiceNum: '001'
	},
]

```

#### fail

- status code: `400`

## deleteOverview

```
DELETE http://localhost:8080/deleteOverview/${key}
```

### Params

- `key`: 要删除的数据的唯一标识符

### Response

#### success

- `200 OK`：删除成功

#### fail

- `404 Not Found`：未找到指定的概览数据
- `500 Internal Server Error`：服务器内部错误



