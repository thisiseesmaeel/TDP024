package main

import (
	_ "github.com/lib/pq"
    "database/sql"
    "encoding/json"
    "fmt"
    "io/ioutil"
    "log"
    "net/http"
    "strconv"
    "strings"
)

type Task struct {
    Id int
    Name string
    Done bool
    ListId int
}

type CreateTaskRequest struct {
    Name string `json:"name"`
    ListId int  `json:"list_id"`
}

type List struct {
    Id int
    Name string
}

type ListCreateRequest struct {
    Name string
}

type ListCreateResponse struct {
    Id int
}

func CheckFatal(err error) {
    if err != nil {
        log.Fatal(err)
    }
}

type Database struct {
    Db *sql.DB
}


func getURLParameter(path string) *string {
    param := strings.Split(path, "/")
    if len(param) == 3 {
        return &param[2]
    } else {
        return nil
    }
}

func getLists(db *sql.DB) []List {
    rows, err := db.Query("select * from list")
    CheckFatal(err)

    // Retrieve all lists from the query
    res := make([]List, 0)
    for rows.Next() {
        list := List{}
        err := rows.Scan(&list.Id, &list.Name)
        CheckFatal(err)
        res = append(res, list)
    }

    return res
}

func getTasks(db *sql.DB, listId int) []Task {
    // Query the database for all tasks that references the specified list
    rows, err := db.Query("select * from task where list=$1", listId)
    CheckFatal(err)

    // Retrieve all tasks from the query
    res := make([]Task, 0)
    for rows.Next() {
        var name string
        var id, list int
        var done bool
        err := rows.Scan(&id, &name, &done, &list)
        CheckFatal(err)
        res = append(res, Task{Id: id, Name: name, Done: done, ListId: list})
    }

    return res
}

func insertList(db *sql.DB, listName string) int {
    var listId int
    err := db.QueryRow("insert into list (name) values ($1) returning id", listName).Scan(&listId)
    CheckFatal(err)

    return listId
}

func insertTask(db *sql.DB, taskName string, listId int) {
    _, err := db.Exec("insert into task (name, list) values ($1, $2)", taskName, listId)
    // Handle non-existing list id
    CheckFatal(err)
}

func (db *Database) listHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method == "GET" {
        // Handle GET Request
        param := getURLParameter(r.URL.Path)

        // If no parameter exists, retrieve all lists
        if param == nil || *param == "" {
            // Retrieve lists
            list := getLists(db.Db)
            json.NewEncoder(w).Encode(&list)
        } else {
            // Get the list id from the parameter
            listId, err := strconv.Atoi(*param)
            CheckFatal(err)

            // Retrieve tasks and send them back
            tasks := getTasks(db.Db, listId)
            json.NewEncoder(w).Encode(&tasks)
        }
    } else if r.Method == "POST" {
        // Parse the request and create a new list
        body, err := ioutil.ReadAll(r.Body)
        CheckFatal(err)
        listRequest := ListCreateRequest{}
        err = json.Unmarshal(body, &listRequest)
        CheckFatal(err)

        listResponse := ListCreateResponse{}
        listResponse.Id = insertList(db.Db, listRequest.Name)

        json.NewEncoder(w).Encode(&listResponse)
    }
}

func (db *Database) taskHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method == "POST" {
        body, err := ioutil.ReadAll(r.Body)
        CheckFatal(err)
        taskRequest := CreateTaskRequest{}
        err = json.Unmarshal(body, &taskRequest)
        CheckFatal(err)

        insertTask(db.Db, taskRequest.Name, taskRequest.ListId)

        fmt.Fprintf(w, "OK")
    }
}

func ConnectDb() *sql.DB {
    db, err := sql.Open("postgres", "postgres://simon@localhost/todo?sslmode=disable")
    CheckFatal(err)

    return db
}

func Handlers() *http.ServeMux {
    db := Database{Db: ConnectDb()}
    mux := http.NewServeMux()
    mux.Handle("/list", http.HandlerFunc(db.listHandler))
    mux.Handle("/list/", http.HandlerFunc(db.listHandler))
    mux.Handle("/task", http.HandlerFunc(db.taskHandler))
    return mux
}

func main() {
    // Listen on port 5050
    err := http.ListenAndServe(":5050", Handlers())
    CheckFatal(err)
}
