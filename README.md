# EmployeeManager
**Get child departments**
----
  Returns json data about child departments of selected department. 

* **URL**

  /departments/{depId}/subs

* **Method:**

  `GET`
  
*  **URL Params** 

   **Required:**
 
   `depId=[integer]`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** <br />`[ `<br />
  `{
    "id": 3,
    "name": "Development",
    "chiefId": 2,
    "dismissed": false,
    "parent_id": 1
  },` <br />
  `{
    "id": 2,
    "name": "Financial Consulting",
    "chiefId": 5,
    "dismissed": false,
    "parent_id": 1
  }`<br />
`]`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ "code": 1, "message": "Department [{depId}] not found."}`

* **Sample Call:**

  ```
  $.ajax({
      url: "/departments/1/subs",
      dataType: "json",
      type : "GET",
      success : function(r) {
        console.log(r);
      }
    });
    ```
